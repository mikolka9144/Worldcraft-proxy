package com.mikolka9144.WoCserver.model.Packet;

import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;

import java.util.List;

public abstract class PacketServer extends PacketInterceptor {
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
