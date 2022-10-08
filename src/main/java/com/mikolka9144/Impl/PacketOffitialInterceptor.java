package com.mikolka9144.Impl;

import com.mikolka9144.Impl.Loggers.PacketLogger;
import com.mikolka9144.Models.Packet;
import com.mikolka9144.Models.WorldcraftSocket;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

import java.io.IOException;
import java.net.Socket;

public class PacketOffitialInterceptor extends PacketLogger {

    private final WorldcraftSocket officialIO;

    public PacketOffitialInterceptor(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
        Socket socket = null;
        try {
            socket = new Socket("worldcraft.solverlabs.com",443);
            officialIO = new WorldcraftSocket(socket);
            new Thread(() -> {
                while (true){
                    try {
                        Packet packet = officialIO.getChannel().recive();
                        super.InterceptRawPacket(packet);
                        connectionIO.send(packet);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        try {
            officialIO.getChannel().send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        officialIO.close();
    }
}
