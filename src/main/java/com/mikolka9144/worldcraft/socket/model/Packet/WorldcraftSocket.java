package com.mikolka9144.worldcraft.socket.model.Packet;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class WorldcraftSocket implements Closeable {
    private final com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket channel;
    private final String connectedIp;

    private final Socket rawSocket;

    public WorldcraftSocket(Socket rawSocket) throws IOException {
        channel = new com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket(rawSocket.getInputStream(),rawSocket.getOutputStream());
        connectedIp = rawSocket.getInetAddress().toString();
        this.rawSocket = rawSocket;
    }

    @Override
    public void close() throws IOException {
        rawSocket.close();
    }

    public com.mikolka9144.worldcraft.socket.logic.WorldcraftSocket getChannel() {
        return channel;
    }

    public String getConnectedIp() {
        return connectedIp;
    }
}
