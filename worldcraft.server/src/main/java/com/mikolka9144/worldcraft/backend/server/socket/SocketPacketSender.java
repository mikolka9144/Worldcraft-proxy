package com.mikolka9144.worldcraft.backend.server.socket;

import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftSocket;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Class used to communicate with client and server alike.
 */
@Slf4j
public class SocketPacketSender {
    private final List<PacketAlteringModule> clientInterceptors;
    private final List<PacketAlteringModule> serverInterceptors;
    private final WorldcraftSocket clientconnection;

    public SocketPacketSender(List<PacketAlteringModule> clientInterceptors, List<PacketAlteringModule> serverInterceptors, WorldcraftSocket clientConnection){
        this.clientInterceptors = clientInterceptors;

        this.serverInterceptors = serverInterceptors;
        this.clientconnection = clientConnection;
    }

    /**
     * Sends packet to connected client
     * @param packet packet to send
     */
    public void sendToClient(Packet packet){
        try {
            WorldcraftThread.sendPacket(packet,serverInterceptors,clientInterceptors);
        }
        catch (Exception x){
            log.error("Exception was thrown when attempting to send packet by interceptor to client:",x);
        }
    }
    /**
     * Sends packet to server as connected client
     * @param packet packet to send
     */
    public void sendToServer(Packet packet){
        try {
            WorldcraftThread.sendPacket(packet,clientInterceptors,serverInterceptors);
        }
        catch (Exception x) {
            log.error("Exception was thrown when attempting to send packet by interceptor to server:", x);
        }
    }

    /**
     * Terminates connection with client
     */
    @SneakyThrows
    public void closeConnection(){
        clientconnection.close();
    }
}
