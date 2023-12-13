package com.mikolka9144.worldcraft.official;

import com.mikolka9144.level.World;
import com.mikolka9144.worldcraft.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("http-official-download")
@Slf4j
public class OfficialDownloader implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        if(request.getWorld() != null){
            log.info("World present. Not downloading from server");
            return;
        }
        var worldBin = client.getWorld(request.getWorldId());
        request.setWorld(World.fromTarGzBin(worldBin));
    }

    private final com.mikolka9144.worldcraft.http.HttpClient client;

    public OfficialDownloader(ServerConfigManifest manifest) {
        client = new com.mikolka9144.worldcraft.http.HttpClient(
                manifest.getTargetServer(),
                manifest.getTargetHttpPort());
    }
}
