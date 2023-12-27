package com.mikolka9144.worldcraft.backend.server.socket.interceptor;

import com.mikolka9144.worldcraft.backend.client.api.PacketBuilder;
import com.mikolka9144.worldcraft.backend.client.api.PacketCommandResolver;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.client.socket.PacketCommands;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class containing basic packet recognition for use as a base for {@code interceptors}.
 * In addition, it also initialises {@link PacketBuilder packager}, but only after it receives playerId from one of packets.
 */
@Slf4j
public abstract class CommandPacketInterceptor extends PacketAlteringModule implements PacketCommands {

    protected PacketBuilder packager = null;
    private final PacketCommandResolver resolver;

    protected CommandPacketInterceptor() {
        resolver = new PacketCommandResolver(this);
    }

    // first packet is added by default
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        configurePacketer(packet);
        return resolver.executeCommand(packet);
    }

    private void configurePacketer(Packet packet) {
        if (packager == null) {
            if (packet.getProtoId() != PacketProtocol.SERVER) {
                this.packager = new PacketBuilder(packet.getProtoId());
            }
            return;
        }
        if (packet.getPlayerId() != 0 && packager.getPlayerId() == 0) {
            packager.setPlayerId(packet.getPlayerId());
        }
    }
}