package com.mikolka9144.worldcraft.official;

import com.mikolka9144.worldcraft.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("http-official-upload")
@Slf4j
public class OfficialUploader implements HttpUploadInterceptor {
    private final com.mikolka9144.worldcraft.http.HttpClient client;

    public OfficialUploader(ServerConfigManifest manifest) {
        client = new com.mikolka9144.worldcraft.http.HttpClient(
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
