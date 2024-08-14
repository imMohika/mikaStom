package ir.mohika.mikastom.listeners;

import ir.mohika.mikastom.api.minetools.MineToolsApi;
import ir.mohika.mikastom.api.minetools.MineToolsProfile;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PlayerSkinListener implements EventListener<PlayerSkinInitEvent> {

  private static final Logger logger = LoggerFactory.getLogger(PlayerSkinListener.class);

  @Override
  public @NotNull Class<PlayerSkinInitEvent> eventType() {
    return PlayerSkinInitEvent.class;
  }

  @Override
  public @NotNull Result run(@NotNull PlayerSkinInitEvent event) {
    Player player = event.getPlayer();

    Optional<MineToolsProfile> playerProfile = MineToolsApi.getPlayerProfile(player.getUsername());
    if (playerProfile.isEmpty()) {
      logger.info("Player profile is empty");
      return Result.INVALID;
    }

    MineToolsProfile.Raw.Property textures = playerProfile.get().getRaw().getProperties()[0];

    PlayerSkin skin = new PlayerSkin(textures.getValue(), textures.getSignature());

    logger.info(skin.toString());
    event.setSkin(skin);

    return Result.SUCCESS;
  }
}
