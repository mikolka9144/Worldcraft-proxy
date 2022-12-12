package com.mikolka9144.WoCserver.worldcraft;


import com.mikolka9144.WoCserver.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.ServerComponents.WorldcraftServer;
import com.mikolka9144.WoCserver.worldcraft.PurchaseFaker;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args){
        ClientInterceptorFunc reqInterceptors  = (client, server) -> List.of(
                new PurchaseFaker(client)
        );
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault(reqInterceptors)){
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
