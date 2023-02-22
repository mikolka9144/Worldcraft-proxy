package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftThreadHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SocketServer extends WorldcraftThreadHandler implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private final List<PacketInterceptor> interceptors;
    private final PacketServer server;

    public SocketServer(int port, List<PacketInterceptor> interceptors, PacketServer server) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = new ArrayList<>(interceptors);
        this.server = server;
    }
    public void start() throws IOException {
        log.info("Starting socket thread loop");
        while (true){
            // this is thread-locking
            WorldcraftSocket client = new WorldcraftSocket(serverSocket.accept());
            log.info("New client to server connected: "+client.getConnectedIp());

            interceptors.add(server);
            attachToThread(client,interceptors,server);
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
