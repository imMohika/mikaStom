package ir.mohika.mikastom.listeners;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements EventListener<AsyncPlayerConfigurationEvent> {
  private final InstanceContainer instanceContainer;

  public PlayerJoinListener(InstanceContainer instanceContainer) {
    this.instanceContainer = instanceContainer;
  }

  @Override
  public @NotNull Class eventType() {
    return AsyncPlayerConfigurationEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
    final Player player = event.getPlayer();
    event.setSpawningInstance(instanceContainer);
    player.setRespawnPoint(new Pos(0, 42, 0));
    return Result.SUCCESS;
  }
}
