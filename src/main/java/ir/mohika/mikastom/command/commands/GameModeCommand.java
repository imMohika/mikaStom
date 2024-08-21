package ir.mohika.mikastom.command.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command(name = "gamemode", aliases = "gm")
@Permission("commands.gamemode")
public class GameModeCommand {
  @Execute
  public void execute(@Context @NotNull Player player, @Arg GameMode gameMode) {
    player.setGameMode(gameMode);
  }
}
