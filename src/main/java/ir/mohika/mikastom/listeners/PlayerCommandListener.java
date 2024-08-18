package ir.mohika.mikastom.listeners;

import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.utils.Log;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerCommandEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandListener implements EventListener<PlayerCommandEvent> {
  @Override
  public @NotNull Class<PlayerCommandEvent> eventType() {
    return PlayerCommandEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull PlayerCommandEvent event) {
    MinigamePlayer player = (MinigamePlayer) event.getPlayer();
    String command = event.getCommand();

    Log.getLogger()
        .info("COMMAND [{}] {}: /{}", player.getCurrentMinigame(), player.getUsername(), command);
    return Result.SUCCESS;
  }
}
