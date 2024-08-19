package ir.mohika.mikastom.minigames.hub.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import ir.mohika.mikastom.MikaStom;
import ir.mohika.mikastom.minigames.Minigame;
import java.util.Optional;

import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

@Command(name = "hub")
public class HubCommands {

  @Execute
  void execute(@Context Player player) {
    ((MinigamePlayer) player).sendToHub();
  }

  @Execute(name = "save")
  @NotNull
  Component executeSave(@Context CommandSender sender) {
    Optional<Minigame> hubOptional = MikaStom.getServer().getMinigameByName("hub");
    if (hubOptional.isEmpty()) {
      return Component.text("Minigame not found");
    }
    Minigame minigame = hubOptional.get();
    minigame.getInstances().forEach(Instance::saveChunksToStorage);
    return Component.text("Saved successfully");
  }
}
