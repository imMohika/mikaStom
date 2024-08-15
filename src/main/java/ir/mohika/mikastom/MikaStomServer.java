package ir.mohika.mikastom;

import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.utils.Log;
import lombok.NoArgsConstructor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.*;

@NoArgsConstructor
public class MikaStomServer {
  private final List<Minigame> minigames = new ArrayList<>();

  public void addMinigame(Minigame minigame) {
    minigame.setServer(this);
    minigames.add(minigame);
    Log.getLogger().info("Added {} minigame", minigame.getName());
  }

  public Optional<Minigame> getMinigameByName(@NotNull String name) {
    return minigames.stream()
        .filter(m -> m.getName().equalsIgnoreCase(name))
        .findFirst()
        .or(
            () -> {
              Log.getLogger().debug("Minigame {} not found", name);
              return Optional.empty();
            });
  }

  public static List<MinigamePlayer> getOnlinePlayers() {
    return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
        .filter(player -> player.getInstance() != null)
        .map(MinigamePlayer.class::cast)
        .toList();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final EventNode<Event> globalEvents = EventNode.all("globalListeners");

    private SocketAddress address;

    public Builder globalEvent(EventListener<?> listener) {
      globalEvents.addListener(listener);
      return this;
    }

    public Builder address(SocketAddress address) {
      this.address = address;
      return this;
    }

    public void setupServer() {
      if (address == null) {
        throw new IllegalStateException("Address must be set");
      }

      MinecraftServer minecraftServer = MinecraftServer.init();
      MinecraftServer.getGlobalEventHandler().addChild(globalEvents);

      MinecraftServer.getConnectionManager().setPlayerProvider(MinigamePlayer::new);

      minecraftServer.start(address);
      Log.getLogger().info("Listening on {}", address);
    }

    public MikaStomServer buildAndStart() {
      this.setupServer();
      return new MikaStomServer();
    }
  }
}
