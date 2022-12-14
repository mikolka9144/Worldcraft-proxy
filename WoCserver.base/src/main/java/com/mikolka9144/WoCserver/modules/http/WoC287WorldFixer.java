package com.mikolka9144.WoCserver.modules.http;

import com.mikolka9144.WoCserver.model.HttpDownloadInterceptor;
import com.mikolka9144.WoCserver.utills.level.World;

import java.io.IOException;

public class WoC287WorldFixer implements HttpDownloadInterceptor {
    @Override
    public byte[] getWorld(int worldId, byte[] worldBin) {
        try {
            World world = World.fromTarGzBin(worldBin);
            // Modify
            if(!world.level_dat.getCompound("Data").contains("LastPlayed")){
                world.level_dat.getCompound("Data").putLong("LastPlayed",0);
            }
            //Saving
            return world.toTarGzBin();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
