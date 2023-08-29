package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WritebackModule extends PacketAlteringModule {
    private final WorldcraftPacketIO destination;

    public WritebackModule(WorldcraftPacketIO destination){
        this.destination = destination;
    }

    @SneakyThrows
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        destination.send(packet);
        return new PacketsFormula();
    }
}
