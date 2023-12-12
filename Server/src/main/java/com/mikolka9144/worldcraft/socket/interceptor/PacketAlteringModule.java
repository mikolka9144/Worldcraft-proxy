package com.mikolka9144.worldcraft.socket.interceptor;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.socket.api.SocketPacketSender;

import java.io.Closeable;

/**
 * Base class for any {@code PacketInterceptor} in the server.
 * It is a very basic class and is recomended to use ONLY for low-level interceptors.
 * For everything else inherit from {@link CommandPacketInterceptor}
 */
public abstract class PacketAlteringModule implements Closeable {

    public void setupSockets(SocketPacketSender io){}
    protected PacketAlteringModule(){}
    public PacketsFormula interceptRawPacket(Packet packet){
        PacketsFormula formula = new PacketsFormula();
        formula.addUpstream(packet);
        return formula;
    }
    @Override
    public void close() {
        // override this if you need to
    }
}
