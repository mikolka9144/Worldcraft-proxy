package com.mikolka9144.Worldcraft;

import com.mikolka9144.Models.HttpInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.ArrayList;
import java.util.List;

public class WorldcraftHttpProxy implements HttpHandler {
    private List<HttpInterceptor> downloadInterceptors = new ArrayList<>();
    private List<HttpInterceptor> uploadInterceptors = new ArrayList<>();
    @Override
    public void handle(HttpExchange httpExchange) {
        try(httpExchange){
            String urlPath = httpExchange.getRequestURI().toString();
            if(urlPath.contains("rooms")){
                int roomId = Integer.parseInt(urlPath.split("/")[3]);
                byte[] worldBin = new byte[0];

                for (HttpInterceptor interceptor : downloadInterceptors) {
                    worldBin = interceptor.getWorld(roomId,worldBin);
                }

                httpExchange.getResponseHeaders()
                        .add("Content-Type","application/x-gzip");
                httpExchange.sendResponseHeaders(200,worldBin.length);
                httpExchange.getResponseBody().write(worldBin);
            }
            else if (urlPath.contains("upload")) {
                throw new RuntimeException("Uploading not implemented");
            }
        }
        catch (Exception x){
            x.printStackTrace();
            System.exit(-1);
        }
    }

    public List<HttpInterceptor> getDownloadInterceptors() {
        return downloadInterceptors;
    }

    public List<HttpInterceptor> getUploadInterceptors() {
        return uploadInterceptors;
    }
}
