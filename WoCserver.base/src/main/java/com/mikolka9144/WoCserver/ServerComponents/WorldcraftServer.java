package com.mikolka9144.WoCserver.ServerComponents;

import com.mikolka9144.WoCserver.Impl.Http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.Impl.packets.PacketOffitialInterceptor;
import com.mikolka9144.WoCserver.Models.HttpInterceptor;
import com.mikolka9144.WoCserver.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.Models.Packet.PacketServer;
import com.mikolka9144.WoCserver.ServerComponents.http.HttpServer;
import com.mikolka9144.WoCserver.ServerComponents.http.HttpWorldRecever;
import com.mikolka9144.WoCserver.ServerComponents.http.HttpWorldUploader;
import com.mikolka9144.WoCserver.ServerComponents.socket.SocketServer;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldCraftPacketIO;

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
    public static WorldcraftServer configureWorldcraftDefault(
            ClientInterceptorFunc reqInterceptors) throws IOException {
        String hostname = System.getenv("SERVER_IP"); //64.237.54.60
        int port = Integer.getInteger(System.getenv("SERVER_PORT"));
        return configureWorldcraftDefault(reqInterceptors,hostname,port);
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
