package ir.mohika.mikastom.minigames;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.minigames.events.MinigamePlayerEvent;
import ir.mohika.mikastom.utils.Log;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Minigame {
  public @NotNull EventNode<Event> eventNode;

  @Getter private List<InstanceContainer> instances;

  @Nullable @Getter @Setter private MikaStomServer server;

  @Getter private final Path dataDir;

  protected Minigame(@Nullable MikaStomServer server) {
    this.server = server;
    this.dataDir = Path.of("./" + getName());

    if (!Files.exists(dataDir)) {
      try {
        Files.createDirectories(dataDir);
        Files.createDirectories(Path.of(dataDir.toString(), "world"));
        Files.createDirectories(Path.of(dataDir.toString(), "config"));
      } catch (IOException e) {
        Log.getLogger().error("Failed to create data folder for {}", getName());
        throw new RuntimeException(e);
      }
    }

    instances = new ArrayList<>();

    try {
      instances = initInstances();
    } catch (IOException e) {
      Log.getLogger().error("Failed to load instances for {}", getName());
      throw new RuntimeException(e);
    }

    eventNode =
        EventNode.event(
            getName(),
            EventFilter.ALL,
            (Event event) -> {
              if (event instanceof InstanceEvent instanceEvent) {
                return getInstances().contains(instanceEvent.getInstance());
              }

              if (event instanceof MinigamePlayerEvent minigameEvent) {
                return minigameEvent.getMinigame().equalsIgnoreCase(getName());
              }

              return false;
            });
    MinecraftServer.getGlobalEventHandler().addChild(eventNode);

    registerCommands();
  }

  public abstract String getName();

  protected abstract List<InstanceContainer> initInstances() throws IOException;

  protected abstract Command initCommands(Command rootCommand);

  protected void registerCommands() {
    //    MinecraftServer.getCommandManager()
    //        .register(this.initCommands(new BaseRootCommand(this.getName())));
  }
}
