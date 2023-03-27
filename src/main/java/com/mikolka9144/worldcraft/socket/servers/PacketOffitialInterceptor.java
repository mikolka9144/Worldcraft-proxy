package com.mikolka9144.worldcraft.socket.servers;

import com.mikolka9144.worldcraft.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class PacketOffitialInterceptor extends PacketServer {

    private final String hostname;
    private final int port;
    private WorldcraftClient wocClient;
    private List<PacketInterceptor> upstreamInterceptors;
    private List<PacketInterceptor> loopbackInterceptors;


    public PacketOffitialInterceptor(WorldCraftPacketIO client,String hostname,int port) {
        super(client);
        this.hostname = hostname;
        this.port = port;
    }
    @Override
    public void startWritebackConnection(List<PacketInterceptor> interceptors){
        this.upstreamInterceptors = interceptors;
        this.loopbackInterceptors = new ArrayList<>(interceptors);

        try {
            log.info(String.format("Attempting to connect to %s:%d",hostname,port));
            loopbackInterceptors.add(new WritebackInterceptor(client));
            wocClient = new WorldcraftClient(hostname,port,this.loopbackInterceptors,this.upstreamInterceptors);

            log.info(String.format("Connected to %s:%d",hostname,port));
        } catch (IOException e) {
            log.info(String.format("Couldn't connect to %s:%d",hostname,port));
            log.debug(e.getMessage());
        }
    }

    @Override
    public List<PacketInterceptor> GetloopbackInterceptors() {
        return this.loopbackInterceptors;
    }

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        try {
            wocClient.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s to %s failed",packet.getCommand().name(),hostname));
        }
        return new PacketsFormula();
    }
    @Override
    public void close() throws IOException {
        wocClient.close();
    }
}
