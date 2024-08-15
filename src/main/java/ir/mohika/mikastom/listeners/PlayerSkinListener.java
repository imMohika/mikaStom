package ir.mohika.mikastom.listeners;

import ir.mohika.mikastom.api.minetools.MineToolsApi;
import ir.mohika.mikastom.api.minetools.MineToolsProfile;
import ir.mohika.mikastom.utils.Log;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerSkinListener implements EventListener<PlayerSkinInitEvent> {

  @Override
  public @NotNull Class<PlayerSkinInitEvent> eventType() {
    return PlayerSkinInitEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull PlayerSkinInitEvent event) {
    Player player = event.getPlayer();

    Optional<MineToolsProfile> playerProfile = MineToolsApi.getPlayerProfile(player.getUsername());
    if (playerProfile.isEmpty()) {
      Log.getLogger().info("Player profile is empty");
      return Result.INVALID;
    }

    MineToolsProfile.Raw.Property textures = playerProfile.get().getRaw().getProperties()[0];
    PlayerSkin skin = new PlayerSkin(textures.getValue(), textures.getSignature());
    event.setSkin(skin);
    return Result.SUCCESS;
  }
}
