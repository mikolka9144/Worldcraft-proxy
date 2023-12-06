package com.mikolka9144.worldcraft.modules.officialConnect;

import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.common.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component("http-official-download")
public class OfficialDownloader implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
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
