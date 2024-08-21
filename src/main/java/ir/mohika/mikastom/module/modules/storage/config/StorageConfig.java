package ir.mohika.mikastom.module.modules.storage.config;

import de.exlll.configlib.Configuration;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@NoArgsConstructor
@Getter
public class StorageConfig {
  private StorageType storageType = StorageType.SQLITE;
  private RemoteConfig remote =
      new RemoteConfig(
          "localhost",
          "3306",
          "mikastom",
          "root",
          "password",
          new PoolSettings(10, 10, 1800000, 0, 5000));

  public record RemoteConfig(
      String host,
      String port,
      String database,
      String username,
      String password,
      PoolSettings poolSettings) {}

  public record PoolSettings(
      int minPoolSize, int minIdle, int maxLifetime, int keepaliveTime, int connectionTimeout) {}

  public enum StorageType {
    SQLITE
  }
}
