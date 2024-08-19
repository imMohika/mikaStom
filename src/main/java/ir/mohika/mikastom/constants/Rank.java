package ir.mohika.mikastom.constants;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;
import org.jetbrains.annotations.NotNull;

@Getter
public enum Rank {
  OWNER("owner", "<aqua>OWNER", true),
  DEVELOPER("dev", "<yellow>DEV", true),
  ADMIN("admin", "<red>ADMIN", true),
  BUILDER("builder", "<purple>Builder", true),
  DEFAULT("default", "<gray>Member", false),
  ;
  private final String name;
  private final Component prefix;
  private final boolean isStaff;
  private final @NotNull Permission permission;

  Rank(String name, Component prefix, boolean isStaff) {
    this.name = name;
    this.prefix = prefix;
    this.isStaff = isStaff;
    this.permission = new Permission("group." + name);
  }

  Rank(String name, @NotNull String prefix, boolean isStaff) {
    this(name, MiniMessage.miniMessage().deserialize(prefix), isStaff);
  }

  public boolean isEqualOrHigher(@NotNull Rank rank) {
    return this.ordinal() <= rank.ordinal();
  }

  // todo: make this better
  public static @NotNull Rank getPlayerRank(@NotNull Player player) {
    for (Rank rank : Rank.values()) {
      if (player.hasPermission(rank.permission)) {
        return rank;
      }
    }

    player.addPermission(Rank.DEFAULT.permission);
    return Rank.DEFAULT;
  }
}
