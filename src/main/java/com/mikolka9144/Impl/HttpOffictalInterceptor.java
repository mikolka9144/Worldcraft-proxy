package com.mikolka9144.Impl;

import com.mikolka9144.Models.HttpInterceptor;

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
    public byte[] uploadWorld(int worldId, byte[] worldBin) {
        throw new RuntimeException("Not implemented");
    }
}
