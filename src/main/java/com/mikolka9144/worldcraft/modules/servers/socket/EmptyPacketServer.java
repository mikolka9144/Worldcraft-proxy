package com.mikolka9144.worldcraft.modules.servers.socket;

import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Interceptors.SendToSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component("packet-empty")
public class EmptyPacketServer extends PacketServer {
    private List<PacketAlteringModule> connection;
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        return new PacketsFormula();
    }
    @Override
    public void startWritebackConnection(List<PacketAlteringModule> interceptors) {
        connection = new ArrayList<>(interceptors);
        log.info("Mocking server init");
        connection.add(new SendToSocketInterceptor(client,this));
    }

    @Override
    public List<PacketAlteringModule> GetloopbackInterceptors() {
        return connection;
    }

    @Override
    public void close() {
        log.info("Closing fake server");
    }
}
