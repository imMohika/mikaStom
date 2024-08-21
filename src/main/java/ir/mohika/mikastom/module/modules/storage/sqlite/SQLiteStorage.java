package ir.mohika.mikastom.module.modules.storage.sqlite;

import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.module.modules.storage.Query;
import ir.mohika.mikastom.module.modules.storage.Storage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;

public class SQLiteStorage extends Storage {
  private final File dbFile;
  private Connection connection;
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  @Getter @Setter private boolean shutdownScheduled = false;
  @Getter @Setter private CompletableFuture<Void> shutdownFuture;

  public SQLiteStorage(Path dbPath) {
    this(dbPath.toFile());
  }

  public SQLiteStorage(File dbFile) {
    this.dbFile = dbFile;

    if (!dbFile.exists()) {
      try {
        if (dbFile.getParentFile() != null) {
          Files.createDirectories(dbFile.getParentFile().toPath());
        }
        Files.createFile(dbFile.toPath());
      } catch (IOException e) {
        Log.getLogger().error("Failed to create the sqlite database file", e);
      }
    }
  }

  @Override
  public void connect() {
    try {
      Class.forName("org.sqlite.JDBC");
      if (this.connection == null) {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getPath());
      }

      startQueue();
    } catch (SQLException e) {
      Log.getLogger().error("Failed to connect to the sqlite database", e);
    } catch (ClassNotFoundException e) {
      Log.getLogger().error("SQLite JDBC driver not found", e);
    }
  }

  @Override
  public Query.QueryResult run(Query query) {
    return executeQuerySync(query);
  }

  public Query.QueryResult executeQuerySync(Query query) {
    try {
      var preparedStatement = query.createPreparedStatement(connection);
      ResultSet resultSet = null;

      if (query.getStatement().startsWith("INSERT")
          || query.getStatement().startsWith("UPDATE")
          || query.getStatement().startsWith("DELETE")
          || query.getStatement().startsWith("CREATE")
          || query.getStatement().startsWith("ALTER")) {
        preparedStatement.executeUpdate();
        preparedStatement.close();
      } else {
        resultSet = preparedStatement.executeQuery();
      }

      query.complete(resultSet);

      if (resultSet == null) {
        return new Query.QueryResult(Query.StatusCode.FINISHED, Optional.empty());
      }

      return new Query.QueryResult(Query.StatusCode.FINISHED, Optional.of(resultSet));
    } catch (SQLException e) {
      onQueryFail(query, e);

      query.increaseFailedAttempts();
      if (query.getFailedAttempts() > failAttemptRemoval) {
        onQueryRemoveDueToFail(query);
        return new Query.QueryResult(Query.StatusCode.FINISHED, null);
      }
    }
    return new Query.QueryResult(Query.StatusCode.FAILED, null);
  }

  protected void tick() {
    for (Query.Priority priority : Query.Priority.values()) {
      var queries = queue.get(priority);
      if (queries == null || queries.isEmpty()) continue;

      var query = queries.getFirst();
      if (executeQuerySync(query).statusCode() == Query.StatusCode.FINISHED) {
        queries.removeFirst();
      }
      break;
    }
  }

  @Override
  public CompletableFuture<Void> scheduleShutdown() {
    if (isShutdownScheduled()) {
      return shutdownFuture;
    }
    shutdownFuture = new CompletableFuture<>();

    if (isQueueEmpty()) {
      forceShutdown();
      shutdownFuture.complete(null);
      return shutdownFuture;
    }

    setShutdownScheduled(true);
    return shutdownFuture;
  }

  @Override
  public void forceShutdown() {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        Log.getLogger().error("Failed to close the sqlite database connection", e);
      }
    }
  }

  @Override
  protected void startQueue() {
    if (isQueueRunning()) return;
    setQueueRunning(true);
    executor.scheduleAtFixedRate(
        () -> {
          Log.getLogger().info("meow");
          if (!isQueueEmpty()) {
            tick();
          } else {
            stopQueue();
          }
        },
        0,
        1,
        TimeUnit.MILLISECONDS);
  }

  private void stopQueue() {
    if (!isQueueRunning()) return;
    executor.shutdown();
    try {
      if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
    setQueueRunning(false);
    if (isShutdownScheduled()) {
      forceShutdown();
      if (shutdownFuture != null && !shutdownFuture.isDone()) {
        shutdownFuture.complete(null);
      }
    }
  }

  private void onQueryFail(Query query, SQLException e) {
    Log.getLogger().error("Failed to execute query: {}", query.getStatement(), e);
  }

  private void onQueryRemoveDueToFail(Query query) {
    Log.getLogger().info("Removing query due to failure: {}", query.getStatement());
  }
}
