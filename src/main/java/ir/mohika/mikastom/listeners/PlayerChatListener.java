package ir.mohika.mikastom.listeners;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.utils.Log;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerChatListener implements EventListener<PlayerChatEvent> {
  @Override
  public @NotNull Class<PlayerChatEvent> eventType() {
    return PlayerChatEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull PlayerChatEvent event) {
    event.setCancelled(true);
    MinigamePlayer player = (MinigamePlayer) event.getPlayer();
    String message = event.getMessage();
    Component formattedMessage =
        MiniMessage.miniMessage()
            .deserialize(
                "<dark_gray>[<minigame><dark_gray>] <white><player> <dark_gray>» <gray><message>",
                Placeholder.component("player", player.getName()),
                Placeholder.unparsed("minigame", player.getCurrentMinigame()),
                Placeholder.unparsed("message", message));

    List<MinigamePlayer> players = MikaStomServer.getOnlinePlayers();

    players.stream()
        .filter(p -> p.getCurrentMinigame().equalsIgnoreCase(player.getCurrentMinigame()))
        .forEach(receiver -> receiver.sendMessage(formattedMessage));
    Log.getLogger()
        .info("CHAT [{}] {}: {}", player.getCurrentMinigame(), player.getUsername(), message);
    return Result.SUCCESS;
  }
}
