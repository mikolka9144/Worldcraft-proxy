package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Interceptors.SendToSocketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component("direct-packet-server")
public class DirectPacketServer extends PacketServer {
    private List<PacketAlteringModule> connection;

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        return new PacketsFormula();
    }

    @Override
    public void startWritebackConnection(List<PacketAlteringModule> interceptors) {
        connection = new ArrayList<>(interceptors);
        connection.add(new SendToSocketInterceptor(client,this));
    }

    @Override
    public List<PacketAlteringModule> GetloopbackInterceptors() {
        return connection;
    }

    @Override
    public void close(){
        // We have nothing to close
    }
}
