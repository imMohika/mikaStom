package ir.mohika.mikastom.module.modules.permission.events;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class PermissionUpdateEvent implements PlayerEvent {
  private final Player player;
  private final String permission;

  public PermissionUpdateEvent(@NotNull Player player, @NotNull String permission) {
    this.player = player;
    this.permission = permission;
  }
}
