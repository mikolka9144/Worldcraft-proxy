package com.mikolka9144.WoCserver.logic.socket;

import com.mikolka9144.WoCserver.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.model.Packet.WorldcraftSocket;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.function.Function;

public class SocketServer extends WorldcraftThreadHandler implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private ClientInterceptorFunc interceptors;
    private final Function<WorldCraftPacketIO,PacketServer> endpoint;

    public SocketServer(int port, ClientInterceptorFunc interceptors, Function<WorldCraftPacketIO,PacketServer> endpoint) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
        this.endpoint = endpoint;
    }
    public void start() throws IOException {
        while (true){
            WorldcraftSocket socket = new WorldcraftSocket(serverSocket.accept());
            PacketServer server = endpoint.apply(socket.getChannel());
            List<PacketInterceptor> socketInter = interceptors.apply(socket.getChannel(),server);
            socketInter.add(server);
            attachToThread(socket,socketInter);
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
