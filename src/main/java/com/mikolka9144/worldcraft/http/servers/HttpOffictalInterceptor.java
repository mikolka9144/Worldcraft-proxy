package com.mikolka9144.worldcraft.http.servers;

import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpOffictalInterceptor{
    public static class Uploader implements HttpUploadInterceptor{
        private final String hostname;
        private final int port;

        public Uploader(String hostname, int port){

            this.hostname = hostname;
            this.port = port;
        }
        @Override
        public byte[] uploadWorld(byte[] worldBin, String contentType) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofByteArray(worldBin))
                    .setHeader("Content-type", contentType)
                    .uri(URI.create(String.format("http://%s:%d/worldcraft-web/upload",hostname,port)))
                    .build();
            try {
                client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return worldBin;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static class Downloader implements HttpDownloadInterceptor{
        @Override
        public byte[] getWorld(int worldId,byte[] worldBin) {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(String.format("http://%s:%d/worldcraft-web/rooms/%d/game.tar.gz",hostname,port,worldId)))
                    .build();
            try {
                HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return response.body();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        private final String hostname;
        private final int port;

        public Downloader(String hostname, int port){

            this.hostname = hostname;
            this.port = port;
        }
    }
}
