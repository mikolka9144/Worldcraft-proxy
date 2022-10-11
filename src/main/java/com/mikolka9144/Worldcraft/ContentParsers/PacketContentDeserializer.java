package com.mikolka9144.Worldcraft.ContentParsers;

import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Worldcraft.PacketParsers.PacketDataReader;

import java.io.IOException;

public class PacketContentDeserializer{
    public static RoomsPacket decodeRoomsData(byte[] data){
        try {
            PacketDataReader reader = new PacketDataReader(data);
            RoomsPacket out = new RoomsPacket(
                    reader.getInt(),
                    reader.getInt(),
                    reader.getShort(),
                    reader.getByte());
            while (reader.hasNext(20)){
                RoomsPacket.Room room = new RoomsPacket.Room(
                        reader.getInt(),
                        reader.getString(),
                        reader.getBoolean(),
                        reader.getShort(),
                        reader.getShort(),
                        reader.getInt(),
                        reader.getInt(),
                        reader.getBoolean());
                out.getRooms().add(room);
            }
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
