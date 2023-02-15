package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.socket.model.ServerConfig;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        switch (args.length) {
            default -> {
                System.out.println("You need to specify configuration for a server");
                System.out.println("Available configurations: Default Legacy Cmd");
                System.out.println("Available presets are: Official Legacial");
                System.out.println("Argument layouts:");
                System.out.println("./server <config> <target-hostname> <target-http-port> <target-socket-port> <host-http-port> <host-socket-port>");
                System.out.println("./server <preset>");
            }
            case 1 -> {
                try {
                    ServerConfig config = ConfigurationBuilder.configure(ConfigurationBuilder.ServerPreset.valueOf(args[0]));
                    try (var server = WorldcraftServer.configure(config)) {
                        server.start();
                    }
                }
                catch ( IllegalArgumentException x){
                    System.out.println("couldn't find preset you specified");
                    System.out.println("Available presets are: Official Legacial");
                }
            } case 6 -> {
                try {
                    ServerConfig config = ConfigurationBuilder.configure(
                            ConfigurationBuilder.ConfigPreset.valueOf(args[0]),
                            args[1],
                            Integer.parseInt(args[2]),
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5]));
                    try (var server = WorldcraftServer.configure(config)) {
                        server.start();
                    }
                } catch (IllegalArgumentException x) {
                    System.out.println("Couldn't find a config you specified");
                    System.out.println("Available configurations: Default Legacy Cmd");
                    System.out.println("./server <config> <target-hostname> <target-http-port> <target-socket-port> <host-http-port> <host-socket-port>");
                }
            }
        }
    }
}
