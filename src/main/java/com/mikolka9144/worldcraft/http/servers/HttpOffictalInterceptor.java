package com.mikolka9144.worldcraft.http.servers;

import com.mikolka9144.worldcraft.common.ServerConfigManifest;
import com.mikolka9144.worldcraft.common.level.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@SuppressWarnings("HttpUrlsUsage")
// As a game server I don't need to create a certificate JUST to send a world over a http
public class HttpOffictalInterceptor{
    private HttpOffictalInterceptor(){}
    @Component("http-official-upload")
    public static class Uploader implements HttpUploadInterceptor{
        private final String hostname;
        private final int port;

        public Uploader(ServerConfigManifest manifest){

            this.hostname = manifest.getTargetServer();
            this.port = manifest.getTargetHttpPort();
        }
        @Override
        public byte[] uploadWorld(byte[] world, String contentType) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofByteArray(world))
                    .setHeader("Content-type", contentType)
                    .uri(URI.create(String.format("http://%s:%d/worldcraft-web/upload",hostname,port)))
                    .build();
            try {
                client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return world;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Component("http-official-download")
    public static class Downloader implements HttpDownloadInterceptor{
        @Override
        public World getWorld(int worldId, World world) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(String.format("http://%s:%d/worldcraft-web/rooms/%d/game.tar.gz",hostname,port,worldId)))
                    .build();
            try {
                HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return World.fromTarGzBin(response.body());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        private final String hostname;
        private final int port;

        public Downloader(ServerConfigManifest manifest){

            this.hostname = manifest.getTargetServer();
            this.port = manifest.getTargetHttpPort();
        }
    }
}
