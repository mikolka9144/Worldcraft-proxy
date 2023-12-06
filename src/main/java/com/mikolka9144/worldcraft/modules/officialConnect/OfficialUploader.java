package com.mikolka9144.worldcraft.modules.officialConnect;

import com.mikolka9144.worldcraft.common.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import org.springframework.stereotype.Component;

@Component("http-official-upload")
public class OfficialUploader implements HttpUploadInterceptor {
    private final com.mikolka9144.worldcraft.http.HttpClient client;

    public OfficialUploader(ServerConfigManifest manifest) {
        client = new com.mikolka9144.worldcraft.http.HttpClient(
                manifest.getTargetServer(),
                manifest.getTargetHttpPort());
    }

    @Override
    public void uploadWorld(WorldUploadRequest data) {
        client.uploadWorld(data.getToken(), data.getWorld());
    }
}
