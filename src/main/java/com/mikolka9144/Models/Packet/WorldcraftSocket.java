package com.mikolka9144.Models.Packet;

import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class WorldcraftSocket implements Closeable {
    private final WorldCraftPacketIO channel;
    private Socket rawSocket;

    public WorldcraftSocket(Socket rawSocket) throws IOException {
        channel = new WorldCraftPacketIO(rawSocket.getInputStream(),rawSocket.getOutputStream());
        this.rawSocket = rawSocket;
    }

    @Override
    public void close() throws IOException {
        rawSocket.close();
    }

    public WorldCraftPacketIO getChannel() {
        return channel;
    }
}
