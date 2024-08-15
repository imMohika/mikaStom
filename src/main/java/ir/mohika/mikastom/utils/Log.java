package ir.mohika.mikastom.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Log {
  private static final Map<String, Logger> loggerCache = new ConcurrentHashMap<>();

  public static Logger getLogger() {
    String className = Thread.currentThread().getStackTrace()[2].getClassName();
    return loggerCache.computeIfAbsent(className, LoggerFactory::getLogger);
  }
}
