package com.mikolka9144.WoCserver.logic.http;

import com.mikolka9144.WoCserver.model.HttpDownloadInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class HttpWorldRecever implements HttpHandler {
    private final List<HttpDownloadInterceptor> downloadInterceptors;
    public HttpWorldRecever(List<HttpDownloadInterceptor> interceptors){
        downloadInterceptors = interceptors;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String urlPath = httpExchange.getRequestURI().toString();
        int roomId = Integer.parseInt(urlPath.split("/")[3]);
        byte[] worldBin = new byte[0];

        for (HttpDownloadInterceptor interceptor : downloadInterceptors) {
            worldBin = interceptor.getWorld(roomId,worldBin);
        }

        httpExchange.getResponseHeaders()
                .add("Content-Type","application/x-gzip");
        httpExchange.sendResponseHeaders(200,worldBin.length);
        httpExchange.getResponseBody().write(worldBin);
    }
}
