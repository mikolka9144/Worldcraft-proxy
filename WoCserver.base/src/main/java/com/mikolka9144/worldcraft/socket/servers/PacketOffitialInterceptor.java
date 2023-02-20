package com.mikolka9144.worldcraft.socket.servers;

import com.mikolka9144.worldcraft.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
@Slf4j
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
            log.info(String.format("Attempting to connect to %s:%d",hostname,port));
            client = new WorldcraftClient(hostname,port,
                    s -> {
                        interceptors.add(new WritebackInterceptor(s, connectionIO));
                        return interceptors;
                    });
            log.info(String.format("Connected to %s:%d",hostname,port));
        } catch (IOException e) {
            log.info(String.format("Couldn't connect to %s:%d",hostname,port));
            log.debug(e.getMessage());
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
        private final WorldCraftPacketIO out;

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
    }
}
