package com.mikolka9144;

import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault()){
            new Thread(() -> {
                try {
                    WorldcraftServer.configureLegacy().start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}