package com.mikolka9144;

import com.mikolka9144.Impl.Loggers.PacketLogger;
import com.mikolka9144.Impl.PacketOffitialInterceptor;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try(WorldcraftServer server = new WorldcraftServer(
                WorldcraftServer.WORLD_OF_CRAFRT_HTTP_PORT,io -> List.of(
                        new PacketLogger(io),
                        new PacketOffitialInterceptor(io)
        ))) {
           server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}