package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;

import java.io.Closeable;
import java.util.List;

/**
 * {@link PacketServer} is a base class for server-line {@link PacketAlteringModule interceptor}.
 * Main goal of this class is to allow you to make your own implementations of server backend.
 */
public abstract class PacketServer extends PacketAlteringModule implements Closeable {
    protected WorldcraftPacketIO client;

    protected PacketServer() {}
    public void supplyIOConnection(WorldcraftPacketIO client){
        this.client = client;
    }

    /**
     * Initialise server's connection to client.
     * @param interceptors additional interceptors
     */
    public abstract void startWritebackConnection(List<PacketAlteringModule> interceptors);

    /**
     * This method will provide you with list of interceptors used by the server to send packets
     * allowing you to send packets on server behalf.
     * @return list of interceptors (probably with {@link com.mikolka9144.worldcraft.socket.logic.WritebackModule WritebackModule} too)
     */
    public abstract List<PacketAlteringModule> GetloopbackInterceptors();

    /**
     * @deprecated this method is NEVER called for {@code PacketServer}
     */
    @Override
    @Deprecated(forRemoval = false)
    public void setupSockets(SocketPacketSender io) {
        //IMPORTANT NOTE
        //Server will never have this method called ene
    }
}
