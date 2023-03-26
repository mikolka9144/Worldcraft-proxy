package com.mikolka9144.worldcraft.http.modules;

import com.mikolka9144.worldcraft.http.logic.level.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import lombok.SneakyThrows;

public class WoC287WorldFixer implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public byte[] getWorld(int worldId, byte[] worldBin) {
        World world = World.fromTarGzBin(worldBin);
        world.getLevel(s -> {
            // Modify
            if (!s.getNbt().getCompound("Data").contains("LastPlayed")) {
                s.getNbt().getCompound("Data").putLong("LastPlayed", 0);
            }
        });
        //Saving
        return world.toTarGzBin();

    }
}
