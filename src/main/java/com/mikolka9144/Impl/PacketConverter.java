package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;

public class PacketConverter extends FullPacketInterceptor {

    public PacketConverter(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void interceptRoomsPacket(Packet packet, RoomsPacket data) {
        packet.setData(PacketContentSerializer.encodeRoomsData(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data) {
        packet.setData(PacketContentSerializer.encodeChatMessage(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
    }
}
