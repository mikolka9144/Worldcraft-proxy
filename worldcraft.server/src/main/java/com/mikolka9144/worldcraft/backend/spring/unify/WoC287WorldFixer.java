package com.mikolka9144.worldcraft.backend.spring.unify;

import com.mikolka9144.worldcraft.level.World;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("WoC287-world-patch")
public class WoC287WorldFixer implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        World world = request.getWorld();
        //Modifying
        if (!world.getLevel().getData().contains("LastPlayed")) {
            world.getLevel().getData().putLong("LastPlayed", 0);
        }
    }
}
