package com.mikolka9144.worldcraft.simba;

import com.mikolka9144.worldcraft.backend.level.Level;
import com.mikolka9144.worldcraft.backend.level.Terrain;
import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.utills.Vector3;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Consumer;

@Component("direct-download")
public class DirectConnectHttpInterceptors implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        switch (request.getWorldId()){
            case 1:{
                setWorld(request,"./game.tar.gz");
                return;
            }
            case 2:{
                setWorld(request,"./exp1.tar.gz");
                return;
            }
            case 3:{
                setWorld(request,"./zz.tar.gz");
                return;
            }
            default:{
                Level lvl = new Level();
                lvl.setPosition(new Vector3(50,20,30));
                Terrain terrain = new Terrain(6,8);
                Random rng = SecureRandom.getInstanceStrong();


                terrain.enumerateWorld2D(1,s -> terrain.at(s).setBlockType(BlockType.SAND_ID));
                terrain.enumerateWorld2D(2,s -> {
                    int height = rng.nextInt(4);
                    for (int i = 0; i < height; i++) {
                        terrain.at(s.getX(),s.getY()+i,s.getZ()).setBlockType(BlockType.DIRT_ID);
                    }
                    if(height != 0) {
                        terrain.at(s.getX(),s.getY()+height,s.getZ()).setBlockType(BlockType.DIRT_WITH_GRASS_ID);
                        if(rng.nextInt(10) == 1) terrain.at(s.getX(),s.getY()+height+1,s.getZ()).setBlockType(BlockType.FLOWER_ID);
                    }

                });
                Consumer<Vector3Short> waterGen = s -> {
                    if (terrain.at(s).getBlockType() == BlockType.AIR) terrain.at(s).setBlockType(BlockType.WATER_ID);
                };
                terrain.enumerateWorld2D(2, waterGen);
                terrain.enumerateWorld2D(3, waterGen);
                request.setWorld(new World(lvl,terrain));
            }
        }
    }

    private static void setWorld(WorldDownloadRequest request, String s) throws IOException {
        byte[] file = Files.readAllBytes(Path.of(s));
        request.setWorld(World.fromTarGzBin(file));
    }
}
