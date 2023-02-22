package com.mikolka9144.worldcraft.socket.model.Packet;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;

import java.io.Closeable;
import java.util.List;
public abstract class PacketServer extends PacketInterceptor implements Closeable {
    protected WorldCraftPacketIO client;

    protected PacketServer(WorldCraftPacketIO client) {

        this.client = client;
    }
    public abstract void startWritebackConnection(List<PacketInterceptor> interceptors);
}
