package ir.mohika.mikastom.minigames.hub;

import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.constants.Tags;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.minigames.Minigame;
import ir.mohika.mikastom.minigames.hub.commands.HubCommands;
import ir.mohika.mikastom.minigames.hub.listeners.HubPlayerJoin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import net.hollowcube.polar.PolarLoader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import org.jetbrains.annotations.NotNull;

public class HubMinigame extends Minigame {
  @Getter private static HubMinigame instance;

  public HubMinigame(MikaStomServer server) {
    super(server);
    instance = this;

    eventNode.addListener(new HubPlayerJoin());

    LiteMinestomFactory.builder().commands(new HubCommands()).build();
  }

  @Override
  public @NotNull String getName() {
    return "hub";
  }

  @Override
  protected @NotNull List<InstanceContainer> initInstances() throws IOException {
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
}
