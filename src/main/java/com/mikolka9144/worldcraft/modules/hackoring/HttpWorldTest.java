package com.mikolka9144.worldcraft.modules.hackoring;

import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.common.api.packet.enums.BlockType;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("testWorld")
public class HttpWorldTest implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        World world = request.getWorld();
        world.getChunks().setBlockType(5,55,2, BlockType.BRICK_BLOCK_ID);
        world.getChunks().setBlockType(3,55,9, BlockType.DIAMOND_BLOCK_ID);
        world.getChunks().setBlockType(0,55,0, BlockType.GLASS_ID);
        world.getChunks().setBlockType(15,55,15, BlockType.GOLD_BLOCK_ID);
    }
}
