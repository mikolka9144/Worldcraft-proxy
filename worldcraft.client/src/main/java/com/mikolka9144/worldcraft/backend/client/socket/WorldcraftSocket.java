package com.mikolka9144.worldcraft.backend.client.socket;

import lombok.Getter;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WorldcraftSocket implements Closeable {
    @Getter
    private final WorldcraftPacketIO channel;
    @Getter
    private final String connectedIp;

    private final Closeable rawSocket;

    public WorldcraftSocket(Socket rawSocket) throws IOException {
        this(rawSocket.getInputStream(), rawSocket.getOutputStream(), rawSocket.getInetAddress().toString(), rawSocket);
    }

    public WorldcraftSocket(InputStream input, OutputStream output, String clientName, Closeable lifecycleObject) { //NOTE remove lifecycleObject
        channel = new WorldcraftPacketIO(input, output);
        connectedIp = clientName;
        this.rawSocket = lifecycleObject;
    }

    @Override
    public void close() throws IOException {
        rawSocket.close();
    }

}
