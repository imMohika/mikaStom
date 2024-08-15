package ir.mohika.mikastom.commands;

import ir.mohika.mikastom.constants.Permissions;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

import java.util.Arrays;

public class GameModeCommand extends Command {
  public GameModeCommand() {
    super("gamemode", "gm");

    setCondition(((sender, commandString) -> sender.hasPermission(Permissions.getOperator())));

    setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /gamemode <gamemode>"));

    ArgumentEnum<GameMode> gameModeArg = ArgumentType.Enum("gamemode", GameMode.class);
    gameModeArg.setFormat(ArgumentEnum.Format.LOWER_CASED);
    gameModeArg.setCallback(
        (sender, exception) ->
            sender.sendMessage(
                "Valid game modes are: " + Arrays.stream(GameMode.values()).map(Enum::name)));

    addSyntax(
        (sender, context) -> {
          if (sender instanceof Player player) {
            final GameMode gameMode = context.get(gameModeArg);
            player.setGameMode(gameMode);
            return;
          }

          sender.sendMessage("Only players can use this command.");
        },
        gameModeArg);
  }
}
