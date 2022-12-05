package com.mikolka9144.Utills;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketCommand;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

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
                    PacketContentSerializer.encodeChatMessage(chatMsg,clientProto)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
