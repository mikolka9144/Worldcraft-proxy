package com.mikolka9144;

import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault()){
                server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}