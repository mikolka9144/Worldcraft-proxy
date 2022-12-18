package com.mikolka9144.WoCserver.model.Packet.Interceptors;

import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.Packet.Packet;

import java.io.Closeable;

public abstract class PacketInterceptor implements Closeable {
    protected WorldCraftPacketIO connectionIO;

    public PacketInterceptor(WorldCraftPacketIO connectionIO){

        this.connectionIO = connectionIO;
    }
    public void InterceptRawPacket(Packet packet){

    }


}
