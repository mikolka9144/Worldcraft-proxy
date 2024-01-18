package com.mikolka9144.worldcraft.backend.server.socket;

import com.mikolka9144.worldcraft.backend.client.socket.SocketClient;
import com.mikolka9144.worldcraft.backend.client.socket.SocketPacketIO;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftThread;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.PacketInterceptor;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.SocketPacketSender;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfig;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
public class WorldcraftServer implements Closeable {
    private final ServerSocket serverSocket;

    private final Supplier<List<PacketInterceptor>> interceptors;

    @Autowired
    public WorldcraftServer(ServerConfig config){
        this(config.getHostingSocketPort(), config.getReqInterceptors());
    }

    public WorldcraftServer(int port, Supplier<List<PacketInterceptor>> interceptors)  {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't create socket server on port "+port,e);
        }
        this.interceptors = interceptors;
    }

    public void start()  {
        log.info("Starting socket thread loop");

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Socket acceptedConnection = serverSocket.accept();
                SocketPacketIO io = new SocketPacketIO(acceptedConnection);
                String name = acceptedConnection.getRemoteSocketAddress().toString();

                log.info("New client to server connected: " + name);

                List<PacketInterceptor> clientInterceptors = new ArrayList<>(interceptors.get());
                SendToSocketInterceptor loopback = new SendToSocketInterceptor(io, () -> onClientDisconnect(clientInterceptors));

                WorldcraftThread clientThread = new WorldcraftThread(clientInterceptors, loopback::interceptRawPacket,true);
                SocketPacketSender.configureWoCThread(clientThread,io);
                SocketClient client = new SocketClient(io,name,clientThread::sendClientPacket);
                client.start();
            }
            catch (WorldcraftCommunicationException exp){
                log.error("While accepting connection:");
                log.error(exp.getMessage());
                log.error("Reason:",exp.getCause());
            }
            catch (IOException exp){
                log.error("General IO exception!");
                log.error("Reason:",exp);
            }
            catch (Exception exp){
                log.error("UNKNOWN EXCEPTION",exp);
            }
            // this is thread-locking
        }
    }


    private void onClientDisconnect(List<PacketInterceptor> modulesToClose) {
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
