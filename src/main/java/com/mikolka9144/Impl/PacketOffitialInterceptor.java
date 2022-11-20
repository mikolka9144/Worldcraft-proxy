package com.mikolka9144.Impl;

import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.Packet.PacketServer;
import com.mikolka9144.Worldcraft.ServerComponents.WorldcraftClient;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketOffitialInterceptor extends PacketServer {

    private WorldcraftClient client;

    public PacketOffitialInterceptor(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }
    public void setInterceptors(List<PacketInterceptor> interceptors){
        try {
            client = new WorldcraftClient("64.237.54.60",443,
                    s -> {
                        List<PacketInterceptor> ret = new ArrayList<>(interceptors);
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
