package com.mikolka9144.worldcraft.socket.api;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.worldcraft.socket.interceptor.PacketAlteringModule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.mikolka9144.worldcraft.socket.server.WorldcraftThread.sendPacket;

/**
 * Class used to communicate with client and server alike.
 */
@Slf4j
public class SocketPacketSender {
    private final List<PacketAlteringModule> clientInterceptors;
    private final List<PacketAlteringModule> serverInterceptors;

    public SocketPacketSender(List<PacketAlteringModule> clientInterceptors, List<PacketAlteringModule> serverInterceptors){
        this.clientInterceptors = clientInterceptors;

        this.serverInterceptors = serverInterceptors;
    }

    /**
     * Sends packet to connected client
     * @param packet packet to send
     */
    public void sendToClient(Packet packet){
        try {
            sendPacket(packet,serverInterceptors,clientInterceptors);
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
            sendPacket(packet,clientInterceptors,serverInterceptors);
        }
        catch (Exception x) {
            log.error("Exception was thrown when attempting to send packet by interceptor to server:", x);
        }
    }
}
