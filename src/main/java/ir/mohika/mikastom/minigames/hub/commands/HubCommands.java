package ir.mohika.mikastom.minigames.hub.commands;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import ir.mohika.mikastom.MikaStom;
import ir.mohika.mikastom.minigames.Minigame;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.instance.Instance;

@Command(name = "hub")
public class HubCommands {

  @Execute(name = "save")
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
