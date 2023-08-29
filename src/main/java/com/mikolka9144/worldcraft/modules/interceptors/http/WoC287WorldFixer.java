package com.mikolka9144.worldcraft.modules.interceptors.http;

import com.mikolka9144.worldcraft.common.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("WoC287-world-patch")
public class WoC287WorldFixer implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public World getWorld(int worldId, World world) {
        //Modifying
        if (!world.getLevel().getData().contains("LastPlayed")) {
            world.getLevel().getData().putLong("LastPlayed", 0);
        }
        //Saving
        return world;

    }
}
