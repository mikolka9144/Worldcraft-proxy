package com.mikolka9144.worldcraft.modules.servers.socket;

import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component("packet-empty")
public class EmptyPacketServer extends PacketServer {
    public EmptyPacketServer(){}

    @Override
    public void startWritebackConnection(List<PacketAlteringModule> interceptors) {
        log.info("Mocking server init");
    }

    @Override
    public List<PacketAlteringModule> GetloopbackInterceptors() {
        return new ArrayList<>();
    }

    @Override
    public void close() {
        log.info("Closing fake server");
    }
}
