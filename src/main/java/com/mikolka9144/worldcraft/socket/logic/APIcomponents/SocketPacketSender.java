package com.mikolka9144.worldcraft.socket.logic.APIcomponents;

import com.mikolka9144.worldcraft.socket.logic.WorldcraftThreadHandler;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Class used to communicate with client and server alike.
 */
@Slf4j
public class SocketPacketSender extends WorldcraftThreadHandler {
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
