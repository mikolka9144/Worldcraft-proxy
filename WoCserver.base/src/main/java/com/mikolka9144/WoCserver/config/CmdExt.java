package com.mikolka9144.WoCserver.config;

import com.mikolka9144.WoCserver.modules.socket.ChatCommandsInterceptor;
import com.mikolka9144.WoCserver.modules.socket.DeviceSpoofer;
import com.mikolka9144.WoCserver.modules.socket.PacketLogger;
import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.logic.WorldcraftServer;

import java.io.IOException;
import java.util.List;

public class CmdExt {
    public static void main(String hostname, int port) {
        ClientInterceptorFunc reqInterceptors  = (client, server) -> List.of(
                new ChatCommandsInterceptor(client,server),
                new PacketLogger(client),
                new DeviceSpoofer(client)
        );
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault(reqInterceptors,hostname,port)){
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}