package com.mikolka9144.Models.EventCodecs;

import com.mikolka9144.Worldcraft.ContentParsers.PacketContentDeserializer;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

public abstract class FullPacketInterceptor extends PacketInterceptor {
    public FullPacketInterceptor(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        switch (packet.getCommand()){
            case S_ROOM_LIST_RESP -> this.InterceptRoomsPacket(packet,
                    PacketContentDeserializer.decodeRoomsData(packet.getData()));
        }
    }
    public void InterceptRoomsPacket(Packet packet, RoomsPacket data) {

    }
}
