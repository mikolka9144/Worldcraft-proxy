package com.mikolka9144.worldcraft.socket.Packet.packetParsers;

import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;

import java.util.List;

@SuppressWarnings("unused")
//this class is a serializer used by future interceptors.
public class PacketDataEncoder {
    private PacketDataEncoder() {
    }

    public static byte[] roomsQueryResponse(RoomsPacket data) {
        PacketDataBuilder builder = new PacketDataBuilder()
                .append(data.getPacketIndex())
                .append(data.getAllPackets())
                .append(data.getInitialRoomListSize())
                .append(data.getRoomType().getId());
        for (RoomsPacket.Room room : data.getRooms()) {
            builder.append(room.getId())
                    .append(room.getName())
                    .append(room.isProtected())
                    .append(room.getActivePlayers())
                    .append(room.getRoomCapacity())
                    .append(room.getNumberOfEntrances())
                    .append(room.getLikes())
                    .append(room.isReadOnly());
        }
        return builder.build();
    }

    public static byte[] validatePurchaseReq(PurchaseValidationReq data) {
        return new PacketDataBuilder()
                .append(data.getPurchaseName())
                .append(data.getBundleId())
                .append(data.getReceipt())
                .build();
    }

    public static byte[] validatePurchaseResp(PurchaseValidationResp data) {
        return new PacketDataBuilder()
                .append(data.getStatus().ordinal())
                .append(data.getReceipt())
                .build();
    }

    public static byte[] chatMessage(ChatMessage data) {
        return new PacketDataBuilder()
                .append((byte) data.getType().ordinal())
                .append(data.getPlayerNicknameArg())
                .append(data.getMessage())
                .build();
    }

    public static byte[] playerMessage(String data) {
        return data.getBytes();
    }

    public static byte[] enemyMovementPacket(MovementPacket data) {
        PacketDataBuilder writer = new PacketDataBuilder();writer
                .append(data.getPlayerId())
                .append(data.getPosition())
                .append(data.getOrientation())
                .append(data.getBase());
        return writer.build();
    }

    public static byte[] placeBlockReq(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();writer
                .append(serverPlaceBlock(data))
                .append(data.getPrevBlockData())
                .append(data.getPrevBlockType());
        return writer.build();
    }

    public static byte[] serverBlocks(ServerBlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPacketIndex())
                .append(data.getAllPackets());
        for (BlockData block : data.getBlocks()) {
            writer.append(serverPlaceBlock(block));
        }
        return writer.build();
    }

    public static byte[] serverPlaceBlock(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder()
                .append(data.getX())
                .append(data.getY())
                .append(data.getZ())
                .append(data.getChunkX())
                .append(data.getChunkZ())
                .append(data.getBlockType().getId())
                .append(data.getBlockData());
        return writer.build();
    }

    public static byte[] login(LoginInfo data) {
        PacketDataBuilder writer = new PacketDataBuilder();writer
                .append(data.getUsername())
                .append(data.getSkinId())
                .append(data.getClientVer())
                .append(data.getDeviceId())
                .append(data.getDeviceName())
                .append(data.getAndroidVer())
                .append(data.getAndroidAPI())
                .append(data.getMarketName());
        return writer.build();
    }

    public static byte[] versionCheckReq(ClientVersion data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getMarket())
                .append(data.getClientVersion());
        return writer.build();

    }

    public static byte[] roomsReq(RoomListRequest data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append((byte) data.getRoomsType().ordinal())
                .append(data.getStartingIndex());
        return writer.build();
    }

    public static byte[] enemyAction(PlayerAction data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId())
                .append((byte) data.getActionType().ordinal());
        return writer.build();
    }

    public static byte[] loginResponse(LoginResponse data) {
        PacketDataBuilder writer = new PacketDataBuilder()
                .append(data.getPlayerId())
                .append(data.getPlayerName())
                .append(data.isPurchaseValidated());
        return writer.build();
    }

    public static byte[] playerDisconnect(int playerId) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(playerId);
        return writer.build();
    }

    public static byte[] joinRoomRequest(JoinRoomRequest data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getRoomName())
                .append(data.getPassword())
                .append(data.isReadOlny());
        return writer.build();
    }
    public static byte[] joinRoomResponse(JoinRoomResponse data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.isUserOwner())
                .append(data.isWorldReadOnly());
        return writer.build();
    }

    public static byte[] popupMessage(PopupMessage data) {
        PacketDataBuilder writer = new PacketDataBuilder()
                .append(data.getMessagePreset().getValue())
                .append(data.getTimeParameter())
                .append(data.getTextParameter())
                .append(data.getBaseMessage());
        return writer.build();
    }
    public static byte[] playerList(List<Player> data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        for (Player player : data) {
            writer.append(playerInfo(player));
        }
        return writer.build();
    }
    public static byte[] playerInfo(Player player) {
        PacketDataBuilder writer = new PacketDataBuilder()
                .append(player.getId())
        .append(player.getNickname())
        .append(player.getSkinId())
        .append(player.getId())
        .append(player.getPosition())
        .append(player.getAt())
        .append(player.getUp());
        return writer.build();
    }
    public static byte[] playerUpdateInfo(PlayerInfo data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getId());
        writer.append(data.getNickname());
        writer.append(data.getSkinId());
        return writer.build();
    }
    public static byte[] roomsSearchReq(RoomSearchReq data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getQuery())
                .append(data.getStartingIndex());
        return writer.build();
    }
    public static byte[] roomCreateReq(RoomCreateReq data){
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getRoomName())
                .append(data.getRoomPassword())
                .append(data.isReadOnly());
        return writer.build();
    }
    @SneakyThrows
    public static byte[] purchaseLoadResponse(@Nullable PurchasesList data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        if (data != null){
            writer.append(data.encodeToJson());
        }
        else {
            writer.append("");
        }
        return writer.build();
    }
    public static byte[] roomCreateResp(String token) {
        return token.getBytes();
    }
}
