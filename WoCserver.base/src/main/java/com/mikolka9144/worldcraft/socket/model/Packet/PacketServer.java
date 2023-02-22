package com.mikolka9144.worldcraft.socket.model.Packet;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import lombok.Getter;

import java.io.Closeable;
import java.util.List;
public abstract class PacketServer extends PacketInterceptor implements Closeable {
    private WorldCraftPacketIO client;

    public PacketServer(WorldCraftPacketIO client) {

        this.client = client;
    }
}
