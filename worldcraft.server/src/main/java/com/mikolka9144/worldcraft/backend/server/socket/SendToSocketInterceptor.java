package com.mikolka9144.worldcraft.backend.server.socket;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendToSocketInterceptor extends PacketAlteringModule {
    private final WorldcraftPacketIO destination;
    private final Runnable connectionLifecycleHolder;

    /**
     * Creates an interceptors for sending packages to a socket
     * @param destination destination socket
     * @param onConnectionLost action to execute once socket disconnects
     */
    public SendToSocketInterceptor(WorldcraftPacketIO destination, Runnable onConnectionLost){
        this.destination = destination;
        this.connectionLifecycleHolder = onConnectionLost;
    }

    @SneakyThrows
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        try{
            destination.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s failed. Closing connection.",packet.getCommand().name()));
            connectionLifecycleHolder.run();
        }
        return new PacketsFormula();
    }

    @Override
    public void close() {
        // We have nothing to close
    }
}
