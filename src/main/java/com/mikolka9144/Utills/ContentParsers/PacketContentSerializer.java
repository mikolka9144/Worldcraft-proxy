package com.mikolka9144.Utills.ContentParsers;

import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Utills.PacketParsers.PacketDataBuilder;

public class PacketContentSerializer {
    public static byte[] encodeRoomsData(RoomsPacket packet, PacketProtocol protoFormat){
        PacketDataBuilder builder = new PacketDataBuilder()
                .append(packet.getPacketIndex())
                .append(packet.getAllPackets())
                .append(packet.getInitialRoomListSize())
                .append(packet.getRoomType());
        for (RoomsPacket.Room room: packet.getRooms()) {
            builder.append(room.getId())
                    .append(room.getName())
                    .append(room.isProtected())
                    .append(room.getActivePlayers())
                    .append(room.getRoomCapacity())
                    .append(room.getNumberOfEntrances())
                    .append(room.getLikes());
            if(protoFormat.equals(PacketProtocol.WORLD_OF_CRAFT_V_3_8_5)){
                builder.append(room.isReadOlny());
            }
        }
        return builder.build();
    }
}
