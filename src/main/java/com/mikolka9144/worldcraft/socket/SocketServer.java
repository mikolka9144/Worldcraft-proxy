package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.common.config.ServerConfig;
import com.mikolka9144.worldcraft.socket.api.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.server.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.server.WorldcraftThread;
import com.mikolka9144.worldcraft.socket.interceptor.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.server.SendToSocketInterceptor;
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
    public SocketServer(ServerConfig config) throws IOException {
        this(config.getHostingSocketPort(), config.getReqInterceptors());
    }
    public SocketServer(int port, Supplier<List<PacketAlteringModule>> interceptors) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
    }
    public void start() throws IOException {
        log.info("Starting socket thread loop");

        //noinspection InfiniteLoopStatement
        while (true){
            // this is thread-locking
            WorldcraftSocket client = new WorldcraftSocket(serverSocket.accept());
            log.info("New client to server connected: "+client.getConnectedIp());

            List<PacketAlteringModule> clientInterceptors = new ArrayList<>(interceptors.get());
            List<PacketAlteringModule> serverInterceptors = new ArrayList<>(clientInterceptors);

            PacketAlteringModule loopback = new SendToSocketInterceptor(client.getChannel(),() -> onClientDisconnect(clientInterceptors));
            serverInterceptors.add(loopback);
            setupAlteringModules(clientInterceptors,serverInterceptors);

            WorldcraftThread clientThread = new WorldcraftThread(client, clientInterceptors, serverInterceptors);
            clientThread.startThread();
        }
    }

    private void setupAlteringModules(List<PacketAlteringModule> clientInterceptors,List<PacketAlteringModule> serverInterceptors) {
        for (PacketAlteringModule module : clientInterceptors) {
            module.setupSockets(new SocketPacketSender(clientInterceptors,serverInterceptors));
        }
    }

    private void onClientDisconnect(List<PacketAlteringModule> modulesToClose){
        modulesToClose.forEach(packetAlteringModule -> {
            try {
                packetAlteringModule.close();
            }
            catch (Exception ignored){
                log.warn(String.format("%s threw an exception while closing down. Ignoring!",packetAlteringModule.getClass().getName()));
            }
        });
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
