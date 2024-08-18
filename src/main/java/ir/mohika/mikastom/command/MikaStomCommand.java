package ir.mohika.mikastom.command;

import ir.mohika.mikastom.constants.Rank;
import ir.mohika.mikastom.minigames.player.MinigamePlayer;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.CommandSender;

@Getter
public abstract class MikaStomCommand {
  private final CommandInfo info;
  private final String name;
  private final MinestomCommand command;

  protected MikaStomCommand() {
    this.info = this.getClass().getAnnotation(CommandInfo.class);
    this.name = this.info.name();
    this.command = new MinestomCommand(this);
  }

  public abstract void execute(MinestomCommand command);

  public boolean checkPermission(CommandSender sender) {
    MinigamePlayer player = (MinigamePlayer) sender;

    boolean pass = false;

    if (!info.permission().isEmpty()) {
      pass = player.hasPermission(info.permission());
    } else if (info.rank() != null) {
      pass = Rank.getPlayerRank(player).isEqualOrHigher(info.rank());
    }

    if (!pass) {
      player.sendMessage(
          MiniMessage.miniMessage()
              .deserialize("<red>You do not have permission to use this command."));
    }

    return pass;
  }
}
