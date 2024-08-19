package ir.mohika.mikastom.module.modules.chat;

import dev.emortal.api.modules.annotation.ModuleData;
import dev.emortal.api.modules.env.ModuleEnvironment;
import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.module.MikaStomModule;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

@ModuleData(name = "chat")
public class ChatModule extends MikaStomModule {
  private ChatListener listener;

  public ChatModule(@NotNull ModuleEnvironment environment) {
    super(environment);
  }

  @Override
  public boolean onLoad() {
    this.listener = new ChatListener();
    this.eventNode.addListener(listener);
    return true;
  }

  @Override
  public void onUnload() {
    if (listener != null) {
      this.eventNode.removeListener(listener);
    }
  }

  private static class ChatListener implements EventListener<PlayerChatEvent> {
    @Override
    public @NotNull Class<PlayerChatEvent> eventType() {
      return PlayerChatEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChatEvent event) {
      event.setCancelled(true);

      MinigamePlayer player = (MinigamePlayer) event.getPlayer();
      ChatMessage message =
          ChatMessage.builder()
              .message(event.getMessage())
              .senderId(player.getUuid())
              .senderUsername(player.getUsername())
              .channel(player.getCurrentMinigame())
              .build();

      List<MinigamePlayer> players = MikaStomServer.getOnlinePlayers();
      players.stream()
          .filter(p -> message.channel.equalsIgnoreCase(player.getCurrentMinigame()))
          .forEach(receiver -> receiver.sendMessage(message.parsed()));

      Log.getLogger()
          .info("CHAT [{}] {}: {}", message.channel, message.senderUsername, message.message);
      return Result.SUCCESS;
    }
  }

  @Builder
  public record ChatMessage(String message, String channel, UUID senderId, String senderUsername) {
    public @NotNull Component parsed() {
      return MiniMessage.miniMessage()
          .deserialize(
              "<dark_gray>[<gray><channel><dark_gray>] <white><username> <dark_gray>» <gray><message>",
              Placeholder.unparsed("username", senderUsername),
              Placeholder.unparsed("channel", channel),
              Placeholder.unparsed("message", message));
    }
  }
}
