package ir.mohika.mikastom;

import ir.mohika.mikastom.commands.GameModeCommand;
import ir.mohika.mikastom.commands.OnlineListCommand;
import ir.mohika.mikastom.commands.StopCommand;
import ir.mohika.mikastom.config.ConfigManager;
import ir.mohika.mikastom.listeners.PlayerChatListener;
import ir.mohika.mikastom.listeners.PlayerJoinListener;
import ir.mohika.mikastom.listeners.PlayerSkinListener;
import ir.mohika.mikastom.minigames.hub.HubMinigame;
import lombok.Getter;
import net.minestom.server.MinecraftServer;

public class MikaStom {
  @Getter private static MikaStomServer server;

  public static void main(String[] args) {
    new ConfigManager();

    server =
        MikaStomServer.builder()
            .address(ConfigManager.getServerConfig().getAddress())
            .globalEvent(new PlayerJoinListener())
            .globalEvent(new PlayerSkinListener())
            .globalEvent(new PlayerChatListener())
            .buildAndStart();
    server.addMinigame(new HubMinigame(server));

    MinecraftServer.getCommandManager().register(new StopCommand());
    MinecraftServer.getCommandManager().register(new GameModeCommand());
    MinecraftServer.getCommandManager().register(new OnlineListCommand());
  }
}
