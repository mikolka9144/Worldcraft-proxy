package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

/**
 * Base class for any {@code PacketInterceptor} in the server.
 * It is a very basic class and is recomended to use OLNY for low-level interceptors.
 * For everything else inherit from {@link FullPacketInterceptor}
 */
public abstract class PacketAlteringModule {

    public void setupSockets(SocketPacketSender io){}
    protected PacketAlteringModule(){}
    public PacketsFormula InterceptRawPacket(Packet packet){
        PacketsFormula formula = new PacketsFormula();
        formula.addUpstream(packet);
        return formula;
    }

}
