package com.mikolka9144.Models.Packet;

import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.util.List;

public abstract class PacketServer extends PacketInterceptor {
    public PacketServer(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    public void setInterceptors(List<PacketInterceptor> interceptors) {
    }
}
