package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Models.HttpInterceptor;
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

    }
}
