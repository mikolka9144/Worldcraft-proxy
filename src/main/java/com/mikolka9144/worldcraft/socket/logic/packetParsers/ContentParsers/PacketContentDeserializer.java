package com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers;

import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PacketContentDeserializer{
    public static RoomsPacket decodeRoomsData(byte[] data){
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
    }
    @SneakyThrows
    public static ClientVersion decodeVersionCheckReq(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        return new ClientVersion(
                reader.getString(),
                reader.getInt());
    }
    public static PurchaseValidationReq decodeValidatePurchaseReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PurchaseValidationReq(
                reader.getString(),
                reader.getString(),
                reader.getString());
    }
    public static PurchaseValidationResp decodeValidatePurchaseResp(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PurchaseValidationResp(
                PurchaseValidationResp.Status.values()[reader.getInt()],
                reader.getString());
    }
    public static ChatMessage decodeChatMessage(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        byte msgTypeBit = reader.getByte();
        return new ChatMessage(
                reader.getString(),
                reader.getString(),
                ChatMessage.MsgType.values()[msgTypeBit]);
    }

    public static String decodePlayerMessage(byte[] data) {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(data)).toString();
    }

    public static MovementPacket decodeMovementPacket(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new MovementPacket(reader.getInt(),
                reader.getVector3(),
                reader.getVector3(),
                reader.getVector3()
        );
    }

    @SneakyThrows
    public static BlockData decodePlaceBlockReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);

            return new BlockData(reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    BlockData.BlockType.findBlockById(reader.getByte()),
                    reader.getByte(),
                    reader.getByte(),
                    reader.getByte());

    }

    public static ServerBlockData decodeServerBlocks(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        int curPacket = reader.getInt();
        int packetCount = reader.getInt();
        List<BlockData> blocks = new ArrayList<>();
        while (reader.hasNext(10)){
            blocks.add(new BlockData(reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    BlockData.BlockType.findBlockById(reader.getByte()),
                    reader.getByte()));
            reader = new PacketDataReader(reader.getBytes());
        }
        return new ServerBlockData(curPacket,packetCount,blocks);
    }

    public static BlockData decodeServerPlaceBlock(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new BlockData(reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                BlockData.BlockType.findBlockById(reader.getByte()),
                reader.getByte());
    }

    public static LoginInfo decodeLogin(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new LoginInfo(
                reader.getString(),
                reader.getShort(),
                reader.getString(),
                reader.getString(),
                reader.getString(), reader.getString(), reader.getString(), reader.getString());

    }

    public static RoomListRequest decodeRoomsReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new RoomListRequest(RoomListRequest.RoomsType.values()[reader.getByte()], reader.getInt());
    }

    public static PlayerAction decodeEnemyAction(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PlayerAction(reader.getInt(), PlayerAction.ActionType.values()[reader.getByte()]);
    }

    public static LoginResponse decodeLoginResponse(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new LoginResponse(reader.getInt(), reader.getString(), reader.getBoolean());
    }
    public static int decodePlayerDisconnect(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        return reader.getInt();
    }

    public static JoinRoomRequest decodeJoinRoomRequest(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new JoinRoomRequest(reader.getString(), reader.getString(), reader.getBoolean());
    }

    public static PopupMessage decodePopupMessage(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PopupMessage(PopupMessage.PopupMessageType.findByValue(reader.getByte()), reader.getLong(), reader.getString(), reader.getString());
    }
}