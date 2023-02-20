package com.mikolka9144.worldcraft.socket.model.Packet;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class WorldcraftSocket implements Closeable {
    private final WorldCraftPacketIO channel;
    private final String connectedIp;

    private final Socket rawSocket;

    public WorldcraftSocket(Socket rawSocket) throws IOException {
        channel = new WorldCraftPacketIO(rawSocket.getInputStream(),rawSocket.getOutputStream());
        connectedIp = rawSocket.getInetAddress().toString();
        this.rawSocket = rawSocket;
    }

    @Override
    public void close() throws IOException {
        rawSocket.close();
    }

    public WorldCraftPacketIO getChannel() {
        return channel;
    }

    public String getConnectedIp() {
        return connectedIp;
    }
}
