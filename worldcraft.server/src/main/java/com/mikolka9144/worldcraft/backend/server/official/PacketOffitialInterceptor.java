package com.mikolka9144.worldcraft.backend.server.official;

import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.server.socket.SocketPacketSender;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component("packet-official")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PacketOffitialInterceptor extends PacketAlteringModule {

    private final String hostname;
    private final int port;
    private WorldcraftClient wocClient;
    private SocketPacketSender io;

    public PacketOffitialInterceptor(ServerConfigManifest manifest) {
        this.hostname = manifest.getTargetServer();
        this.port = manifest.getTargetSocketPort();
    }

    @Override
    public void setupSockets(SocketPacketSender io) {
        this.io = io;
        try {
            log.info(String.format("Attempting to connect to %s:%d",hostname,port));
            wocClient = new WorldcraftClient(hostname,port, io::sendToClient);
            wocClient.start();
            log.info(String.format("Connected to %s:%d",hostname,port));
        } catch (IOException e) {
            log.info(String.format("Couldn't connect to %s:%d",hostname,port));
            log.debug(e.getMessage());
        }
    }
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if(packet.getProtoId() == PacketProtocol.SERVER) return super.interceptRawPacket(packet);
        try {
            wocClient.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s to %s failed. Closing connection!",packet.getCommand().name(),hostname));
            io.closeConnection();
        }
        return new PacketsFormula();
    }

    @Override
    public void close() {
        wocClient.close();
    }
}
