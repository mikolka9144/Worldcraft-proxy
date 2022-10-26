package com.mikolka9144.Utills.ContentParsers;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.EventCodecs.PurchaseValidationReq;
import com.mikolka9144.Models.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Models.PacketProtocol;
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
    public static byte[] encodeValidatePurchaseReq(PurchaseValidationReq data) {
        return new PacketDataBuilder()
                .append(data.getPurchaseName())
                .append(data.getBundleId())
                .append(data.getReceipt())
                .build();
    }
    public static byte[] encodeValidatePurchaseResp(PurchaseValidationResp data){
        return new PacketDataBuilder()
                .append(data.getStatus().ordinal())
                .append(data.getReceipt())
                .build();
    }
    public static byte[] encodeChatMessage(ChatMessage data,PacketProtocol protoFormat){
        switch (protoFormat){
            case WORLDCRAFT_V_2_8_7,WORLDCRAFT_V_2_7_4 -> {
                return data.getMessage().getBytes();
            }
            default -> {
                return new PacketDataBuilder()
                        .append((byte)data.getType().ordinal())
                        .append(data.getMsgTypeArg())
                        .append(data.getMessage())
                        .build();
            }
        }

    }
}
