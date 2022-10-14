package com.mikolka9144;

import com.mikolka9144.Models.HttpInterceptor;
import com.mikolka9144.Models.EventCodecs.PacketInterceptor;
import com.mikolka9144.Worldcraft.ServerComponents.HttpWorldRecever;
import com.mikolka9144.Worldcraft.ServerComponents.HttpWorldUploader;
import com.mikolka9144.Worldcraft.ServerComponents.SocketServer;
import com.mikolka9144.Worldcraft.ServerComponents.HttpServer;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class WorldcraftServer implements Closeable {

    private SocketServer socketServer;

    private HttpServer httpServer;

    public void createSocketServer(int port, Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors) throws IOException {
        socketServer = new SocketServer(port,interceptors);
    }

    public void createHttpServer(int port, List<HttpInterceptor> receivers,List<HttpInterceptor> uploaders) throws IOException {
        var httpDownloader = new HttpWorldRecever();
        httpDownloader.getDownloadInterceptors().addAll(receivers);
        var httpUploaders = new HttpWorldUploader();
        httpDownloader.getDownloadInterceptors().addAll(uploaders);
        httpServer = new HttpServer(port,httpDownloader,httpUploaders);
    }
    @Override
    public void close() throws IOException {
        socketServer.close();
        httpServer.close();
    }

    public void start() throws IOException {
        socketServer.start();
    }
}
