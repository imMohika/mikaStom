package ir.mohika.mikastom.module.modules.skin;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.emortal.api.modules.annotation.ModuleData;
import dev.emortal.api.modules.env.ModuleEnvironment;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.core.web.api.minetools.MineToolsApi;
import ir.mohika.mikastom.core.web.api.minetools.MineToolsProfile;
import ir.mohika.mikastom.module.MikaStomModule;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.minestom.server.extras.MojangAuth;
import org.jetbrains.annotations.NotNull;

@ModuleData(name = "skin")
public class SkinModule extends MikaStomModule {
  PlayerSkinListener skinListener;
  private Cache<String, PlayerSkin> skinCache;

  public SkinModule(@NotNull ModuleEnvironment environment) {
    super(environment);
  }

  @Override
  public boolean onLoad() {
    if (MojangAuth.isEnabled()) {
      Log.getLogger().error("SkinModule conflicts with Mojang Auth");
      return false;
    }

    skinCache = Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1)).build();
    skinListener = new PlayerSkinListener();
    eventNode.addListener(skinListener);
    return true;
  }

  @Override
  public void onUnload() {
    eventNode.removeListener(skinListener);
    skinCache.cleanUp();
  }

  private class PlayerSkinListener implements EventListener<PlayerSkinInitEvent> {
    @Override
    public @NotNull Class<PlayerSkinInitEvent> eventType() {
      return PlayerSkinInitEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerSkinInitEvent event) {
      Player player = event.getPlayer();

      PlayerSkin cachedSkin = skinCache.getIfPresent(player.getUsername());
      if (cachedSkin != null) {
        event.setSkin(cachedSkin);
        return Result.SUCCESS;
      }

      CompletableFuture<Optional<MineToolsProfile>> playerProfile =
          MineToolsApi.getPlayerProfile(player.getUsername());

      playerProfile.thenAccept(
          profile -> {
            if (profile.isEmpty()) {
              Log.getLogger().info("Player profile is empty");
            } else {
              MineToolsProfile.Raw.Property textures = profile.get().getRaw().getProperties()[0];
              PlayerSkin skin = new PlayerSkin(textures.getValue(), textures.getSignature());
              player.setSkin(skin);
              skinCache.put(player.getUsername(), skin);
            }
          });
      return Result.SUCCESS;
    }
  }
}
