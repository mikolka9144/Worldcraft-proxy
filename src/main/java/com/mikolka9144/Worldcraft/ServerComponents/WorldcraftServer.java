package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Impl.Http.HttpOffictalInterceptor;
import com.mikolka9144.Impl.Http.WoC287WorldFixer;
import com.mikolka9144.Impl.PacketConverter;
import com.mikolka9144.Impl.PacketOffitialInterceptor;
import com.mikolka9144.Models.HttpInterceptor;
import com.mikolka9144.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.Models.Packet.PacketServer;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpServer;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldRecever;
import com.mikolka9144.Worldcraft.ServerComponents.http.HttpWorldUploader;
import com.mikolka9144.Worldcraft.ServerComponents.socket.SocketServer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

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

    public void createHttpServer(int port, List<HttpInterceptor> receivers,List<HttpInterceptor> uploaders) throws IOException {
        HttpWorldRecever httpDownloader = new HttpWorldRecever();
        httpDownloader.getDownloadInterceptors().addAll(receivers);
        HttpWorldUploader httpUploaders = new HttpWorldUploader();
        httpUploaders.getUploadInterceptors().addAll(uploaders);
        httpServer = new HttpServer(port,httpDownloader,httpUploaders);
    }
    public static WorldcraftServer configureWorldcraftDefault(
            ClientInterceptorFunc reqInterceptors) throws IOException {
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
                }, PacketOffitialInterceptor::new
        );
        return server;
    }
    public static WorldcraftServer configureLegacy(
            ClientInterceptorFunc reqInterceptors) throws IOException {
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
                (io,x) -> {
                    var argumentInterceptors = reqInterceptors.apply(io,x);
                    var ret = new ArrayList<>(argumentInterceptors);
                    ret.add(0,new PacketConverter.Early(io));
                    ret.add(new PacketConverter.Late(io));
                    x.setInterceptors(ret);
                    return ret;
                }, PacketOffitialInterceptor::new
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
