package com.mikolka9144.tests;

import com.mikolka9144.worldcraft.backend.level.Level;
import com.mikolka9144.worldcraft.backend.level.Terrain;
import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;
import com.mikolka9144.worldcraft.utills.Vector3;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import org.springframework.stereotype.Component;

@Component("build")
public class WorldBuilder implements HttpDownloadInterceptor {
    @Override
    public void getWorld(WorldDownloadRequest request) {
        Level lvl = new Level();
        Terrain trn = new Terrain(1,1);
        //
        lvl.setSpawn(new Vector3Short(5,5,5));
        lvl.setPosition(new Vector3(5,5,5));

        trn.enumerateWorld2D(2,v -> trn.at(v).setBlock(BlockType.DIRT_WITH_GRASS_ID.getId()));
        trn.enumerateWorld2D(3,v -> trn.at(v).setBlock(BlockType.FLOWER_ID.getId()));
        trn.enumerateWorld2D(4,v -> trn.at(v).setBlock(BlockType.FLOWER_ID.getId()));
        trn.enumerateWorld2D(20,v -> trn.at(v).setBlock(BlockType.DIAMOND_BLOCK_ID.getId()));
        trn.enumerateWorld3D(v -> trn.at(v).setBlockLight((byte) 1).setSkyLight((byte) 1));

        trn.at(2,4,2).setBlock(BlockType.AIR.getId());
        trn.at(2,3,2).setBlock(BlockType.AIR.getId()); //emitter
        trn.at(2,2,2).setBlockLight((byte) 15).setBlock(BlockType.SLAB_ID.getId());
        trn.at(5,3,5).setBlockLight((byte) 15).setBlock(BlockType.AIR.getId()); // emitter
        trn.at(5,2,5).setBlock(BlockType.DIAMOND_ORE_ID.getId());
        trn.at(6,2,5).setBlock(BlockType.AIR.getId());

        trn.at(10,10,10).setBlock(BlockType.EMERALD_ID.getId());
        trn.at(9,10,10).setBlockLight((byte) 15);
        trn.at(11,10,10).setBlockLight((byte) 15);
        trn.at(10,10,9).setBlockLight((byte) 15);
        trn.at(10,10,11).setBlockLight((byte) 15);
        trn.at(10,9,10).setSkyLight((byte) 15);
        //
        request.setWorld(new World(lvl,trn));
    }
}
