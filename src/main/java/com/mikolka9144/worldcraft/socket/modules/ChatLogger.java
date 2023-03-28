package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatLogger extends FullPacketInterceptor {
    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        log.info("$ "+message);
        super.interceptPlayerMessage(packet, message, formula);
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        log.info(data.getMessage());
        super.interceptChatMessage(packet, data, formula);
    }
}
