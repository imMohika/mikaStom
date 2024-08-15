package ir.mohika.mikastom.minigames.player;

import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.events.MinigamePlayerJoinEvent;
import ir.mohika.mikastom.minigames.events.MinigamePlayerLeaveEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MinigamePlayer extends Player {
  public MinigamePlayer(
      @NotNull UUID uuid, @NotNull String username, @NotNull PlayerConnection playerConnection) {
    super(uuid, username, playerConnection);
  }

  public String getCurrentMinigame() {
    return this.getTag(Tags.getMinigame());
  }

  public void setCurrentMinigame(String minigame) {
    this.setTag(Tags.getMinigame(), minigame);
  }

  public void sendToHub() {
    if (this.getCurrentMinigame().equalsIgnoreCase("hub")) {
      this.teleport(this.getInstance().getTag(Tags.getSpawnPos()));
      return;
    }

    MinecraftServer.getGlobalEventHandler()
        .call(new MinigamePlayerLeaveEvent(this.getCurrentMinigame(), this));
    MinecraftServer.getGlobalEventHandler().call(new MinigamePlayerJoinEvent("hub", this));
  }
}
