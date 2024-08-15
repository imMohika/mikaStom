package ir.mohika.mikastom.minigames.events;

import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import org.jetbrains.annotations.NotNull;

public class MinigamePlayerLeaveEvent implements MinigamePlayerEvent {
  private final String minigame;
  private final MinigamePlayer player;

  public MinigamePlayerLeaveEvent(String minigame, MinigamePlayer player) {
    this.minigame = minigame;
    this.player = player;

    player.setCurrentMinigame("null");
  }

  @Override
  public @NotNull String getMinigame() {
    return minigame;
  }

  @Override
  public @NotNull MinigamePlayer getPlayer() {
    return player;
  }
}
