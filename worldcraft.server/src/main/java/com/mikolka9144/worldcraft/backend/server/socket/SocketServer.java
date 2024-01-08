package com.mikolka9144.worldcraft.backend.server.socket;

import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftSocket;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfig;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
public class SocketServer implements Closeable {
    private final ServerSocket serverSocket;
    private final Supplier<List<PacketAlteringModule>> interceptors;

    @Autowired
    public SocketServer(ServerConfig config){
        this(config.getHostingSocketPort(), config.getReqInterceptors());
    }

    public SocketServer(int port, Supplier<List<PacketAlteringModule>> interceptors)  {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create socket server on port "+port,e);
        }
        this.interceptors = interceptors;
    }

    public void start() throws IOException {
        log.info("Starting socket thread loop");

        //noinspection InfiniteLoopStatement
        while (true) {
            // this is thread-locking
            WorldcraftSocket client = new WorldcraftSocket(serverSocket.accept());
            log.info("New client to server connected: " + client.getConnectedIp());

            List<PacketAlteringModule> clientInterceptors = new ArrayList<>(interceptors.get());
            List<PacketAlteringModule> serverInterceptors = new ArrayList<>(clientInterceptors);

            PacketAlteringModule loopback = new SendToSocketInterceptor(client.getChannel(), () -> onClientDisconnect(clientInterceptors));
            serverInterceptors.add(loopback);
            setupAlteringModules(clientInterceptors, serverInterceptors, client);

            WorldcraftThread clientThread = new WorldcraftThread(client, clientInterceptors, serverInterceptors);
            clientThread.startThread();
        }
    }

    private void setupAlteringModules(List<PacketAlteringModule> clientInterceptors, List<PacketAlteringModule> serverInterceptors, WorldcraftSocket conn) {
        for (PacketAlteringModule module : clientInterceptors) {
            module.setupSockets(new SocketPacketSender(clientInterceptors, serverInterceptors, conn));
        }
    }

    private void onClientDisconnect(List<PacketAlteringModule> modulesToClose) {
        modulesToClose.forEach(packetAlteringModule -> {
            try {
                packetAlteringModule.close();
            } catch (Exception ignored) {
                log.warn(String.format("%s threw an exception while closing down. Ignoring!", packetAlteringModule.getClass().getName()));
            }
        });
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
