package ir.mohika.mikastom.minigames.events;

import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public interface MinigamePlayerEvent extends PlayerEvent {
  @NotNull
  String getMinigame();

  @NotNull
  MinigamePlayer getPlayer();
}
