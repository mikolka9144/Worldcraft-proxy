package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.socket.logic.WorldcraftThread;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.SendToSocketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WorldcraftClient implements Closeable {
    @Getter
    private final WorldcraftSocket socket;
    private final WorldcraftThread thread;

    public WorldcraftClient(String hostname, int port, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> writebackInterceptors) throws IOException {
        this(new WorldcraftSocket(new Socket(hostname,port)),upstreamInterceptors,writebackInterceptors);
    }
    public WorldcraftClient(WorldcraftSocket io, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> writebackInterceptors){
        socket = io;
        writebackInterceptors.add(new SendToSocketInterceptor(io.getChannel()));
        thread = new WorldcraftThread(socket, upstreamInterceptors,writebackInterceptors);
        thread.attachToThread().start();
    }
    public void send(Packet packet) {
        thread.sendPacket(packet);
    }
    @Override
    public void close() throws IOException {
        thread.stopThread();
        socket.close();
    }
}
