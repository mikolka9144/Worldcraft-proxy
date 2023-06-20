package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WritebackModule extends PacketAlteringModule {
    private final WorldcraftSocket destination;

    public WritebackModule(WorldcraftSocket destination){
        this.destination = destination;
    }

    @SneakyThrows
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        destination.send(packet);
        return new PacketsFormula();
    }
}
