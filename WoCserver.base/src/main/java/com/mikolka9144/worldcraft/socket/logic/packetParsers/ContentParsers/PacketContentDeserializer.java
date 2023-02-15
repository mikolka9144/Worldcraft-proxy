package com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketDataReader;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    public static PurchaseValidationReq decodeValidatePurchaseReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PurchaseValidationReq(
                reader.getString(),
                reader.getString(),
                reader.getString());
    }
    public static PurchaseValidationResp decodeValidatePurchaseResp(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        try {
            return new PurchaseValidationResp(
                    PurchaseValidationResp.Status.values()[reader.getInt()],
                    reader.getString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return new MovementPacket(reader.getInt(),
                    reader.getVector3(),
                    reader.getVector3(),
                    reader.getVector3()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BlockData decodePlaceBlockReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        try {
            return new BlockData(reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getByte(),
                    reader.getByte(),
                    reader.getByte(),
                    reader.getByte());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerBlockData decodeServerBlocks(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        try {
            int curPacket = reader.getInt();
            int packetCount = reader.getInt();
            List<BlockData> blocks = new ArrayList<>();
            while (reader.hasNext(10)){
                blocks.add(new BlockData(reader.getShort(),
                        reader.getShort(),
                        reader.getShort(),
                        reader.getShort(),
                        reader.getShort(),
                        reader.getByte(),
                        reader.getByte()));
                reader = new PacketDataReader(reader.getBytes());
            }
            return new ServerBlockData(curPacket,packetCount,blocks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BlockData decodeServerPlaceBlock(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        try {
            return new BlockData(reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getByte(),
                    reader.getByte());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LoginInfo decodeLogin(byte[] data) {
        try {
            PacketDataReader reader = new PacketDataReader(data);
                return new LoginInfo(
                        reader.getString(),
                        reader.getShort(),
                        reader.getString(),
                        reader.getString(),
                        reader.getString(), reader.getString(), reader.getString(), reader.getString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static RoomListRequest decodeRoomsReq(byte[] data) {
        try {
            PacketDataReader reader = new PacketDataReader(data);
            return new RoomListRequest(RoomListRequest.RoomsType.values()[reader.getByte()], reader.getInt());
        } catch (IOException x){
            throw new RuntimeException(x);
        }
    }

    public static PlayerAction decodeEnemyAction(byte[] data) {
        try {
            PacketDataReader reader = new PacketDataReader(data);
            return new PlayerAction(reader.getInt(), PlayerAction.ActionType.values()[reader.getByte()]);
        } catch (IOException x){
            throw new RuntimeException(x);
        }
    }
}
