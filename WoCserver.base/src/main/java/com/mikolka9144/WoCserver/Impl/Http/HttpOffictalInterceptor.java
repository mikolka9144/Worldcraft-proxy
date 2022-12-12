package com.mikolka9144.WoCserver.Impl.Http;

import com.mikolka9144.WoCserver.Models.HttpInterceptor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpOffictalInterceptor implements HttpInterceptor {
    @Override
    public byte[] getWorld(int worldId,byte[] worldBin) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://worldcraft.solverlabs.com/worldcraft-web/rooms/"+worldId+"/game.tar.gz"))
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
                .uri(URI.create("http://worldcraft.solverlabs.com/worldcraft-web/upload"))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return worldBin;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
