package ir.mohika.mikastom.module.modules.permission.commands;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import ir.mohika.mikastom.module.modules.permission.PermissionStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;

@Command(name = "permissions", aliases = "perm")
public class PermissionsCommand {
  private final PermissionStorage storage;

  public PermissionsCommand(PermissionStorage storage) {
    this.storage = storage;
  }

  @Execute(name = "add")
  public void addPermission(
      @Context CommandSender sender, @Arg Player target, @Arg String permission) {
    storage.addPermission(target, permission);
  }

  @Execute(name = "list")
  public Component listPermissions(@Context CommandSender sender, @Arg Player target) {
    return Component.join(
        JoinConfiguration.commas(true),
        target.getAllPermissions().stream()
            .map(p -> Component.text(p.getPermissionName()))
            .toList());
  }
}
