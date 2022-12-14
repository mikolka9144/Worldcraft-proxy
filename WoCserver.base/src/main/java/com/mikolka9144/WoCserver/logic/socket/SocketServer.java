package com.mikolka9144.WoCserver.logic.socket;

import com.mikolka9144.WoCserver.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.model.Packet.WorldcraftSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
@Slf4j
public class SocketServer extends WorldcraftThreadHandler implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private final ClientInterceptorFunc interceptors;
    private final Function<WorldCraftPacketIO,PacketServer> endpoint;

    public SocketServer(int port, ClientInterceptorFunc interceptors, Function<WorldCraftPacketIO,PacketServer> endpoint) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
        this.endpoint = endpoint;
    }
    public void start() throws IOException {
        log.info("Starting socket thread loop");
        while (true){
            // this is thread-locking
            WorldcraftSocket socket = new WorldcraftSocket(serverSocket.accept());
            log.info("New client to server connected: "+socket.getConnectedIp());

            PacketServer server = endpoint.apply(socket.getChannel());
            List<PacketInterceptor> socketInter = new ArrayList<>(interceptors.apply(socket.getChannel(),server));
            server.setInterceptors(new ArrayList<>(socketInter));
            socketInter.add(server);
            attachToThread(socket,socketInter);
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
