package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class SendToSocketInterceptor extends PacketAlteringModule {
    private final WorldcraftPacketIO destination;
    private final Closeable connectionLifecycleHolder;

    /**
     * Creates a interceptors for sending packages to a socket
     * @param destination destination socket
     * @param connectionLifecycleHolder object to close once socket disconnects
     */
    public SendToSocketInterceptor(WorldcraftPacketIO destination, Closeable connectionLifecycleHolder){
        this.destination = destination;
        this.connectionLifecycleHolder = connectionLifecycleHolder;
    }

    @SneakyThrows
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        try{
            destination.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s failed. Closing connection.",packet.getCommand().name()));
            connectionLifecycleHolder.close();
        }
        return new PacketsFormula();
    }
}
