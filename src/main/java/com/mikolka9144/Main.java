package com.mikolka9144;

import com.mikolka9144.Impl.HttpOffictalInterceptor;
import com.mikolka9144.Impl.Loggers.PacketLogger;
import com.mikolka9144.Impl.PacketOffitialInterceptor;
import com.mikolka9144.Worldcraft.ServerComponents.HttpServer;
import com.mikolka9144.Worldcraft.ServerComponents.SocketServer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try(WorldcraftServer server = new WorldcraftServer()){
                server.createHttpServer(
                        HttpServer.WORLDCRAFRT_HTTP_PORT,
                        List.of(new HttpOffictalInterceptor()),
                        List.of()
                );
                server.createSocketServer(
                        SocketServer.WORLDCRAFT_PORT,
                        io -> List.of(
                                new PacketLogger(io),
                                new PacketOffitialInterceptor(io))
                );
                server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}