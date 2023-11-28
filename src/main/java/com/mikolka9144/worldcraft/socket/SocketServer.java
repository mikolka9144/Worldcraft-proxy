package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.common.config.ServerConfig;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftThread;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Service
public class SocketServer implements Closeable {
    private final ServerSocket serverSocket;
    private final Supplier<List<PacketAlteringModule>> interceptors;
    private final Function<WorldcraftPacketIO, PacketServer> socketServersProvider;
    @Autowired
    public SocketServer(ServerConfig config) throws IOException {
        this(config.getHostingSocketPort(), config.getReqInterceptors(), config.getPacketServer());
    }
    public SocketServer(int port, Supplier<List<PacketAlteringModule>> interceptors, Function<WorldcraftPacketIO, PacketServer> socketServersProvider) throws IOException {
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

            List<PacketAlteringModule> clientInterceptors = new ArrayList<>(interceptors.get());
            PacketServer connectionServer = socketServersProvider.apply(client.getChannel());


            connectionServer.startWritebackConnection(new ArrayList<>(clientInterceptors));
            setupAlteringModules(clientInterceptors,connectionServer);

            WorldcraftThread clientThread = new WorldcraftThread(client, clientInterceptors, connectionServer.GetloopbackInterceptors());
            clientThread.attachToThread().start();
        }
    }

    private void setupAlteringModules(List<PacketAlteringModule> clientInterceptors, PacketServer connectionServer) {
        clientInterceptors.add(connectionServer);
        for (PacketAlteringModule module : clientInterceptors) {
            module.setupSockets(new SocketPacketSender(clientInterceptors,connectionServer.GetloopbackInterceptors()));
        }
    }


    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
