package ir.mohika.mikastom.command.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import ir.mohika.mikastom.MikaStomServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command(name = "list", aliases = "online")
public class ListCommand {
  @Execute
  public void execute(@Context @NotNull CommandSender sender) {
    sender.sendMessage(
        Component.join(
            JoinConfiguration.commas(true),
            MikaStomServer.getOnlinePlayers().stream().map(Player::getName).toList()));
  }
}
