package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public abstract class PacketAlteringModule {

    public void setupSockets(SocketPacketSender io){}
    protected PacketAlteringModule(){}
    public PacketsFormula InterceptRawPacket(Packet packet){
        PacketsFormula formula = new PacketsFormula();
        formula.addUpstream(packet);
        return formula;
    }

}
