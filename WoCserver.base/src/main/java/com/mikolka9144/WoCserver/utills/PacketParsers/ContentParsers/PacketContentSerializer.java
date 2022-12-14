package com.mikolka9144.WoCserver.utills.PacketParsers.ContentParsers;

import com.mikolka9144.WoCserver.model.EventCodecs.*;
import com.mikolka9144.WoCserver.model.PacketProtocol;
import com.mikolka9144.WoCserver.utills.PacketParsers.PacketDataBuilder;

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
            if(protoFormat.isWorldOfCraft()){
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
    public static byte[] encodeChatMessage(ChatMessage data){
            return new PacketDataBuilder()
                    .append((byte)data.getType().ordinal())
                    .append(data.getMsgTypeArg())
                    .append(data.getMessage())
                    .build();
    }
    public static byte[] encodePlayerMessage(String data) {
        return data.getBytes();
    }
    public static byte[] encodeMovementPacket(MovementPacket data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId());
        writer.append(data.getPosition());
        writer.append(data.getOrientation());
        writer.append(data.getUp());
        return writer.build();
    }
    public static byte[] encodePlaceBlockReq(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getX());
        writer.append(data.getY());
        writer.append(data.getZ());
        writer.append(data.getChunkX());
        writer.append(data.getChunkZ());
        writer.append(data.getBlockType());
        writer.append(data.getBlockData());
        writer.append(data.getPrevBlockType());
        writer.append(data.getPrevBlockData());
        return writer.build();
    }
    public static byte[] encodeServerBlocks(ServerBlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPacketIndex());
        writer.append(data.getAllPackets());
        for (BlockData block : data.getBlocks()) {
            writer.append(block.getX());
            writer.append(block.getY());
            writer.append(block.getZ());
            writer.append(block.getChunkX());
            writer.append(block.getChunkZ());
            writer.append(block.getBlockType());
            writer.append(block.getBlockData());
        }
        return writer.build();
    }
    public static byte[] encodeServerPlaceBlock(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getX());
        writer.append(data.getY());
        writer.append(data.getZ());
        writer.append(data.getChunkX());
        writer.append(data.getChunkZ());
        writer.append(data.getBlockType());
        writer.append(data.getBlockData());
        return writer.build();
    }
    public static byte[] encodeLogin(LoginInfo data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getUsername());
        writer.append(data.getSkinId());
        writer.append(data.getClientVer());
        writer.append(data.getDeviceId());
        writer.append(data.getDeviceName());
        writer.append(data.getAndroidVer());
        writer.append(data.getAndroidAPI());
        writer.append(data.getMarketName());
        return writer.build();
    }
    public static byte[] decodeRoomsReq(RoomListRequest data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append((byte)data.getRoomsType().ordinal());
        writer.append(data.getStartingIndex());
        return writer.build();
    }
    public static byte[] encodeEnemyAction(PlayerAction data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId());
        writer.append((byte) data.getActionType().ordinal());
        return writer.build();
    }
}
