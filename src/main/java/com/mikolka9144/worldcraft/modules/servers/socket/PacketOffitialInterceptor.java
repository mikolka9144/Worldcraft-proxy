package com.mikolka9144.worldcraft.modules.servers.socket;

import com.mikolka9144.worldcraft.common.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.socket.logic.WritebackModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component("packet-official")
public class PacketOffitialInterceptor extends PacketServer {

    private final String hostname;
    private final int port;
    private WorldcraftClient wocClient;
    private List<PacketAlteringModule> loopbackInterceptors;


    public PacketOffitialInterceptor(ServerConfigManifest manifest) {
        this.hostname = manifest.getTargetServer();
        this.port = manifest.getTargetSocketPort();
    }
    @Override
    public void startWritebackConnection(List<PacketAlteringModule> interceptors){
        List<PacketAlteringModule> upstreamInterceptors;
        upstreamInterceptors = interceptors;
        this.loopbackInterceptors = new ArrayList<>(interceptors);

        try {
            log.info(String.format("Attempting to connect to %s:%d",hostname,port));
            loopbackInterceptors.add(new WritebackModule(client));
            wocClient = new WorldcraftClient(hostname,port,this.loopbackInterceptors,upstreamInterceptors);

            log.info(String.format("Connected to %s:%d",hostname,port));
        } catch (IOException e) {
            log.info(String.format("Couldn't connect to %s:%d",hostname,port));
            log.debug(e.getMessage());
        }
    }

    @Override
    public List<PacketAlteringModule> GetloopbackInterceptors() {
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
