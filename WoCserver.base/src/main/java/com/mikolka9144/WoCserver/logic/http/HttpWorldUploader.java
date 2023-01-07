package com.mikolka9144.WoCserver.logic.http;

import com.mikolka9144.WoCserver.model.HttpUploadInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class HttpWorldUploader implements HttpHandler {
    private final List<HttpUploadInterceptor> uploadInterceptors;
    public HttpWorldUploader(List<HttpUploadInterceptor> interceptors){
        uploadInterceptors = interceptors;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        byte[] data = httpExchange.getRequestBody().readAllBytes();
        String contentType = httpExchange.getRequestHeaders().getFirst("Content-type");
        for (HttpUploadInterceptor interceptor : uploadInterceptors) {
            data = interceptor.uploadWorld(data,contentType);
        }
        httpExchange.sendResponseHeaders(200,2);
        httpExchange.getResponseBody().write("OK".getBytes());
        httpExchange.close();
    }
}
