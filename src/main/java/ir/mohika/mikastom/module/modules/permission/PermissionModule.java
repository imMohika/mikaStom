package ir.mohika.mikastom.module.modules.permission;

import dev.emortal.api.modules.annotation.Dependency;
import dev.emortal.api.modules.annotation.ModuleData;
import dev.emortal.api.modules.env.ModuleEnvironment;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.module.MikaStomModule;
import ir.mohika.mikastom.module.modules.permission.commands.PermissionsCommand;
import ir.mohika.mikastom.module.modules.permission.events.PermissionUpdateEvent;
import ir.mohika.mikastom.module.modules.storage.StorageModule;
import java.sql.SQLException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.permission.Permission;
import org.jetbrains.annotations.NotNull;

@ModuleData(name = "permission", dependencies = @Dependency(name = "storage"))
public class PermissionModule extends MikaStomModule {
  private PermissionStorage permissionStorage;
  private PlayerConfigurationListener playerConfigurationListener;
  private PermissionUpdateListener permissionUpdateListener;

  public PermissionModule(@NotNull ModuleEnvironment environment) {
    super(environment);
  }

  @Override
  public boolean onLoad() {
    StorageModule storageModule = getModule(StorageModule.class);
    try {
      permissionStorage = new PermissionStorage(storageModule.getStorage());
    } catch (SQLException e) {
      Log.getLogger().error("Failed to initialize permission storage", e);
      return false;
    }

    playerConfigurationListener = new PlayerConfigurationListener(permissionStorage);
    this.eventNode.addListener(playerConfigurationListener);

    permissionUpdateListener = new PermissionUpdateListener();
    this.eventNode.addListener(permissionUpdateListener);

    LiteMinestomFactory.builder().commands(new PermissionsCommand(permissionStorage)).build();
    return true;
  }

  @Override
  public void onUnload() {
    this.eventNode.removeListener(playerConfigurationListener);
    this.eventNode.removeListener(permissionUpdateListener);
  }

  private record PlayerConfigurationListener(PermissionStorage storage)
      implements EventListener<AsyncPlayerConfigurationEvent> {

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
      return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
      Player player = event.getPlayer();
      storage.getPermissions(player).forEach(player::addPermission);
      return Result.SUCCESS;
    }
  }

  private static class PermissionUpdateListener implements EventListener<PermissionUpdateEvent> {
    @Override
    public @NotNull Class<PermissionUpdateEvent> eventType() {
      return PermissionUpdateEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PermissionUpdateEvent event) {
      Player player =
          MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(event.getPlayer().getUuid());
      if (player == null) return Result.SUCCESS;

      player.addPermission(new Permission(event.getPermission()));
      return Result.SUCCESS;
    }
  }
}
