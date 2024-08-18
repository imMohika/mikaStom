package ir.mohika.mikastom.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;

public class MinestomCommand extends Command {
  public MinestomCommand(MikaStomCommand command) {
    super(command.getName(), command.getInfo().aliases().split(" "));

    setDefaultExecutor(
        (sender, context) -> {
          if (command.getInfo().usage().isEmpty()) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong Usage"));
          } else {
            sender.sendMessage(
                MiniMessage.miniMessage()
                    .deserialize("<red>Usage<dark_gray>:<reset> " + command.getInfo().usage()));
          }
        });

    setCondition(
        (commandSender, string) -> {
          if (commandSender instanceof ConsoleSender) {
            return command.getInfo().allowConsole();
          }

          return command.checkPermission(commandSender);
        });

    command.execute(this);
  }
}
