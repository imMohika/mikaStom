package ir.mohika.mikastom.module.modules.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(chain = true)
public class Query {
  private final String statement;
  private final Map<Integer, Object> statementValues = new HashMap<>();
  private final Set<Query> requirements = new HashSet<>();
  private final CompletableFuture<ResultSet> completableFuture = new CompletableFuture<>();
  private Consumer<ResultSet> consumer;
  private int failedAttempts = 0;

  @Setter private int statusCode = StatusCode.NOT_STARTED.getCode();

  private Query(String statement) {
    this.statement = statement;
  }

  public Query addRequirement(Query query) {
    requirements.add(query);
    return this;
  }

  public boolean hasDoneRequirements() {
    return requirements.stream()
        .allMatch(req -> req.getStatusCode() == StatusCode.FINISHED.getCode());
  }

  public void complete(ResultSet result) {
    completableFuture.complete(result);
    if (consumer != null) {
      consumer.accept(result);
    }
  }

  public Query onComplete(Consumer<ResultSet> consumer) {
    this.consumer = consumer;
    return this;
  }

  public void increaseFailedAttempts() {
    failedAttempts++;
  }

  public Query setStatementValue(int index, Object value) {
    statementValues.put(index, value);
    return this;
  }

  public PreparedStatement createPreparedStatement(Connection connection) {
    if (connection == null) {
      throw new NullPointerException("Can't prepare statement while connection is null");
    }

    try {
      PreparedStatement preparedStatement = connection.prepareStatement(statement);
      for (Map.Entry<Integer, Object> entry : statementValues.entrySet()) {
        preparedStatement.setObject(entry.getKey(), entry.getValue());
      }
      return preparedStatement;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create PreparedStatement", e);
    }
  }

  public static Query query(String statement) {
    return new Query(statement);
  }

  public enum Priority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW
  }

  @Getter
  public enum StatusCode {
    NOT_STARTED(-1),
    RUNNING(0),
    FAILED(1),
    FINISHED(2);

    private final int code;

    StatusCode(int code) {
      this.code = code;
    }
  }

  public record QueryResult(StatusCode statusCode, Optional<ResultSet> result) {}
}
