package ir.mohika.mikastom.config;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import lombok.Getter;

import java.nio.file.Path;

public class ConfigManager {
  @Getter private static ServerConfig serverConfig;

  public ConfigManager() {
    YamlConfigurationProperties properties = YamlConfigurationProperties.newBuilder().build();
    YamlConfigurationStore<ServerConfig> serverConfigStore =
        new YamlConfigurationStore<>(ServerConfig.class, properties);
    Path serverConfigPath = Path.of("./mikastom.yaml");

    if (!serverConfigPath.toFile().exists()) {
      serverConfigStore.save(new ServerConfig(), serverConfigPath);
      serverConfig = serverConfigStore.load(serverConfigPath);
    } else {
      serverConfig = serverConfigStore.update(serverConfigPath);
    }
  }
}
