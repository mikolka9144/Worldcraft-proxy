package com.mikolka9144.Worldcraft.ContentParsers;

import com.mikolka9144.Worldcraft.EventCodecs.RoomsPacket;
import com.mikolka9144.Worldcraft.PacketParsers.PacketDataReader;

public class PacketContentDeserializer{
    public RoomsPacket decodeRoomsData(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
    }
}
