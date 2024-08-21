package ir.mohika.mikastom.config;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurationStore;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
  private static final Map<String, YamlConfigurationStore<?>> stores = new HashMap<>();
  private static final YamlConfigurationProperties properties =
      YamlConfigurationProperties.newBuilder().build();

  public static <T> T load(String name, Path configPath, Class<T> clazz) {
    YamlConfigurationStore<T> store = new YamlConfigurationStore<>(clazz, properties);

    if (!configPath.toFile().exists()) {
      T configInstance = createDefaultInstance(clazz);
      store.save(configInstance, configPath);
      T config = store.load(configPath);
      stores.put(name, store);
      return config;
    }

    T config = store.update(configPath);
    stores.put(name, store);
    return config;
  }

  public static <T> YamlConfigurationStore<T> getStore(String fileName) {
    @SuppressWarnings("unchecked")
    YamlConfigurationStore<T> store = (YamlConfigurationStore<T>) stores.get(fileName);
    return store;
  }

  private static <T> T createDefaultInstance(Class<T> configClass) {
    try {
      return configClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(
          "Unable to create default instance of " + configClass.getName(), e);
    }
  }
}
