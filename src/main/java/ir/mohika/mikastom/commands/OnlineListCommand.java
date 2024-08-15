package ir.mohika.mikastom.commands;

import ir.mohika.mikastom.MikaStomServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class OnlineListCommand extends Command {
  public OnlineListCommand() {
    super("onlinelist", "online", "list");

    setDefaultExecutor(
        (sender, context) ->
            sender.sendMessage(
                Component.join(
                    JoinConfiguration.commas(true),
                    MikaStomServer.getOnlinePlayers().stream().map(Player::getName).toList())));
  }
}
