package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.socket.logic.WorldcraftThread;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.SendToSocketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket;
import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WorldcraftClient implements Closeable {
    @Getter
    private final WorldcraftSocket socket;
    private final WorldcraftThread thread;
    private final List<PacketAlteringModule> upstreamInterceptors;
    private final List<PacketAlteringModule> writebackInterceptors;

    public WorldcraftClient(String hostname, int port, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> writebackInterceptors) throws IOException {
        this(new WorldcraftSocket(new Socket(hostname,port)),upstreamInterceptors,writebackInterceptors);

    }
    public WorldcraftClient(WorldcraftSocket io, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> writebackInterceptors){
        socket = io;
        this.upstreamInterceptors = upstreamInterceptors;
        this.writebackInterceptors = writebackInterceptors;
        writebackInterceptors.add(new SendToSocketInterceptor(io.getChannel(), this));
        thread = new WorldcraftThread(socket, upstreamInterceptors,writebackInterceptors);
        thread.attachToThread().start();
    }
    public void send(Packet packet) {
        WorldcraftThread.sendPacket(packet,writebackInterceptors,upstreamInterceptors);
    }
    @Override
    public void close() throws IOException {
        thread.close();
        socket.close();
    }
}
