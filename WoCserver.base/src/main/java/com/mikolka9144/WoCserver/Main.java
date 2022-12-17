package com.mikolka9144.WoCserver;

import com.mikolka9144.WoCserver.logic.WorldcraftServer;
import com.mikolka9144.WoCserver.model.ServerConfig;
import com.mikolka9144.WoCserver.logic.ConfigurationBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length==0){
            System.out.println(String.join("\n",
                    "You need to specify configuration for a server",
                    "Avaliable configurations: default legacy cmd",
                    "Avaliable presets are: cmd-proxy official legacial",
                    "Argument layouts:",
                    "./server <config> <target-hostname> <target-http-port> <target-socket-port> <host-http-port> <host-socket-port>",
                    "./server <preset>"));
        } else if (args.length != 6 && args.length != 1) {
            System.out.println(String.join("\n",
                    "Argument layout ./server <config> <target-hostname> <target-http-port> <target-socket-port> <host-http -port> <host-socket-port>",
                    "Avaliable presets are: cmd-proxy official legacial"));
        }
        else {
            ServerConfig config = ConfigurationBuilder.configureFromArgs(args);
            var server = WorldcraftServer.configure(config);
            server.start();
        }
    }
}
