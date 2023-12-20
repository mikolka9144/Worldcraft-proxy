package com.mikolka9144.worldcraft.hacked.modules.hackoring;

import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("testWorld")
public class HttpWorldTest implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        World world = request.getWorld();
        world.getChunks().at(5,55,2).setBlockType(BlockType.BRICK_BLOCK_ID);
        world.getChunks().at(3,55,9).setBlockType(BlockType.DIAMOND_BLOCK_ID);
        world.getChunks().at(0,55,0).setBlockType(BlockType.GLASS_ID);
        world.getChunks().at(15,55,15).setBlockType(BlockType.GOLD_BLOCK_ID);
    }
}
