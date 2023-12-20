package com.mikolka9144.worldcraft.backend.server.official;

import com.mikolka9144.worldcraft.backend.client.http.HttpWorldClient;
import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;
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
        log.info("Downloading "+request.getWorldId());
        var worldBin = client.getWorld(request.getWorldId());
        log.info("Parsing "+request.getWorldId());
        request.setWorld(World.fromTarGzBin(worldBin));
        log.info("Done "+request.getWorldId());
    }

    private final HttpWorldClient client;

    public OfficialDownloader(ServerConfigManifest manifest) {
        client = new HttpWorldClient(
                manifest.getTargetServer(),
                manifest.getTargetHttpPort());
    }
}
