package ir.mohika.mikastom.minigames.hub;

import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.hub.commands.HubCommands;
import ir.mohika.mikastom.minigames.hub.listeners.HubPlayerJoin;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import ir.mohika.mikastom.utils.Log;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.hollowcube.polar.PolarLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;

public class HubMinigame extends Minigame {
  @Getter private static HubMinigame instance;

  public HubMinigame(MikaStomServer server) {
    super(server);

    eventNode.addListener(new HubPlayerJoin());

    LiteMinestomFactory.builder().commands(new HubCommands()).build();
  }

  @Override
  public String getName() {
    return "hub";
  }

  @Override
  protected List<InstanceContainer> initInstances() throws IOException {
    List<InstanceContainer> instances = new ArrayList<>();
    // just create one instance for now
    InstanceContainer instanceContainer =
        MinecraftServer.getInstanceManager().createInstanceContainer();

    Log.getLogger().info("Loading Hub World");
    instanceContainer.setChunkLoader(new PolarLoader(Path.of("./hub/world/hub.polar")));
    instanceContainer.setTimeRate(0);

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
