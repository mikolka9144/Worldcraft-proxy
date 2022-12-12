package com.mikolka9144.WoCserver.ServerComponents.http;

import com.mikolka9144.WoCserver.Models.HttpInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpWorldUploader implements HttpHandler {
    private List<HttpInterceptor> uploadInterceptors = new ArrayList<>();

    public List<HttpInterceptor> getUploadInterceptors() {
        return uploadInterceptors;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        byte[] data = httpExchange.getRequestBody().readAllBytes();
        String contentType = httpExchange.getRequestHeaders().getFirst("Content-type");
        for (HttpInterceptor interceptor : uploadInterceptors) {
            data = interceptor.uploadWorld(data,contentType);
        }
        httpExchange.sendResponseHeaders(200,2);
        httpExchange.getResponseBody().write("OK".getBytes());
        httpExchange.close();
    }
}
