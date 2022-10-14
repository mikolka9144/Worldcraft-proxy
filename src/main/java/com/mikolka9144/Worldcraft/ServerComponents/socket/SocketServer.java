package com.mikolka9144.Worldcraft.ServerComponents.socket;

import com.mikolka9144.Models.Packets.PacketInterceptor;
import com.mikolka9144.Models.WorldcraftSocket;
import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftThreadHandler;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.function.Function;

public class SocketServer extends WorldcraftThreadHandler implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors;

    public SocketServer(int port, Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
    }
    public void start() throws IOException {
        while (true){
            WorldcraftSocket socket = new WorldcraftSocket(serverSocket.accept());
            List<PacketInterceptor> socketInter = interceptors.apply(socket.getChannel());
            attachToThread(socket,socketInter);
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
