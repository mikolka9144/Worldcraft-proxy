package com.mikolka9144.worldcraft.http.modules;

import com.mikolka9144.worldcraft.common.level.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("testWorld")
public class HttpWorldTest implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public byte[] getWorld(int worldId, byte[] worldBin) {
        World world = World.fromTarGzBin(worldBin);
        world.getRegion(reg -> {
            reg.setBlock(5,50,2, BlockData.BlockType.BRICK_BLOCK_ID);
            reg.setBlock(3,50,9, BlockData.BlockType.DIAMOND_BLOCK_ID);
            reg.setBlock(0,50,0, BlockData.BlockType.GLASS_ID);
            reg.setBlock(15,50,15, BlockData.BlockType.GOLD_BLOCK_ID);
        });
        return world.toTarGzBin();
    }
}
