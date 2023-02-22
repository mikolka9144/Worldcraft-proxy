package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
@Slf4j
public class PacketLogger extends PacketInterceptor {

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        log.info("--------------------------------");
        log.info("Proto: "+packet.getProtoId());
        log.info("Error code: "+packet.getError());
        log.info("Command: "+packet.getCommand());
        log.info("PlayerId: "+packet.getPlayerId());
        log.info("Msg: "+packet.getMessage());
        log.info("Data: "+ Arrays.toString(packet.getData()));
        log.info("--------------------------------");
        return super.InterceptRawPacket(packet);
    }

}
