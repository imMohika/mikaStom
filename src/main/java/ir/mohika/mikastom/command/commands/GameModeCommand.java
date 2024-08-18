package ir.mohika.mikastom.command.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

@Command(name = "gamemode", aliases = "gm")
public class GameModeCommand {
  @Execute
  public void execute(@Context Player player, @Arg GameMode gameMode) {
    player.setGameMode(gameMode);
  }
}
