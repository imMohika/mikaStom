package ir.mohika.mikastom.module.modules.core.listeners;

import ir.mohika.mikastom.MikaStom;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

public class PlayerConfigurationListener implements EventListener<AsyncPlayerConfigurationEvent> {
  @Override
  public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
    return AsyncPlayerConfigurationEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
    MinigamePlayer player = (MinigamePlayer) event.getPlayer();
    MikaStom.getServer()
        .getMinigameByName("hub")
        .ifPresentOrElse(
            hub -> {
              InstanceContainer hubInstance = hub.getInstances().getFirst();
              event.setSpawningInstance(hubInstance);
              Pos respawnPoint = hubInstance.getTag(Tags.getSpawnPos());
              player.setRespawnPoint(respawnPoint);
              player.setCurrentMinigame("hub");
              Log.getLogger().info("{} Joined", player.getUsername());
            },
            () -> {
              player.kick("Failed to connect to hub");
              Log.getLogger().error("Couldn't find a minigame named 'hub'");
            });
    return Result.SUCCESS;
  }
}
