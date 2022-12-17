package com.mikolka9144.WoCserver.modules.http;

import com.mikolka9144.WoCserver.model.HttpInterceptor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpOffictalInterceptor implements HttpInterceptor {
    private final String hostname;
    private final int port;

    public HttpOffictalInterceptor(String hostname, int port){

        this.hostname = hostname;
        this.port = port;
    }
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

    @Override
    public byte[] uploadWorld(byte[] worldBin, String ContentType) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofByteArray(worldBin))
                .setHeader("Content-type",ContentType)
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
