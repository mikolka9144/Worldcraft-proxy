package com.mikolka9144.worldcraft.socket.logic.packetParsers;

import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;

import java.util.List;

@SuppressWarnings("unused")
//this class is a serializer used by future interceptors.
public class PacketContentSerializer {
    private PacketContentSerializer() {
    }

    public static byte[] encodeRoomsData(RoomsPacket packet) {
        return encodeRoomsData(packet, true);
    }

    public static byte[] encodeRoomsData(RoomsPacket packet, boolean encodeReadOnlyStatus) {
        PacketDataBuilder builder = new PacketDataBuilder()
                .append(packet.getPacketIndex())
                .append(packet.getAllPackets())
                .append(packet.getInitialRoomListSize())
                .append(packet.getRoomType());
        for (RoomsPacket.Room room : packet.getRooms()) {
            builder.append(room.getId())
                    .append(room.getName())
                    .append(room.isProtected())
                    .append(room.getActivePlayers())
                    .append(room.getRoomCapacity())
                    .append(room.getNumberOfEntrances())
                    .append(room.getLikes());
            if (encodeReadOnlyStatus) {
                builder.append(room.isReadOnly());
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

    public static byte[] encodeValidatePurchaseResp(PurchaseValidationResp data) {
        return new PacketDataBuilder()
                .append(data.getStatus().ordinal())
                .append(data.getReceipt())
                .build();
    }

    public static byte[] encodeChatMessage(ChatMessage data) {
        return new PacketDataBuilder()
                .append((byte) data.getType().ordinal())
                .append(data.getPlayerNicknameArg())
                .append(data.getMessage())
                .build();
    }

    public static byte[] encodePlayerMessage(String data) {
        return data.getBytes();
    }

    public static byte[] encodeEnemyMovementPacket(MovementPacket data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId())
                .append(data.getPosition())
                .append(data.getOrientation())
                .append(data.getUp());
        return writer.build();
    }

    public static byte[] encodePlaceBlockReq(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(encodeServerPlaceBlock(data))
                .append(data.getPrevBlockData())
                .append(data.getPrevBlockType());
        return writer.build();
    }

    public static byte[] encodeServerBlocks(ServerBlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPacketIndex())
                .append(data.getAllPackets());
        for (BlockData block : data.getBlocks()) {
            writer.append(encodeServerPlaceBlock(block));
        }
        return writer.build();
    }

    public static byte[] encodeServerPlaceBlock(BlockData data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getX())
                .append(data.getY())
                .append(data.getZ())
                .append(data.getChunkX())
                .append(data.getChunkZ())
                .append(data.getBlockType().getId())
                .append(data.getBlockData());
        return writer.build();
    }

    public static byte[] encodeLogin(LoginInfo data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getUsername())
                .append(data.getSkinId())
                .append(data.getClientVer())
                .append(data.getDeviceId())
                .append(data.getDeviceName())
                .append(data.getAndroidVer())
                .append(data.getAndroidAPI())
                .append(data.getMarketName());
        return writer.build();
    }

    public static byte[] encodeVersionCheckReq(ClientVersion data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getMarket())
                .append(data.getClientVersion());
        return writer.build();

    }

    public static byte[] encodeRoomsReq(RoomListRequest data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append((byte) data.getRoomsType().ordinal())
                .append(data.getStartingIndex());
        return writer.build();
    }

    public static byte[] encodeEnemyAction(PlayerAction data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId())
                .append((byte) data.getActionType().ordinal());
        return writer.build();
    }

    public static byte[] encodeLoginResponse(LoginResponse data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getPlayerId())
                .append(data.getPlayerName())
                .append(data.isPurchaseValidated());
        return writer.build();
    }

    public static byte[] encodePlayerDisconnect(int playerId) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(playerId);
        return writer.build();
    }

    public static byte[] encodeJoinRoomRequest(JoinRoomRequest data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getRoomName())
                .append(data.getPassword())
                .append(data.isReadOlny());
        return writer.build();
    }
    public static byte[] encodeJoinRoomResponse(JoinRoomResponse data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.isUserOwner())
                .append(data.isWorldReadOnly());
        return writer.build();
    }

    public static byte[] encodePopupMessage(PopupMessage data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getMessagePreset().getValue())
                .append(data.getTimeParameter())
                .append(data.getTextParameter())
                .append(data.getBaseMessage());
        return writer.build();
    }
    public static byte[] encodePlayerList(List<Player> data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        for (Player player : data) {
            writer.append(encodePlayerInfo(player));
        }
        return writer.build();
    }
    public static byte[] encodePlayerInfo(Player player) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(player.getId());
        writer.append(player.getNickname());
        writer.append(player.getSkinId());
        writer.append(player.getId());
        writer.append(player.getPosition());
        writer.append(player.getAt());
        writer.append(player.getUp());
        return writer.build();
    }
    public static byte[] encodePlayerUpdateInfo(PlayerInfo data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        writer.append(data.getId());
        writer.append(data.getNickname());
        writer.append(data.getSkinId());
        return writer.build();
    }
    @SneakyThrows
    public static byte[] encodePurchaseLoadResponse(@Nullable PurchasesList data) {
        PacketDataBuilder writer = new PacketDataBuilder();
        if (data != null){
            writer.append(data.encodeToJson());
        }
        else {
            writer.append("");
        }
        return writer.build();
    }
}
