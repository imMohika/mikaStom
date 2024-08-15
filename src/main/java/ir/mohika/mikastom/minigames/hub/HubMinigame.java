package ir.mohika.mikastom.minigames.hub;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.hub.listeners.HubPlayerJoin;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;

public class HubMinigame extends Minigame {
  @Getter private static HubMinigame instance;

  public HubMinigame(MikaStomServer server) {
    super(server);

    eventNode.addListener(new HubPlayerJoin());
  }

  @Override
  public String getName() {
    return "hub";
  }

  @Override
  protected List<InstanceContainer> initInstances() {
    List<InstanceContainer> instances = new ArrayList<>();
    // just create one instance for now
    InstanceContainer instanceContainer =
        MinecraftServer.getInstanceManager().createInstanceContainer();

    // Let there be land
    instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

    // Let there be light
    instanceContainer.setChunkSupplier(LightingChunk::new);

    // Always sunshine
    instanceContainer.setTimeRate(0);

    // Spawn pos
    instanceContainer.setTag(Tags.getSpawnPos(), new Pos(0, 60, 0));

    instances.add(instanceContainer);
    return instances;
  }

  @Override
  protected Command initCommands(Command rootCommand) {
    rootCommand.setDefaultExecutor(
        ((sender, context) -> {
          MinigamePlayer player = (MinigamePlayer) sender;
          player.sendToHub();
        }));
    return rootCommand;
  }
}
