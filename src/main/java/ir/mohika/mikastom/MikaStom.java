package ir.mohika.mikastom;

import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import ir.mohika.mikastom.command.commands.GameModeCommand;
import ir.mohika.mikastom.command.commands.ListCommand;
import ir.mohika.mikastom.command.commands.StopCommand;
import ir.mohika.mikastom.command.commands.UpCommand;
import ir.mohika.mikastom.config.ConfigManager;
import ir.mohika.mikastom.config.ServerConfig;
import ir.mohika.mikastom.minigames.hub.HubMinigame;
import ir.mohika.mikastom.module.modules.chat.ChatModule;
import ir.mohika.mikastom.module.modules.core.CoreModule;
import ir.mohika.mikastom.module.modules.permission.PermissionModule;
import ir.mohika.mikastom.module.modules.skin.SkinModule;
import ir.mohika.mikastom.module.modules.storage.StorageModule;
import java.nio.file.Path;
import lombok.Getter;

public class MikaStom {
  @Getter private static MikaStomServer server;
  @Getter private static ServerConfig serverConfig;

  public static void main(String[] args) {
    serverConfig =
        ConfigManager.load("server", Path.of("config", "server.yml"), ServerConfig.class);
    server =
        MikaStomServer.builder()
            .address(serverConfig.getAddress())
            .module(CoreModule.class, CoreModule::new)
            .module(ChatModule.class, ChatModule::new)
            .module(SkinModule.class, SkinModule::new)
            .module(StorageModule.class, StorageModule::new)
            .module(PermissionModule.class, PermissionModule::new)
            .build();

    server.addMinigame(new HubMinigame(server));

    LiteMinestomFactory.builder()
        .commands(new StopCommand(), new UpCommand(), new ListCommand(), new GameModeCommand())
        .build();

    server.start();
  }
}
