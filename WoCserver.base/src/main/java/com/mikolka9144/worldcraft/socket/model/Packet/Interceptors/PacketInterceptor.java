package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public abstract class PacketInterceptor  {

    protected PacketInterceptor(){}
    public abstract PacketsFormula InterceptRawPacket(Packet packet);

}
