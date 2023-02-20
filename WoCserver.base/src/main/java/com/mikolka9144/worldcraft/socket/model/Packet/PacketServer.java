package com.mikolka9144.worldcraft.socket.model.Packet;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;

import java.io.Closeable;
import java.util.List;

public abstract class PacketServer extends PacketInterceptor implements Closeable {
    public PacketServer(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    /**
     * @param interceptors
     * interceptors for a client (creates as a separate list)
     */
    public void setInterceptors(List<PacketInterceptor> interceptors) {
    }
}
