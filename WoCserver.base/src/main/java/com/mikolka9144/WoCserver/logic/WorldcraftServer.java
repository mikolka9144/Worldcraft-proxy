package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.http.HttpWorldRecever;
import com.mikolka9144.WoCserver.logic.http.HttpWorldUploader;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.model.ServerConfig;
import com.mikolka9144.WoCserver.modules.http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.modules.socket.PacketOffitialInterceptor;
import com.mikolka9144.WoCserver.model.HttpInterceptor;
import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class WorldcraftServer implements Closeable {

    private SocketServer socketServer;

    private HttpServer httpServer;

    public static WorldcraftServer configure(ServerConfig config) throws IOException {
        WorldcraftServer server = new WorldcraftServer();

        var httpUploaders = new ArrayList<>(config.getHttpDownloadInterceptors());
        Collections.reverse(httpUploaders);
        server.createHttpServer(
                config.getHostingHttpPort(),
                config.getHttpDownloadInterceptors(),
                httpUploaders
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

    public void createHttpServer(int port, List<HttpInterceptor> receivers, List<HttpInterceptor> uploaders) throws IOException {
        HttpWorldRecever httpDownloader = new HttpWorldRecever();
        httpDownloader.getDownloadInterceptors().addAll(receivers);
        HttpWorldUploader httpUploaders = new HttpWorldUploader();
        httpUploaders.getUploadInterceptors().addAll(uploaders);
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
