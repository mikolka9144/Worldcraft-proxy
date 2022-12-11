package com.mikolka9144.WoCserver.cmdExts;

import com.mikolka9144.WoCserver.cmdExts.SocketModules.ChatCommandsInterceptor;
import com.mikolka9144.WoCserver.cmdExts.SocketModules.DeviceSpoofer;
import com.mikolka9144.WoCserver.cmdExts.SocketModules.PacketLogger;
import com.mikolka9144.WoCserver.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.Models.PacketProtocol;
import com.mikolka9144.WoCserver.ServerComponents.WorldcraftServer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientInterceptorFunc reqInterceptors  = (client, server) -> List.of(
                new ChatCommandsInterceptor(client,server, PacketProtocol.WORLD_OF_CRAFT_V_3_8_6),
                new PacketLogger(client),
                new DeviceSpoofer(client)
        );
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault(reqInterceptors,"64.237.54.60",443)){
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}