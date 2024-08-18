package ir.mohika.mikastom.command.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import ir.mohika.mikastom.MikaStomServer;
import net.minestom.server.MinecraftServer;

@Command(name = "stop")
public class StopCommand {
  @Execute
  void executeStop() {
    MinecraftServer.getSchedulerManager()
        .scheduleEndOfTick(
            () -> {
              MikaStomServer.getOnlinePlayers().forEach(player -> player.kick("Stopping server"));
              MinecraftServer.stopCleanly();
              System.exit(0);
            });
  }
}
