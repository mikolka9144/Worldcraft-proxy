package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class SendToSocketInterceptor extends PacketAlteringModule {
    private final WorldcraftPacketIO destination;

    public SendToSocketInterceptor(WorldcraftPacketIO destination){
        this.destination = destination;
    }

    @SneakyThrows
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        try{
            destination.send(packet);
        } catch (IOException e) {
            log.error(String.format("Sending packet %s failed",packet.getCommand().name()));
        }
        return new PacketsFormula();
    }
}
