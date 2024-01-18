package com.mikolka9144.worldcraft.backend.client.socket.interceptor;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;

import java.io.Closeable;

/**
 * Base class for any {@code PacketInterceptor} in the server.
 * It is a very basic class and is recomended to use ONLY for low-level interceptors.
 * For everything else inherit from {@link FullPacketInterceptor}
 */
public abstract class PacketInterceptor implements Closeable {

    protected SocketPacketSender io;

    public void setupSockets(SocketPacketSender io) {
        this.io = io;
    }

    protected PacketInterceptor() {
    }

    public PacketsFormula interceptRawPacket(Packet packet) {
        return new PacketsFormula(packet);
    }

    @Override
    public void close() {
        // override this if you need to
    }
}
