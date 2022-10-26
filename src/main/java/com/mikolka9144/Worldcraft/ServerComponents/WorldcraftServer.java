package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Impl.*;
import com.mikolka9144.Models.HttpInterceptor;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpServer;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldRecever;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldUploader;
import com.mikolka9144.Worldcraft.ServerComponents.socket.SocketServer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

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
        HttpWorldRecever httpDownloader = new HttpWorldRecever();
        httpDownloader.getDownloadInterceptors().addAll(receivers);
        HttpWorldUploader httpUploaders = new HttpWorldUploader();
        httpUploaders.getUploadInterceptors().addAll(uploaders);
        httpServer = new HttpServer(port,httpDownloader,httpUploaders);
    }
    public static WorldcraftServer configureWorldcraftDefault() throws IOException {
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
                io -> List.of(
                        new PacketLogger(io),
                        new PacketOffitialInterceptor(io, s -> List.of(
                                new PurchaseFaker(io),
                                new PacketLogger(s)
                        )))
        );
        return server;
    }
    public static WorldcraftServer configureLegacy() throws IOException {
        WorldcraftServer server = new WorldcraftServer();
        server.createHttpServer(
                HttpServer.WORLDCRAFRT_HTTP_PORT,
                List.of(
                        new HttpOffictalInterceptor(),
                        new WoC287WorldFixer()
                ),
                List.of(
                        new HttpOffictalInterceptor()
                )
        );
        server.createSocketServer(
                SocketServer.WORLDCRAFT_PORT,
                io -> List.of(
                        new PacketLogger(io),
                        new PacketOffitialInterceptor(io,s -> List.of(
                                new PacketConverter(s),
                                new PacketLogger(s)
                        )))
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
