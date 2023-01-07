package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.http.HttpWorldRecever;
import com.mikolka9144.WoCserver.logic.http.HttpWorldUploader;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.HttpDownloadInterceptor;
import com.mikolka9144.WoCserver.model.HttpUploadInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.model.ServerConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
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

    public void createSocketServer(int port, ClientInterceptorFunc interceptors, Function<WorldCraftPacketIO,PacketServer> server) throws IOException {
        socketServer = new SocketServer(port,interceptors,server);
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
