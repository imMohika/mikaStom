package ir.mohika.mikastom.module.modules.storage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.Setter;

public abstract class Storage {
  protected final Map<Query.Priority, List<Query>> queue = new EnumMap<>(Query.Priority.class);
  @Getter @Setter protected boolean queueRunning = false;
  protected final int failAttemptRemoval = 3;

  protected Storage() {
    for (Query.Priority priority : Query.Priority.values()) {
      queue.put(priority, new ArrayList<>());
    }
  }

  public abstract void connect();

  public abstract CompletableFuture<Void> scheduleShutdown();

  public abstract void forceShutdown();

  public Query enqueue(Query query, Query.Priority priority) {
    queue.get(priority).add(query);
    if (!isQueueRunning()) {
      startQueue();
    }
    return query;
  }

  public Query enqueue(Query query) {
    return enqueue(query, Query.Priority.NORMAL);
  }

  public abstract Query.QueryResult run(Query query);

  public boolean isQueueEmpty() {
    return queue.values().stream().allMatch(List::isEmpty);
  }

  protected abstract void startQueue();
}
