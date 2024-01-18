package com.mikolka9144.worldcraft.backend.client.socket;

import com.mikolka9144.worldcraft.backend.client.socket.interceptor.PacketInterceptor;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.SocketPacketSender;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import static com.mikolka9144.worldcraft.backend.client.socket.SocketClient.getIoStream;

public class WorldcraftClient implements Closeable {
    private final SocketPacketIO target;
    private final SocketClient client;
    private final WorldcraftThread clientThread
            ;

    /**
     * @param hostname        Name (or IP) of target server
     * @param port            A port to hit on target {@code hostname}
     * @throws WorldcraftCommunicationException Thrown if client fails to open connection to target server
     * @throws IOException General connectivity exception. Could be LITERALLY EVERYTHING
     */
    public WorldcraftClient(String hostname, int port, List<PacketInterceptor> interceptorList) throws WorldcraftCommunicationException, IOException {
        this(getIoStream(hostname, port),hostname,interceptorList);
    }



    /**
     * @param io              Connection (real or fake) to operate on
     * @param connectionName  Name (or IP) of connected target
     */
    public WorldcraftClient(SocketPacketIO io, String connectionName, List<PacketInterceptor> interceptorList) {
        target = io;
        clientThread = new WorldcraftThread(interceptorList, s -> {
            try {
                io.send(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },false);
        SocketPacketSender.configureWoCThread(clientThread, io);
        client = new SocketClient(target, connectionName,clientThread::sendServerPacket);
    }
    /**
     * Starts receiving packets from server. As simple as that.
     */
    public void start(){
        client.start();
    }
    /**
     * Sends packet to Server
     *
     * @param packet A packet to send
     * @throws IOException Sending packet to server fails
     */
    public void send(Packet packet) throws IOException {
        clientThread.sendServerPacket(packet);
    }

    @Override
    public void close() throws IOException {
        target.close();
    }
}
