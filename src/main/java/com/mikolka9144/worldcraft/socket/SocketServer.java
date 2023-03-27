package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftThreadHandler;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class SocketServer extends WorldcraftThreadHandler implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private final Supplier<List<PacketInterceptor>> interceptors;
    private final Function<WorldCraftPacketIO, PacketServer> socketServersProvider;

    public SocketServer(int port, Supplier<List<PacketInterceptor>> interceptors, Function<WorldCraftPacketIO, PacketServer> socketServersProvider) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
        this.socketServersProvider = socketServersProvider;
    }
    public void start() throws IOException {
        log.info("Starting socket thread loop");

        while (true){
            // this is thread-locking
            WorldcraftSocket client = new WorldcraftSocket(serverSocket.accept());
            log.info("New client to server connected: "+client.getConnectedIp());

            List<PacketInterceptor> connectionInterceptors = new ArrayList<>(interceptors.get());
            PacketServer connectionServer = socketServersProvider.apply(client.getChannel());

            connectionServer.startWritebackConnection(new ArrayList<>(connectionInterceptors));

            connectionInterceptors.add(connectionServer);
            attachToThread(client,connectionInterceptors, connectionServer.GetloopbackInterceptors());
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
