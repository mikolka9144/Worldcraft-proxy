package com.mikolka9144.worldcraft.backend.spring.official;

import com.mikolka9144.worldcraft.backend.spring.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.backend.base.http.HttpClient;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.model.WorldUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("http-official-upload")
@Slf4j
public class OfficialUploader implements HttpUploadInterceptor {
    private final HttpClient client;

    public OfficialUploader(ServerConfigManifest manifest) {
        client = new HttpClient(
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
