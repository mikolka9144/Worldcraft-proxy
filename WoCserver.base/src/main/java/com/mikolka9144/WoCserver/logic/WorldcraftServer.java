package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.http.HttpWorldRecever;
import com.mikolka9144.WoCserver.logic.http.HttpWorldUploader;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.modules.http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.modules.socket.PacketOffitialInterceptor;
import com.mikolka9144.WoCserver.model.HttpInterceptor;
import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WorldcraftServer implements Closeable {

    private SocketServer socketServer;

    private HttpServer httpServer;

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
    public static WorldcraftServer configureWorldcraftDefault(
            ClientInterceptorFunc reqInterceptors,String hostname,int port) throws IOException {
        WorldcraftServer server = new WorldcraftServer();
        server.createHttpServer(
                HttpServer.WORLD_OF_CRAFRT_HTTP_PORT,
                List.of(
                        new HttpOffictalInterceptor()
                ),
                List.of(
                        new HttpOffictalInterceptor()
                )
        );
        server.createSocketServer(
                SocketServer.WORLD_OF_CRAFT_PORT,
                (io,x) -> {
                    var argumentInterceptors = reqInterceptors.apply(io,x);
                    var ret = new ArrayList<>(argumentInterceptors);
                    x.setInterceptors(argumentInterceptors);
                    return ret;
                }, io -> new PacketOffitialInterceptor(io,hostname,port)
        );
        return server;
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
