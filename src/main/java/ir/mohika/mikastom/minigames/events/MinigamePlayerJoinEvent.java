package ir.mohika.mikastom.minigames.events;

import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import org.jetbrains.annotations.NotNull;

public class MinigamePlayerJoinEvent implements MinigamePlayerEvent {
  private final String minigame;
  private final MinigamePlayer player;

  public MinigamePlayerJoinEvent(String minigame, MinigamePlayer player) {
    this.minigame = minigame;
    this.player = player;

    player.setCurrentMinigame(minigame);
  }

  @Override
  public @NotNull String getMinigame() {
    return minigame;
  }

  public @NotNull MinigamePlayer getPlayer() {
    return player;
  }
}
