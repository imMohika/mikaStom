package ir.mohika.mikastom.commands;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.constants.Permissions;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class StopCommand extends Command {
  public StopCommand() {
    super("stop");

    setCondition(((sender, commandString) -> sender.hasPermission(Permissions.getOperator())));

    setDefaultExecutor(
        (sender, context) ->
            MinecraftServer.getSchedulerManager()
                .scheduleEndOfTick(
                    () -> {
                      MikaStomServer.getOnlinePlayers()
                          .forEach(player -> player.kick("Stopping server"));
                      MinecraftServer.stopCleanly();
                      System.exit(0);
                    }));
  }
}
