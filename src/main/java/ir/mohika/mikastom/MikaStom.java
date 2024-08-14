package ir.mohika.mikastom;

import ir.mohika.mikastom.listeners.PlayerJoinListener;
import ir.mohika.mikastom.listeners.PlayerSkinListener;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class MikaStom {
  public static void main(String[] args) {
    MinecraftServer minecraftServer = MinecraftServer.init();
    InstanceManager instanceManager = MinecraftServer.getInstanceManager();
    InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

    // Let there be land
    instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

    // Let there be light
    instanceContainer.setChunkSupplier(LightingChunk::new);

    GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
    globalEventHandler.addListener(new PlayerJoinListener(instanceContainer));
    globalEventHandler.addListener(new PlayerSkinListener());

    // Start the server on port 25565
    minecraftServer.start("0.0.0.0", 25565);
  }
}
