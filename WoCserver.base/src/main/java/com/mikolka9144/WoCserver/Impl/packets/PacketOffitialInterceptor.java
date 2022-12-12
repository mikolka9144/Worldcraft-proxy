package com.mikolka9144.WoCserver.Impl.packets;

import com.mikolka9144.WoCserver.Models.Packet.Packet;
import com.mikolka9144.WoCserver.Models.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.Models.Packet.PacketServer;
import com.mikolka9144.WoCserver.ServerComponents.WorldcraftClient;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketOffitialInterceptor extends PacketServer {

    private final String hostname;
    private final int port;
    private WorldcraftClient client;

    public PacketOffitialInterceptor(WorldCraftPacketIO connectionIO,String hostname,int port) {
        super(connectionIO);
        this.hostname = hostname;
        this.port = port;
    }
    public void setInterceptors(List<PacketInterceptor> interceptors){
        try {
            client = new WorldcraftClient(hostname,port,
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
