package com.mikolka9144.WoCserver.utills;

import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.model.Packet.PacketCommand;
import com.mikolka9144.WoCserver.model.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.model.PacketProtocol;
import com.mikolka9144.WoCserver.utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.WoCserver.model.EventCodecs.ChatMessage;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

import java.io.IOException;

public class Packeter {
    private final WorldCraftPacketIO client;
    private final PacketInterceptor server;
    private final PacketProtocol clientProto;

    public Packeter(WorldCraftPacketIO client, PacketInterceptor server,PacketProtocol clientProto) {
        this.client = client;
        this.server = server;
        this.clientProto = clientProto;
    }

    public void println(String text) {
        if(clientProto.isWorldOfCraft()){
            text = "[FFFFFF]"+text;
        }
        var chatMsg = new ChatMessage(text, text, ChatMessage.MsgType.PLAYER_JOINED);
        try {
            client.send(new Packet(PacketProtocol.SERVER, 0, PacketCommand.SB_CHAT_MSG, "", (byte) 0,
                    PacketContentSerializer.encodeChatMessage(chatMsg)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
