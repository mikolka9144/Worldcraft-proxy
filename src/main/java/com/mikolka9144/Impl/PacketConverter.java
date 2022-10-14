package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.FullPacketInterceptor;
import com.mikolka9144.Models.EventCodecs.Packet;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Worldcraft.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

import java.io.IOException;

public class PacketConverter extends FullPacketInterceptor {
    private PacketProtocol clientProto;
    public PacketConverter(WorldCraftPacketIO connectionIO, PacketProtocol clientProto) {
        super(connectionIO);
        this.clientProto = clientProto;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void InterceptRoomsPacket(Packet packet, RoomsPacket data) {
        packet.setData(PacketContentSerializer.encodeRoomsData(data,clientProto));
    }
}
