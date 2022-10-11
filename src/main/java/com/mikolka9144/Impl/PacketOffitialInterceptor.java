package com.mikolka9144.Impl;

import com.mikolka9144.Impl.Loggers.PacketLogger;
import com.mikolka9144.Models.EventCodecs.Packet;
import com.mikolka9144.Models.PacketInterceptor;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;
import com.mikolka9144.WorldcraftClient;

import java.io.IOException;
import java.util.List;

public class PacketOffitialInterceptor extends PacketInterceptor {

    private final WorldcraftClient client;

    public PacketOffitialInterceptor(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
        try {
            client = new WorldcraftClient("worldcraft.solverlabs.com",443,
                    s -> List.of(
                            new PacketLogger(s),
                            new PacketConverter(s),
                            new PacketLogger(s),
                            new WritebackInterceptor(s,connectionIO)
                    ));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        try {
            client.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void close() throws IOException {
        client.close();
    }
    private class WritebackInterceptor extends PacketInterceptor{
        private WorldCraftPacketIO out;

        public WritebackInterceptor(WorldCraftPacketIO in, WorldCraftPacketIO out){
            super(in);
            this.out = out;
        }

        @Override
        public void InterceptRawPacket(Packet packet) {
            try {
                out.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {

        }
    }
}
