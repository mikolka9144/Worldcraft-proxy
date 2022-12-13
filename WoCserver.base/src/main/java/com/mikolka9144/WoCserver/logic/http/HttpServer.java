package com.mikolka9144.WoCserver.logic.http;

import com.mikolka9144.WoCserver.model.HttpInterceptor;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

public class HttpServer implements Closeable {
    public static final int WORLD_OF_CRAFRT_HTTP_PORT = 80;
    public static final int WORLDCRAFRT_HTTP_PORT = 8080;
    private final com.sun.net.httpserver.HttpServer httpServer;
    private final HttpWorldRecever httpRecever;

    public HttpServer(int port, HttpWorldRecever httpRecever, HttpWorldUploader httpUploader) throws IOException {
        httpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port),50);
        this.httpRecever = httpRecever;
        httpServer.setExecutor(Executors.newFixedThreadPool(10));
        httpServer.createContext("/worldcraft-web/rooms/",httpRecever);
        httpServer.createContext("/worldcraft-web/upload",httpUploader);
        httpServer.start();
    }
    public List<HttpInterceptor> getReceverInterceptors(){
        return httpRecever.getDownloadInterceptors();
    }

    @Override
    public void close() throws IOException {
        httpServer.stop(0);
    }
}
