package com.mikolka9144.worldcraft.socket.servers;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public class WritebackInterceptor extends PacketInterceptor {
    private final WorldCraftPacketIO destination;

    public WritebackInterceptor(WorldCraftPacketIO destination){
        this.destination = destination;
    }

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        try {
            destination.send(packet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new PacketsFormula();
    }
}
