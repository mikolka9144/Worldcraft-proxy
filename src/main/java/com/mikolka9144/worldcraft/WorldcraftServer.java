package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.http.HttpServer;
import com.mikolka9144.worldcraft.http.logic.HttpWorldRecever;
import com.mikolka9144.worldcraft.http.logic.HttpWorldUploader;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.socket.SocketServer;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.ServerConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class WorldcraftServer implements Closeable {

    private SocketServer socketServer;

    private HttpServer httpServer;

    public static WorldcraftServer configure(ServerConfig config) throws IOException {
        WorldcraftServer server = new WorldcraftServer();
        log.info(String.format("Creating Server: httpHost %d socketHost %d",config.getHostingHttpPort(),config.getHostingSocketPort()));
        var httpUploaders = new ArrayList<>(config.getHttpDownloadInterceptors());
        Collections.reverse(httpUploaders);
        server.createHttpServer(
                config.getHostingHttpPort(),
                config.getHttpDownloadInterceptors(),
                config.getHttpUploadInterceptors()
        );
        server.createSocketServer(
                config.getHostingSocketPort(),
                config.getReqInterceptors(),
                config.getPacketServer()
        );
        return server;
    }

    public void createSocketServer(int port, Supplier<List<PacketInterceptor>> interceptors, Function<WorldCraftPacketIO, PacketServer> socketServersProvider) throws IOException {
        socketServer = new SocketServer(port,interceptors,socketServersProvider);
    }

    public void createHttpServer(int port, List<HttpDownloadInterceptor> receivers, List<HttpUploadInterceptor> uploaders) {
        HttpWorldRecever httpDownloader = new HttpWorldRecever(receivers);
        HttpWorldUploader httpUploaders = new HttpWorldUploader(uploaders);

        httpServer = new HttpServer(port,httpDownloader,httpUploaders);
    }

    @Override
    public void close() throws IOException {
        socketServer.close();
        httpServer.close();
    }

    public void start() throws IOException {
        httpServer.start();
        socketServer.start();
    }
}
