package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Impl.*;
import com.mikolka9144.Impl.Http.HttpOffictalInterceptor;
import com.mikolka9144.Impl.Http.WoC287WorldFixer;
import com.mikolka9144.Models.HttpInterceptor;
import com.mikolka9144.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.Models.Interceptors.ServerInterceptorFunc;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpServer;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldRecever;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldUploader;
import com.mikolka9144.Worldcraft.ServerComponents.socket.SocketServer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorldcraftServer implements Closeable {

    private SocketServer socketServer;

    private HttpServer httpServer;

    public void createSocketServer(int port, ServerInterceptorFunc interceptors) throws IOException {
        socketServer = new SocketServer(port,interceptors);
    }

    public void createHttpServer(int port, List<HttpInterceptor> receivers,List<HttpInterceptor> uploaders) throws IOException {
        HttpWorldRecever httpDownloader = new HttpWorldRecever();
        httpDownloader.getDownloadInterceptors().addAll(receivers);
        HttpWorldUploader httpUploaders = new HttpWorldUploader();
        httpUploaders.getUploadInterceptors().addAll(uploaders);
        httpServer = new HttpServer(port,httpDownloader,httpUploaders);
    }
    public static WorldcraftServer configureWorldcraftDefault(
            ClientInterceptorFunc reqInterceptors,
            ServerInterceptorFunc respInterceptors) throws IOException {
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
                io -> {
                    var official = new PacketOffitialInterceptor(io, respInterceptors);
                    var ret = new ArrayList<>(reqInterceptors.apply(io,official));
                    ret.add(official);
                    return ret;
                }
        );
        return server;
    }
    public static WorldcraftServer configureLegacy(
            ClientInterceptorFunc reqInterceptors,
            ServerInterceptorFunc respInterceptors) throws IOException {
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
                io -> {
                    var official = new PacketOffitialInterceptor(io,s -> {
                        var resp = new ArrayList<>(respInterceptors.apply(s));
                        resp.addAll(List.of(
                                new PacketConverter(s)
                        ));
                        return resp;
                    });
                    var ret = new ArrayList<>(reqInterceptors.apply(io,official));
                    ret.add(official);
                    return ret;
                }
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
