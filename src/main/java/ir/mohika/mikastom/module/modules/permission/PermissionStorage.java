package ir.mohika.mikastom.module.modules.permission;

import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.module.modules.permission.events.PermissionUpdateEvent;
import ir.mohika.mikastom.module.modules.storage.Query;
import ir.mohika.mikastom.module.modules.storage.Storage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;

public class PermissionStorage {
  private final Storage storage;

  public PermissionStorage(Storage storage) throws SQLException {
    this.storage = storage;
    checkTables();
  }

  private void checkTables() throws SQLException {
    Optional<ResultSet> result =
        storage
            .run(
                Query.query(
                    """
CREATE TABLE IF NOT EXISTS players (
id INTEGER PRIMARY KEY,
username VARCHAR(16) not null,
permission VARCHAR(300) not null
);"""))
            .result();
    if (result.isPresent()) {
      result.get().close();
    }
  }

  public List<Permission> getPermissions(Player player) {
    List<Permission> permissions = new ArrayList<>();

    Optional<ResultSet> resultSet =
        storage
            .run(
                Query.query("SELECT permission FROM players WHERE username = ?")
                    .setStatementValue(1, player.getUsername()))
            .result();
    if (resultSet.isPresent()) {
      ResultSet result = resultSet.get();
      try {
        while (result.next()) {
          String permission = result.getString("permission");
          permissions.add(new Permission(permission));
        }
      } catch (SQLException e) {
        Log.getLogger().error("Error when getting user permissions", e);
      }
    }
    return permissions;
  }

  public void addPermission(Player player, String permission) {
    storage
        .enqueue(
            Query.query("INSERT into players (username,permission) values (?,?)")
                .setStatementValue(1, player.getUsername())
                .setStatementValue(2, permission.toLowerCase()))
        .onComplete(
            resultSet ->
                MinecraftServer.getGlobalEventHandler()
                    .call(new PermissionUpdateEvent(player, permission.toLowerCase())));
  }
}
