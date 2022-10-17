package com.mikolka9144.Impl;

import com.mikolka9144.Models.Packets.FullPacketInterceptor;
import com.mikolka9144.Models.Packets.Packet;
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
}
