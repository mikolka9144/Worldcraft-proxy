package com.mikolka9144.worldcraft.socket;

import com.mikolka9144.worldcraft.socket.logic.WorldcraftThreadHandler;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WorldcraftClient extends WorldcraftThreadHandler implements Closeable {
    private final WorldcraftSocket socket;

    public WorldcraftClient(String hostname, int port, List<PacketAlteringModule> interceptors, List<PacketAlteringModule> loopbackInterceptors) throws IOException {
        socket = new WorldcraftSocket(new Socket(hostname,port));

        attachToThread(socket, interceptors,loopbackInterceptors);
    }
    public void send(Packet packet) throws IOException {
        socket.getChannel().send(packet);
    }
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
