package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.config.CmdExt;
import com.mikolka9144.WoCserver.config.DefaultWoC;
import com.mikolka9144.WoCserver.config.GigachadWoC;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.model.HttpInterceptor;
import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.ServerConfig;
import lombok.val;

public class ConfigurationBuilder {
    public static ServerConfig configureFromArgs(String[] args){
        String config_str = args[0];
        return switch (config_str){
            case "official" ->configure(ConfigPreset.Default,
                    "64.237.54.60",
                    80,
                    SocketServer.WORLD_OF_CRAFT_PORT,
                    80,
                    SocketServer.WORLD_OF_CRAFT_PORT);

        }
//        switch (config_str) {
//            case "default" -> DefaultWoC.main(hostname, port, hostingPort);
//            case "legacy" -> GigachadWoC.main(hostname, port, hostingPort);
//            case "cmd" -> CmdExt.main(hostname, port, hostingPort);
//            default -> System.exit(-1);
//        }
        int port = Integer.parseInt(args[2]); //443
        val hostingPort = Integer.parseInt(args[3]); //443
    }
    public static ServerConfig configure(ConfigPreset preset, String targetServer, int targetServerHttpPort, int targetServerSocketPort, int hostingHttpPort, int hostingSocketPort){
        ClientInterceptorFunc socketPreset;
        HttpInterceptor httpPreset;
        switch (preset){

        }
    }

    enum ConfigPreset {
        Default,
        Legacy,
        Cmd
    }
}
