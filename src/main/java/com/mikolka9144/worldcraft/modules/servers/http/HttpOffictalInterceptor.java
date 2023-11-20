package com.mikolka9144.worldcraft.modules.servers.http;

import com.mikolka9144.worldcraft.common.World;
import com.mikolka9144.worldcraft.common.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

public class HttpOffictalInterceptor{
    private HttpOffictalInterceptor(){}
    @Component("http-official-upload")
    public static class Uploader implements HttpUploadInterceptor{
        private final com.mikolka9144.worldcraft.http.HttpClient client;

        public Uploader(ServerConfigManifest manifest){
            client = new com.mikolka9144.worldcraft.http.HttpClient(
                    manifest.getTargetServer(),
                    manifest.getTargetHttpPort());
        }

        @Override
        public void uploadWorld(WorldUploadRequest data) {
            client.uploadWorld(data.getToken(), data.getWorld());
        }
    }
    @Component("http-official-download")
    public static class Downloader implements HttpDownloadInterceptor{
        @SneakyThrows
        @Override
        public World getWorld(int worldId, World world) {
            var worldBin = client.getWorld(worldId);
            return World.fromTarGzBin(worldBin);
        }
        private final com.mikolka9144.worldcraft.http.HttpClient client;

        public Downloader(ServerConfigManifest manifest){
            client = new com.mikolka9144.worldcraft.http.HttpClient(
                    manifest.getTargetServer(),
                    manifest.getTargetHttpPort());
        }
    }
}
