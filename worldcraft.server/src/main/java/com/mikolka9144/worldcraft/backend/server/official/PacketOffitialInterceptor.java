package com.mikolka9144.worldcraft.backend.server.official;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.client.socket.SocketClient;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.SocketPacketSender;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.PacketInterceptor;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.mikolka9144.worldcraft.backend.client.socket.SocketClient.getIoStream;

@Slf4j
@Component("packetServerSender")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PacketOffitialInterceptor extends PacketInterceptor {

    private final String hostname;
    private final int port;
    private SocketClient wocClient;

    public PacketOffitialInterceptor(ServerConfigManifest manifest) {
        this.hostname = manifest.obtainOption("server.target.address");
        this.port = Integer.parseInt(manifest.obtainOption("server.target.socketPort"));
    }

    @Override
    public void setupSockets(SocketPacketSender io) {
        try {
            log.info(String.format("Attempting to connect to %s:%d", hostname, port));
            wocClient = new SocketClient(getIoStream(hostname, port),hostname, io::sendToClient);
            wocClient.start();
            log.info(String.format("Connected to %s:%d", hostname, port));
        } catch (WorldcraftCommunicationException|IOException e) {
            log.error(String.format("Couldn't connect to %s:%d", hostname, port));
            log.error(e.getMessage());
        }
        super.setupSockets(io);
    }

    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if (packet.getProtoId() == PacketProtocol.SERVER) return super.interceptRawPacket(packet);
        try {
            wocClient.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s to %s failed. Closing connection!", packet.getCommand().name(), hostname));
            io.closeConnection();
        }
        return new PacketsFormula();
    }

    @Override
    public void close() {
        wocClient.close();
    }
}
