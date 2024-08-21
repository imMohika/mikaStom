package ir.mohika.mikastom.module.modules.storage;

import dev.emortal.api.modules.annotation.ModuleData;
import dev.emortal.api.modules.env.ModuleEnvironment;
import ir.mohika.mikastom.config.ConfigManager;
import ir.mohika.mikastom.module.MikaStomModule;
import ir.mohika.mikastom.module.modules.storage.config.StorageConfig;
import ir.mohika.mikastom.module.modules.storage.sqlite.SQLiteStorage;
import java.nio.file.Path;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

@Getter
@ModuleData(name = "storage")
public class StorageModule extends MikaStomModule {

  private StorageConfig config;
  private Storage storage;

  public StorageModule(@NotNull ModuleEnvironment environment) {
    super(environment);
  }

  @Override
  public boolean onLoad() {
    config = ConfigManager.load("storage", Path.of("config", "storage.yml"), StorageConfig.class);
    if (config.getStorageType() == StorageConfig.StorageType.SQLITE) {
      storage = new SQLiteStorage(Path.of("storage.db"));
    }

    storage.connect();

    MinecraftServer.getSchedulerManager().buildShutdownTask(() -> storage.scheduleShutdown());
    return true;
  }

  @Override
  public void onUnload() {}
}
