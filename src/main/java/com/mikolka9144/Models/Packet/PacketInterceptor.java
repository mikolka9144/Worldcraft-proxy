package com.mikolka9144.Models.Packet;

import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.Closeable;

public abstract class PacketInterceptor implements Closeable {
    protected WorldCraftPacketIO connectionIO;

    public PacketInterceptor(WorldCraftPacketIO connectionIO){

        this.connectionIO = connectionIO;
    }
    public void InterceptRawPacket(Packet packet){

    }


}
