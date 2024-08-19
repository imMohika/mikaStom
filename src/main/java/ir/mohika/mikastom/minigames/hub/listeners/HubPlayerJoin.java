package ir.mohika.mikastom.minigames.hub.listeners;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.events.MinigamePlayerJoinEvent;
import ir.mohika.mikastom.minigames.hub.HubMinigame;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
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
    MinigamePlayer player = event.getPlayer();
    InstanceContainer instanceContainer = HubMinigame.getInstance().getInstances().getFirst();
    Pos spawnPos = instanceContainer.getTag(Tags.getSpawnPos());

    player.setRespawnPoint(spawnPos);
    player.setCurrentMinigame("hub");
    player.setInstance(instanceContainer, spawnPos);
    player.setGameMode(GameMode.ADVENTURE);

    Component joinMessage =
        MiniMessage.miniMessage()
            .deserialize(
                "<dark_gray>[<green>+<dark_gray>] <white><player>",
                Placeholder.component("player", player.getName()));
    MikaStomServer.getOnlinePlayers().stream()
        .filter(p -> p.getCurrentMinigame() != null)
        .filter(p -> p.getCurrentMinigame().equalsIgnoreCase("hub"))
        .forEach(receiver -> receiver.sendMessage(joinMessage));

    return Result.SUCCESS;
  }
}
