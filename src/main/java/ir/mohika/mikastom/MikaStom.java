package ir.mohika.mikastom;

import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import ir.mohika.mikastom.command.commands.GameModeCommand;
import ir.mohika.mikastom.command.commands.ListCommand;
import ir.mohika.mikastom.command.commands.StopCommand;
import ir.mohika.mikastom.command.commands.UpCommand;
import ir.mohika.mikastom.config.ConfigManager;
import ir.mohika.mikastom.listeners.PlayerChatListener;
import ir.mohika.mikastom.listeners.PlayerCommandListener;
import ir.mohika.mikastom.listeners.PlayerJoinListener;
import ir.mohika.mikastom.listeners.PlayerSkinListener;
import ir.mohika.mikastom.minigames.hub.HubMinigame;
import lombok.Getter;

public class MikaStom {
  @Getter private static MikaStomServer server;

  public static void main(String[] args) {
    new ConfigManager();

    server =
        MikaStomServer.builder()
            .address(ConfigManager.getServerConfig().getAddress())
            .globalEvent(new PlayerJoinListener())
            .globalEvent(new PlayerSkinListener())
            .globalEvent(new PlayerCommandListener())
            .globalEvent(new PlayerChatListener())
            .buildAndStart();

    LiteMinestomFactory.builder()
        .commands(new StopCommand(), new UpCommand(), new ListCommand(), new GameModeCommand())
        .build();

    server.addMinigame(new HubMinigame(server));
  }
}
