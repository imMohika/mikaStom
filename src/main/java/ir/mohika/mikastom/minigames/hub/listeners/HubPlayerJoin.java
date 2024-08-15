package ir.mohika.mikastom.minigames.hub.listeners;

import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.events.MinigamePlayerJoinEvent;
import ir.mohika.mikastom.minigames.hub.HubMinigame;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventListener;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

public class HubPlayerJoin implements EventListener<MinigamePlayerJoinEvent> {

  @Override
  public @NotNull Class<MinigamePlayerJoinEvent> eventType() {
    return MinigamePlayerJoinEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull MinigamePlayerJoinEvent event) {
    InstanceContainer instanceContainer = HubMinigame.getInstance().getInstances().getFirst();
    Pos spawnPos = instanceContainer.getTag(Tags.getSpawnPos());
    MinigamePlayer player = event.getPlayer();
    player.setInstance(instanceContainer, spawnPos);
    return Result.SUCCESS;
  }
}
