package ir.mohika.mikastom.command.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

@Command(name = "up")
public class UpCommand {
  @Execute
  public void execute(@Context Player player, @Arg int number) {
    Pos currPos = player.getPosition();
    Pos newPos = currPos.add(0, number, 0);
    Pos blockPos = currPos.add(0, number - 1, 0);

    Instance instance = player.getInstance();
    Block block = instance.getBlock(blockPos);
    if (!block.isSolid()) {
      instance.setBlock(blockPos, Block.GLASS);
    }
    player.teleport(newPos);
  }
}
