package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;

public abstract class PacketInterceptor  {
    protected WorldCraftPacketIO connectionIO;

    public PacketInterceptor(WorldCraftPacketIO connectionIO){

        this.connectionIO = connectionIO;
    }
    public void InterceptRawPacket(Packet packet){

    }


}
