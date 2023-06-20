package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket;

import java.io.Closeable;
import java.util.List;
public abstract class PacketServer extends PacketAlteringModule implements Closeable {
    protected WorldcraftSocket client;

    protected PacketServer() {}
    public void supplyIOConnection(WorldcraftSocket client){
        this.client = client;
    }
    public abstract void startWritebackConnection(List<PacketAlteringModule> interceptors);
    public abstract List<PacketAlteringModule> GetloopbackInterceptors();

    @Override
    public void setupSockets(SocketPacketSender io) {
        //IMPORTANT NOTE
        //Server will never have this method called ene
    }
}
