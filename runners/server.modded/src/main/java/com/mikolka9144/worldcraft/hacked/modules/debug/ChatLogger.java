package com.mikolka9144.worldcraft.hacked.modules.debug;

import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component("chat-logger")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChatLogger extends CommandPacketInterceptor {
    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        //Sonarlint suggests to log chat message instead of whrowing it to the console
        //But this way it looks better
        System.out.println("$ "+message);
        super.interceptPlayerMessage(packet, message, formula);
    }



    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        log.info(data.getMessage());
        super.interceptChatMessage(packet, data, formula);
    }
}
