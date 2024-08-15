package ir.mohika.mikastom.listeners;

import ir.mohika.mikastom.MikaStom;
import ir.mohika.mikastom.config.ConfigManager;
import ir.mohika.mikastom.constants.Permissions;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.utils.Log;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerJoinListener implements EventListener<AsyncPlayerConfigurationEvent> {
  @Override
  public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
    return AsyncPlayerConfigurationEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
    MinigamePlayer player = (MinigamePlayer) event.getPlayer();

    if (MikaStom.getServer() == null) {
      player.kick("Please try again");
      return Result.EXCEPTION;
    }

    Optional<Minigame> hubMinigameOptional = MikaStom.getServer().getMinigameByName("hub");
    if (hubMinigameOptional.isEmpty()) {
      Log.getLogger().error("Could not find minigame with name 'hub'. Stopping server...");
      MinecraftServer.stopCleanly();
      return Result.EXCEPTION;
    }
    Minigame hubMinigame = hubMinigameOptional.get();

    Component joinMessage =
        MiniMessage.miniMessage()
            .deserialize(
                "<dark_gray>[<green>+<dark_gray>] <white><player>",
                Placeholder.component("player", player.getName()));
    Audiences.players().sendMessage(joinMessage);

    event.setSpawningInstance(hubMinigame.getInstances().getFirst());

    Pos respawnPoint = hubMinigame.getInstances().getFirst().getTag(Tags.getSpawnPos());
    player.setRespawnPoint(respawnPoint);

    player.setCurrentMinigame("hub");

    if (ConfigManager.getServerConfig()
        .getOperators()
        .contains(player.getUsername().toLowerCase())) {
      Log.getLogger().info("OP Player Joined {}", player.getUsername());
      player.addPermission(Permissions.getOperator());
    } else {
      Log.getLogger().info("{} Joined", player.getUsername());
    }

    player.setGameMode(GameMode.ADVENTURE);
    return Result.SUCCESS;
  }
}
