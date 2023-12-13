package com.mikolka9144.worldcraft.modules;

import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.packet.packet.codecs.ChatMessage;
import com.mikolka9144.packet.packet.codecs.JoinRoomResponse;
import com.mikolka9144.packet.packet.codecs.Player;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.enums.PacketCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("join-log")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class JoinLogInterceptor extends CommandPacketInterceptor {
    private final Map<Integer,String> cachePlayers = new HashMap<>();

    @Override
    public void interceptJoinRoomResp(Packet packet, JoinRoomResponse joinRoomResponse, PacketsFormula formula) {
        // setting up
        cachePlayers.clear();
    }

    @Override
    public void interceptPlayerList(Packet packet, List<Player> players, PacketsFormula formula) {
            players.forEach(p -> cachePlayers.put(p.getId(),p.getNickname()) );
            log.info("Finished setting up player list");
    }

    @Override
    public void interceptPlayerJoinedInfo(Packet packet, Player player, PacketsFormula formula) {
        cachePlayers.put(player.getId(), player.getNickname());
        String message = player.getNickname()+" joined the room";
        ChatMessage msg = new ChatMessage(
                player.getNickname(),
                message,
                ChatMessage.MsgType.PLAYER_JOINED
        );
        formula.addUpstream(
                packager.serverPacket(PacketCommand.SB_CHAT_MSG, PacketDataEncoder.chatMessage(msg))
        );
    }

    @Override
    public void interceptPlayerDisconnect(Packet packet, int playerId, PacketsFormula formula) {
        if (!cachePlayers.containsKey(playerId)){
            log.error("PLAYER ID "+playerId+" NOT FOUND");
            return;
        }
        String nickname = cachePlayers.get(playerId);
        String message = nickname+" left the room";
        ChatMessage msg = new ChatMessage(
                nickname,
                message,
                ChatMessage.MsgType.PLAYER_LEFT
        );
        formula.addUpstream(
            packager.serverPacket(PacketCommand.SB_CHAT_MSG, PacketDataEncoder.chatMessage(msg))
        );
        cachePlayers.remove(playerId);
    }
}
