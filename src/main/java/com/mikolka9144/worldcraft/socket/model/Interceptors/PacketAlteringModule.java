package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;

/**
 * Base class for any {@code PacketInterceptor} in the server.
 * It is a very basic class and is recomended to use ONLY for low-level interceptors.
 * For everything else inherit from {@link CommandPacketInterceptor}
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
