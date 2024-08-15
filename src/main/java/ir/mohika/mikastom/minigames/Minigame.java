package ir.mohika.mikastom.minigames;

import ir.mohika.mikastom.MikaStomServer;
import ir.mohika.mikastom.commands.BaseRootCommand;
import ir.mohika.mikastom.minigames.events.MinigamePlayerEvent;
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

import java.util.List;

public abstract class Minigame {
  public @NotNull EventNode<Event> eventNode;

  @Getter private List<InstanceContainer> instances;

  @Nullable @Getter @Setter private MikaStomServer server;

  protected Minigame(@Nullable MikaStomServer server) {
    this.server = server;

    instances = initInstances();

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

  protected abstract List<InstanceContainer> initInstances();

  protected abstract Command initCommands(Command rootCommand);

  protected void registerCommands() {
    MinecraftServer.getCommandManager()
        .register(this.initCommands(new BaseRootCommand(this.getName())));
  }
}
