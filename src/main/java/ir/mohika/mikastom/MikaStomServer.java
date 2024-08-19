package ir.mohika.mikastom;

import dev.emortal.api.modules.LoadableModule;
import dev.emortal.api.modules.Module;
import dev.emortal.api.modules.ModuleManager;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class MikaStomServer {
  private final List<Minigame> minigames = new ArrayList<>();

  private final SocketAddress address;
  @Getter private final @NotNull MinecraftServer server;
  @Getter private final @NotNull ModuleManager moduleManager;

  private MikaStomServer(@NotNull Builder builder) {
    this.address = builder.address;
    this.server = MinecraftServer.init();
    this.moduleManager = builder.moduleManagerBuilder.build();
    MinecraftServer.getSchedulerManager().buildShutdownTask(this.moduleManager::onUnload);
    MinecraftServer.getConnectionManager().setPlayerProvider(MinigamePlayer::new);
  }

  public void start() {
    if (MikaStom.getServer().getMinigameByName("hub").isEmpty()) {
      Log.getLogger().error("Could not find minigame with name 'hub'. Stopping server...");
      MinecraftServer.stopCleanly();
      return;
    }

    this.server.start(address);
    this.moduleManager.onReady();
    Log.getLogger().info("Listening on {}", address);
  }

  public void addMinigame(@NotNull Minigame minigame) {
    minigame.setServer(this);
    minigames.add(minigame);
    Log.getLogger().info("Added {} minigame", minigame.getName());
  }

  public @NotNull Optional<Minigame> getMinigameByName(@NotNull String name) {
    return minigames.stream()
        .filter(m -> m.getName().equalsIgnoreCase(name))
        .findFirst()
        .or(
            () -> {
              Log.getLogger().debug("Minigame {} not found", name);
              return Optional.empty();
            });
  }

  public static @NotNull List<MinigamePlayer> getOnlinePlayers() {
    return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
        .filter(player -> player.getInstance() != null)
        .map(MinigamePlayer.class::cast)
        .toList();
  }

  public static @NotNull Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private SocketAddress address;
    private final ModuleManager.Builder moduleManagerBuilder = ModuleManager.builder();

    public @NotNull Builder address(SocketAddress address) {
      this.address = address;
      return this;
    }

    public @NotNull Builder module(
        @NotNull Class<? extends Module> clazz, @NotNull LoadableModule.Creator moduleCreator) {
      this.moduleManagerBuilder.module(clazz, moduleCreator);
      return this;
    }

    public @NotNull MikaStomServer build() {
      if (address == null) {
        throw new IllegalStateException("Address must be set");
      }
      return new MikaStomServer(this);
    }
  }
}
