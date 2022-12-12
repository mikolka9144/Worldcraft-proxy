package com.mikolka9144.WoCserver.ServerComponents;

import com.mikolka9144.WoCserver.Models.Packet.Packet;
import com.mikolka9144.WoCserver.Models.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.Models.Packet.WorldcraftSocket;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldcraftThreadHandler;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.function.Function;

public class WorldcraftClient extends WorldcraftThreadHandler implements Closeable {
    private final WorldcraftSocket socket;

    public WorldcraftClient(String hostname, int port, Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors) throws IOException {
        socket = new WorldcraftSocket(new Socket(hostname,port));
        List<PacketInterceptor> interceptors1 = interceptors.apply(socket.getChannel());
        attachToThread(socket, interceptors1);
    }
    public void send(Packet packet) throws IOException {
        socket.getChannel().send(packet);
    }
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
