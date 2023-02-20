package com.mikolka9144.worldcraft.http;

import com.mikolka9144.worldcraft.http.logic.HttpWorldRecever;
import com.mikolka9144.worldcraft.http.logic.HttpWorldUploader;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
@Slf4j
public class HttpServer implements Closeable {
    public static final int WORLD_OF_CRAFRT_HTTP_PORT = 80;
    public static final int WORLDCRAFT_HTTP_PORT = 8080;
    private com.sun.net.httpserver.HttpServer httpServer;

    public HttpServer(int port, HttpWorldRecever httpRecever, HttpWorldUploader httpUploader) {
        try {
            httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port),50);
        } catch (IOException e) {
            log.error("Error occurred while placing http server: ",e);
        }
        httpServer.setExecutor(Executors.newFixedThreadPool(10));
        httpServer.createContext("/worldcraft-web/rooms/",httpRecever);
        httpServer.createContext("/worldcraft-web/upload",httpUploader);
    }
    @Override
    public void close() throws IOException {
        httpServer.stop(0);
    }
    public void start(){
        log.info("Starting http server");
        httpServer.start();
    }
}
