package com.mikolka9144.worldcraft.backend.server.official;

import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.backend.client.http.HttpWorldClient;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("http-official-upload")
@Slf4j
public class OfficialUploader implements HttpUploadInterceptor {
    private final HttpWorldClient client;

    public OfficialUploader(ServerConfigManifest manifest) {
        client = new HttpWorldClient(
                manifest.getTargetServer(),
                manifest.getTargetHttpPort());
    }

    @Override
    public void uploadWorld(WorldUploadRequest data) {
        if(data.getWorld() == null){
            log.info("World missing. Not uploading to server");
            return;
        }
        client.uploadWorld(data.getToken(), data.getWorld());
    }
}
