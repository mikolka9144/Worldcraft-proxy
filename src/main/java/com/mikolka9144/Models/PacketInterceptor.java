package com.mikolka9144.Models;

import com.mikolka9144.Models.EventCodecs.Packet;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

import java.io.Closeable;

public abstract class PacketInterceptor implements Closeable {
    protected WorldCraftPacketIO connectionIO;

    public PacketInterceptor(WorldCraftPacketIO connectionIO){

        this.connectionIO = connectionIO;
    }
    public void InterceptRawPacket(Packet packet){

    }

    public void InterceptRoomsPacket(Packet packet,RoomsPacket data) {

    }
}
