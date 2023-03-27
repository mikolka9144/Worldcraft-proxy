package com.mikolka9144.worldcraft.socket.servers;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class EmptyPacketServer extends PacketServer {
    protected EmptyPacketServer(WorldCraftPacketIO client) {
        super(client);
    }
    public EmptyPacketServer(){
        super(null);
    }

    @Override
    public void startWritebackConnection(List<PacketInterceptor> interceptors) {
        log.info("Mocking server init");
    }

    @Override
    public List<PacketInterceptor> GetloopbackInterceptors() {
        return new ArrayList<>();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing fake server");
    }
}
