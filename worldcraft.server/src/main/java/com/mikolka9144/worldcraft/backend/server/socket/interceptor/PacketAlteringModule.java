package com.mikolka9144.worldcraft.backend.server.socket.interceptor;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.server.socket.SocketPacketSender;

import java.io.Closeable;

/**
 * Base class for any {@code PacketInterceptor} in the server.
 * It is a very basic class and is recomended to use ONLY for low-level interceptors.
 * For everything else inherit from {@link CommandPacketInterceptor}
 */
public abstract class PacketAlteringModule implements Closeable {

    public void setupSockets(SocketPacketSender io) {
    }

    protected PacketAlteringModule() {
    }

    public PacketsFormula interceptRawPacket(Packet packet) {
        return new PacketsFormula(packet);
    }

    @Override
    public void close() {
        // override this if you need to
    }
}
