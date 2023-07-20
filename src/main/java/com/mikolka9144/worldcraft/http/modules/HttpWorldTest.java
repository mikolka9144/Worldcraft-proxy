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
    public World getWorld(int worldId, World world) {
        world.getChunks().setBlock(5,55,2, BlockData.BlockType.BRICK_BLOCK_ID);
        world.getChunks().setBlock(3,55,9, BlockData.BlockType.DIAMOND_BLOCK_ID);
        world.getChunks().setBlock(0,55,0, BlockData.BlockType.GLASS_ID);
        world.getChunks().setBlock(15,55,15, BlockData.BlockType.GOLD_BLOCK_ID);
        return world;
    }
}
