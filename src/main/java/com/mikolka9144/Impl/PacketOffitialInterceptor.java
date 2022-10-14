package com.mikolka9144.Impl;

import com.mikolka9144.Models.Packets.Packet;
import com.mikolka9144.Models.Packets.PacketInterceptor;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;
import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PacketOffitialInterceptor extends PacketInterceptor {

    private final WorldcraftClient client;

    public PacketOffitialInterceptor(WorldCraftPacketIO connectionIO,
                                     Function<WorldCraftPacketIO, List<PacketInterceptor>> clientInterceptors) {
        super(connectionIO);
        try {
            client = new WorldcraftClient("worldcraft.solverlabs.com",443,
                    s -> {
                        List<PacketInterceptor> ret = new ArrayList<>(clientInterceptors.apply(s));
                        ret.add(new WritebackInterceptor(s, connectionIO));
                        return ret;
                    });

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
    private static class WritebackInterceptor extends PacketInterceptor{
        private WorldCraftPacketIO out;

        public WritebackInterceptor(WorldCraftPacketIO in, WorldCraftPacketIO out){
            super(in);
            this.out = out;
        }

        @Override
        public void InterceptRawPacket(Packet packet) {
            try {
                out.send(packet);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws IOException {

        }
    }
}
