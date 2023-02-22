package com.mikolka9144.worldcraft.socket.servers;

import com.mikolka9144.worldcraft.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
@Slf4j
public class PacketOffitialInterceptor extends PacketServer {

    private final String hostname;
    private final int port;
    private WorldcraftClient wocClient;


    public PacketOffitialInterceptor(WorldCraftPacketIO client,String hostname,int port) {
        super(client);
        this.hostname = hostname;
        this.port = port;
    }
    @Override
    public void startWritebackConnection(List<PacketInterceptor> interceptors){
        try {
            log.info(String.format("Attempting to connect to %s:%d",hostname,port));
            wocClient = new WorldcraftClient(hostname,port,
                    s -> {
                        interceptors.add(new WritebackInterceptor(client));
                        return interceptors;
                    });
            log.info(String.format("Connected to %s:%d",hostname,port));
        } catch (IOException e) {
            log.info(String.format("Couldn't connect to %s:%d",hostname,port));
            log.debug(e.getMessage());
        }
    }
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        try {
            wocClient.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PacketsFormula();
    }
    @Override
    public void close() throws IOException {
        wocClient.close();
    }
}
