package com.mikolka9144.WoCserver.config;


import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.logic.WorldcraftServer;
import com.mikolka9144.WoCserver.modules.socket.PurchaseFaker;

import java.io.IOException;
import java.util.List;

public class DefaultWoC {
    public static void main(String hostname, int port, int hostingPort){
        ClientInterceptorFunc reqInterceptors  = (client, server) -> List.of(
                new PurchaseFaker(client)
        );
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault(reqInterceptors,hostname,port)){
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
