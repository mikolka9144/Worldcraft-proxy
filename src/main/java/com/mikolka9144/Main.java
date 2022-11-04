package com.mikolka9144;

import com.mikolka9144.Impl.ChatCommandsInterceptor;
import com.mikolka9144.Impl.DeviceSpoofer;
import com.mikolka9144.Impl.PurchaseFaker;
import com.mikolka9144.Models.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.Models.Interceptors.ServerInterceptorFunc;
import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftServer;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientInterceptorFunc reqInterceptors  = (client, server) -> List.of(
            new ChatCommandsInterceptor(client,server),
                new DeviceSpoofer(client)
        );
        ServerInterceptorFunc respInterceptors = client -> List.of(
            new PurchaseFaker(client)
        );
        try(WorldcraftServer server = WorldcraftServer.configureWorldcraftDefault(reqInterceptors,respInterceptors)){
            //new Thread(() -> {
             //   try {
             //       WorldcraftServer.configureLegacy(reqInterceptors,respInterceptors).start();
             //   } catch (IOException e) {
             //       throw new RuntimeException(e);
             //   }
            //}).start();
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}