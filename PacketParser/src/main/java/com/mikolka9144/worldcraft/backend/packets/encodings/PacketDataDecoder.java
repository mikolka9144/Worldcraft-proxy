package com.mikolka9144.worldcraft.backend.packets.encodings;

import com.mikolka9144.worldcraft.backend.packets.codecs.*;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class PacketDataDecoder {
    private PacketDataDecoder(){}
    @PacketHook(PacketCommand.SERVER_ROOM_LIST_RESP)
    public static RoomsPacket decodeRoomsData(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        RoomsPacket out = new RoomsPacket(
                reader.getInt(),
                reader.getInt(),
                reader.getShort(),
                RoomListRequest.RoomsType.findRoomTypeById(reader.getByte()));
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
    @PacketHook(PacketCommand.CLIENT_REPORT_REQ)
    public static PlayerReport decodePlayerReportReq(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        return new PlayerReport(reader.getInt(), reader.getString());
    }
    @SneakyThrows
    @PacketHook(PacketCommand.CLIENT_CHECK_VERSION_REQ)
    public static ClientBuildManifest decodeVersionCheckReq(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        return new ClientBuildManifest(
                reader.getString(),
                reader.getInt());
    }
    @PacketHook(PacketCommand.CLIENT_PURCHASES_VALIDATE_REQ)
    public static PurchaseValidationReq decodeValidatePurchaseReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PurchaseValidationReq(
                reader.getString(),
                reader.getString(),
                reader.getString());
    }
    @PacketHook(PacketCommand.SERVER_PURCHASES_VALIDATE_RESP)
    public static PurchaseValidationResp decodeValidatePurchaseResp(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PurchaseValidationResp(
                PurchaseValidationResp.Status.values()[reader.getInt()],
                reader.getString());
    }
    @PacketHook(PacketCommand.SERVER_MESSAGE)
    public static ChatMessage decodeChatMessage(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        byte msgTypeBit = reader.getByte();
        return new ChatMessage(
                reader.getString(),
                reader.getString(),
                ChatMessage.MsgType.values()[msgTypeBit]);
    }
    @PacketHook(PacketCommand.CLIENT_SPEAK)
    public static String decodePlayerMessage(byte[] data) {
        return new PacketDataReader(data).readAsText();
    }
    @PacketHook(PacketCommand.SERVER_ENEMY_MOVED)
    public static MovementPacket decodeEnemyMovementPacket(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return decodeMovement(reader);
    }
    @PacketHook(PacketCommand.CLIENT_PLAYER_MOVE_REQ)
    public static MovementPacket decodePlayerMovementReq(byte[] data){
        return decodeEnemyMovementPacket(data);
    }
    @PacketHook(PacketCommand.CLIENT_READY_REQ)
    public static MovementPacket decodeinitGraphicsReq(byte[] data){
        return decodeEnemyMovementPacket(data);
    }
    @SneakyThrows
    @PacketHook(PacketCommand.CLIENT_PLACE_BLOCK_REQ)
    public static Block decodePlaceBlockReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        Block result = new Block();
        result.setX(reader.getShort());
        result.setY(reader.getShort());
        result.setZ(reader.getShort());
        result.setChunkX(reader.getShort());
        result.setChunkZ(reader.getShort());
        result.setBlockType(reader.getByte());
        result.setBlockData(reader.getByte());
        result.setPrevBlockType(reader.getByte());
        result.setPrevBlockData(reader.getByte());
        result.setBlockData(reader.getByte());
        return result;
    }

    @PacketHook(PacketCommand.SERVER_ROOM_BLOCKS)
    public static ServerBlockData decodeServerBlocks(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        int curPacket = reader.getInt();
        int packetCount = reader.getInt();
        List<Block> blocks = new ArrayList<>();
        while (reader.hasNext(10)){
            blocks.add(new Block(reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getByte(),
                    reader.getByte()));
            reader = new PacketDataReader(reader.getBytes());
        }
        return new ServerBlockData(curPacket,packetCount,blocks);
    }
    @PacketHook(PacketCommand.SERVER_UPDATE_BLOCK)
    public static Block decodeServerPlaceBlock(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new Block(reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                reader.getShort(),
                reader.getByte(),
                reader.getByte());
    }
    @PacketHook(PacketCommand.CLIENT_LOGIN_REQ)
    public static LoginInfo decodeLogin(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new LoginInfo(
                reader.getString(),
                reader.getShort(),
                reader.getString(),
                reader.getString(),
                reader.getString(), reader.getString(), reader.getString(), reader.getString());

    }

    @PacketHook(PacketCommand.CLIENT_ROOM_LIST_REQ)
    public static RoomListRequest decodeRoomsReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new RoomListRequest(RoomListRequest.RoomsType.findRoomTypeById(reader.getByte()), reader.getInt());
    }
    @PacketHook(PacketCommand.SERVER_ENEMY_TAPPED)
    public static PlayerAction decodeEnemyAction(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PlayerAction(reader.getInt(), PlayerAction.ActionType.values()[reader.getByte()]);
    }@PacketHook(PacketCommand.CLIENT_PLAYER_TAP_REQ)
    public static PlayerAction decodePlayerActionReq(byte[] data) {
        return decodeEnemyAction(data);
    }
    @PacketHook(PacketCommand.SERVER_LOGIN_RESP)
    public static LoginResponse decodeLoginResponse(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new LoginResponse(reader.getInt(), reader.getString(), reader.getBoolean());
    }
    @PacketHook(PacketCommand.SERVER_PLAYER_LEFT)
    public static int decodePlayerDisconnect(byte[] data){
        PacketDataReader reader = new PacketDataReader(data);
        return reader.getInt();
    }
    @PacketHook(PacketCommand.CLIENT_ROOM_JOIN_REQ)
    public static JoinRoomRequest decodeJoinRoomRequest(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new JoinRoomRequest(reader.getString(), reader.getString(), reader.getBoolean());
    }
    @PacketHook(PacketCommand.SERVER_DIALOG_DISPLAY)
    public static PopupMessage decodePopupMessage(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PopupMessage(PopupMessage.PopupMessageType.findByValue(reader.getByte()), reader.getLong(), reader.getString(), reader.getString());
    }
    @PacketHook(PacketCommand.SERVER_ROOM_JOIN_RESP)
    public static JoinRoomResponse decodeJoinRoomResponse(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new JoinRoomResponse(reader.getBoolean(), reader.getBoolean());
    }
    @PacketHook(PacketCommand.SERVER_PLAYERS_LIST)
    public static List<Player> decodePlayerList(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        List<Player> players = new ArrayList<>();
        while (reader.hasNext(8)){
            Player player = new Player();
            player.setId(reader.getInt());
            player.setNickname(reader.getString());
            player.setSkinId(reader.getShort());
            player.setPos(decodeMovement(reader));
            players.add(player);
        }
        return players;
    }
    @PacketHook(PacketCommand.CLIENT_PLAYER_UPDATE_REQ)
    public static Player decodePlayerInfo(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        Player player = new Player();
        player.setId(reader.getInt());
        player.setNickname(reader.getString());
        player.setSkinId(reader.getShort());
        player.setPos(decodeMovement(reader));
        return player;
    }
    @PacketHook(PacketCommand.SERVER_PLAYER_JOINED)
    public static Player decodeEnemyInfo(byte[] data) {
        return decodePlayerInfo(data);
    }
    @PacketHook(PacketCommand.SERVER_ENEMY_UPDATED)
    public static PlayerInfo decodePlayerUpdateInfo(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        return new PlayerInfo(reader.getInt(), reader.getString(), reader.getShort());
    }
    @SneakyThrows
    @PacketHook(PacketCommand.SERVER_PURCHASES_LOAD_RESP)
    public static PurchasesList decodePurchaseLoadResponse(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        String jsonData = reader.getString();
        if (!jsonData.isEmpty()){
            return PurchasesList.decodefromJson(jsonData);
        }
        else{
            return null;
        }
    }
    @PacketHook(PacketCommand.CLIENT_PURCHASES_SAVE_REQ)
    public static PurchasesList decodePurchaseSaveReq(byte[] data) {
        return decodePurchaseLoadResponse(data);
    }
    @PacketHook(PacketCommand.CLIENT_ROOM_SEARCH)
    public static RoomSearchReq decodeRoomsSearchReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        RoomSearchReq searchReq = new RoomSearchReq();
        searchReq.setQuery(reader.getString());
        searchReq.setStartingIndex(reader.getInt());
        return searchReq;
    }
    @PacketHook(PacketCommand.CLIENT_ROOM_CREATE_REQ)
    public static RoomCreateReq decodeRoomCreateReq(byte[] data) {
        PacketDataReader reader = new PacketDataReader(data);
        RoomCreateReq result = new RoomCreateReq();
        result.setRoomName(reader.getString());
        result.setRoomPassword(reader.getString());
        result.setReadOnly(result.isReadOnly());
        return result;
    }
    @PacketHook(PacketCommand.SERVER_ROOM_CREATE_RESP)
    public static String decodeRoomCreateResp(byte[] data) {
        return new PacketDataReader(data).readAsText();
    }
    public static MovementPacket decodeMovement(PacketDataReader reader) {
        MovementPacket result = new MovementPacket();
        result.setPlayerId(reader.getInt());
        result.setPosition(reader.getVector3());
        result.setOrientation(reader.getVector3());
        result.setBase(reader.getVector3());
        return result;
    }
}
