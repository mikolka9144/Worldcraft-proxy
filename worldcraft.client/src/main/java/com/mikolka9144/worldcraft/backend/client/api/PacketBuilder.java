package com.mikolka9144.worldcraft.backend.client.api;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.*;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.packets.errorcodes.LoginErrorCode;
import com.mikolka9144.worldcraft.backend.packets.errorcodes.RoomJoinError;
import com.mikolka9144.worldcraft.backend.packets.errorcodes.CreateRoomErrorCode;
import com.mikolka9144.worldcraft.backend.packets.errorcodes.VersionCheckErrorCode;
import com.mikolka9144.worldcraft.utills.Vector3;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class contains methods for building packets.
 */
@SuppressWarnings("unused") // these are API methods (unused here)
@Getter
public class PacketBuilder {
    private final PacketProtocol clientProto;
    @Setter
    private int playerId;

    public PacketBuilder(PacketProtocol clientProto) {
        this.clientProto = clientProto;
    }
    public Packet serverPacket(PacketCommand command,byte[] data,byte error){
        return new Packet(
                PacketProtocol.SERVER,
                0,
                command,
                "",
                error,
                data
        );
    }
    public Packet serverPacket(PacketCommand command, byte[] data){
        return serverPacket(command,data,(byte)0);
    }
    public Packet clientPacket(PacketCommand command,byte[] data){
        return new Packet(
                clientProto,
                playerId,
                command,
                "",
                (byte)0,
                data
        );
    }
    public Packet loginReq(LoginInfo info){
        return clientPacket(PacketCommand.CLIENT_LOGIN_REQ,PacketDataEncoder.login(info));
    }
    public Packet loginResp(int playerId, String playerName){
        return serverPacket(PacketCommand.SERVER_LOGIN_RESP,
                PacketDataEncoder.loginResponse(
                        new LoginResponse(playerId,playerName,false)
                ));
    }
    public Packet loginResp(LoginErrorCode error,String message){
        return serverPacket(PacketCommand.SERVER_LOGIN_RESP,message.getBytes(),error.getvalue());
    }
    public Packet playerSpeak(String line) {
        return new Packet(clientProto,playerId,
                PacketCommand.CLIENT_SPEAK,"", (byte) 0, PacketDataEncoder.playerMessage(line));
    }

    public Packet serverChatMessage(String text) {
        var chatMsg = new ChatMessage("", text, ChatMessage.MsgType.STANDARD);
        return serverChatMessage(chatMsg);

    }
    public Packet serverChatMessage(ChatMessage message) {
        return serverPacket(PacketCommand.SERVER_MESSAGE,PacketDataEncoder.chatMessage(message));

    }
    public Packet createRoomReq(String name,String password,boolean readOnly){
        return clientPacket(PacketCommand.CLIENT_ROOM_CREATE_REQ,
                PacketDataEncoder.roomCreateReq(new RoomCreateReq(name,password,readOnly)));
    }
    public Packet createRoomReq(String name,boolean readOnly){
        return createRoomReq(name,"",readOnly);
    }
    public Packet createRoomResp(String creationToken){
        return serverPacket(PacketCommand.SERVER_ROOM_CREATE_RESP,PacketDataEncoder.roomCreateResp(creationToken));
    }
    public Packet createRoomResp(CreateRoomErrorCode error,String message){
        return serverPacket(PacketCommand.SERVER_ROOM_CREATE_RESP,message.getBytes(),error.getvalue());
    }
    public Packet joinRoomReq(String name,String password,boolean isReadOnly){
        return clientPacket(PacketCommand.CLIENT_ROOM_JOIN_REQ,PacketDataEncoder.joinRoomRequest(new JoinRoomRequest(name,password,isReadOnly)));
    }
    public Packet joinRoomReq(String name,String password){
        return joinRoomReq(name, password,false);
    }
    public Packet joinRoomReq(String name){
        return joinRoomReq(name, "");
    }
    public Packet joinRoomResp(boolean isAdmin,boolean isReadOlny){
        return serverPacket(PacketCommand.SERVER_ROOM_JOIN_RESP,PacketDataEncoder.joinRoomResponse(new JoinRoomResponse(isAdmin,isReadOlny)));
    }
    public Packet joinRoomResp(RoomJoinError error,String message){
        return serverPacket(PacketCommand.SERVER_ROOM_JOIN_RESP,message.getBytes(),error.getvalue());
    }
    public Packet checkVersion(String buildSource,int buildNumber){
        return clientPacket(PacketCommand.CLIENT_CHECK_VERSION_REQ,PacketDataEncoder.versionCheckReq(new ClientBuildManifest(buildSource,buildNumber)));
    }
    public Packet checkVersionResp(VersionCheckErrorCode status,String message){
        return serverPacket(PacketCommand.SERVER_CHECK_VERSION_RESP,message.getBytes(),status.getvalue());
    }
    public Packet checkVersionResp(){
        return checkVersionResp(VersionCheckErrorCode.NO_ERROR,"");
    }
    public Packet requestRooms(RoomListRequest.RoomsType list,int startingIndex){
        return clientPacket(PacketCommand.CLIENT_ROOM_LIST_REQ,PacketDataEncoder.roomsReq(new RoomListRequest(list,startingIndex)));
    }
    public List<Packet> respondWithRooms(List<RoomsPacket.Room> rooms, RoomListRequest.RoomsType type, int roomsPerPacket){
        List<Packet> packets = new ArrayList<>();
        var chunks = prepareChunks(rooms, roomsPerPacket);
        for (List<RoomsPacket.Room> chunk : chunks) {
            RoomsPacket pack = new RoomsPacket(packets.size(), chunks.size(), (short) 50, type);
            pack.setRooms(chunk);
            packets.add(serverPacket(PacketCommand.SERVER_ROOM_LIST_RESP,
                    PacketDataEncoder.roomsQueryResponse(pack)));
        }
        return packets;
    }

    public Packet sendBlockClientPacket(Block data) {
        return new Packet(clientProto, playerId,
                PacketCommand.CLIENT_PLACE_BLOCK_REQ, "", (byte) 0,
                PacketDataEncoder.placeBlockReq(data));
    }
    public Packet sendBlockClientPacket(int x, int y, int z, BlockType blockType, int blockData, int blockTypePrev, int blockDataPrev) {
        return sendBlockClientPacket(new Block(
                new Vector3Short(x,y,z),
                blockType,
                (byte) blockData,
                (byte) blockTypePrev,
                (byte) blockDataPrev
        ));
    }
    public Packet respondToClientBlock(){
        return pong(PacketCommand.SERVER_PLACE_BLOCK_RESP);
    }
    public Packet
    setBlockServerPacket(int x, int y, int z, BlockType blockType, int blockData){
        return serverPacket(PacketCommand.SERVER_UPDATE_BLOCK,
                PacketDataEncoder.serverPlaceBlock(new Block(
                        new Vector3Short(x,y,z),
                        blockType,
                        (byte) blockData
                )));
    }

    public Packet
    setBlockServerPacket(Block data){
        return new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.SERVER_UPDATE_BLOCK, "", (byte) 0,
                PacketDataEncoder.serverPlaceBlock(data));


    }
    public Packet movePlayer(Vector3 position,Vector3 rotation){
        return clientPacket(PacketCommand.CLIENT_PLAYER_MOVE_REQ,PacketDataEncoder.movementPacket(
                new MovementPacket(playerId,position,rotation,new Vector3(0,1,0))
        ));
    }
    public Packet movePlayerResponse(){
        return pong(PacketCommand.SERVER_PLAYER_MOVE_RESP);
    }
    public Packet moveEnemy(int playerId,Vector3 position,Vector3 rotation){
        return serverPacket(PacketCommand.SERVER_ENEMY_MOVED,PacketDataEncoder.movementPacket(
                new MovementPacket(playerId,position,rotation,new Vector3(0,1,0))
        ));
    }
    public Packet playerTap(PlayerAction.ActionType tapType){
        return clientPacket(PacketCommand.CLIENT_PLAYER_TAP_REQ,PacketDataEncoder.enemyAction(
                new PlayerAction(playerId,tapType)
        ));
    }
    public Packet playerTapResp(){
        return pong(PacketCommand.CLIENT_PLAYER_TAP_RESP);
    }
    public Packet enemyTap(int playerId,PlayerAction.ActionType tapType){
        return serverPacket(PacketCommand.SERVER_ENEMY_TAPPED,PacketDataEncoder.enemyAction(
                new PlayerAction(playerId,tapType)
        ));
    }
    public Packet readyPlayer(Vector3 initPosition,Vector3 initRotation){
        Packet packet = movePlayer(initPosition, initRotation);
        packet.setCommand(PacketCommand.CLIENT_READY_REQ);
        return packet;
    }
    public Packet readyPlayerResp(){
        return pong(PacketCommand.SERVER_READY_RESP);
    }
    public List<Packet> createBlockComp(List<Block> blocks, int blocksPerPacket) {
        List<ServerBlockData> blockData = new ArrayList<>();
        var chunks = prepareChunks(blocks,blocksPerPacket).stream().toList();

        for (int i = 0; i < chunks.size(); i++) {
            blockData.add(new ServerBlockData(i, chunks.size(), chunks.get(i)));
        }

        return blockData.stream()
                .map(PacketDataEncoder::serverBlocks)
                .map(s -> serverPacket(PacketCommand.SERVER_ROOM_BLOCKS,s))
                .toList();

    }
    public List<Packet> createPlayerList(List<Player> players, int playersPerPacket) {
        return prepareChunks(players, playersPerPacket).stream().map(s ->
                serverPacket(PacketCommand.SERVER_PLAYERS_LIST, PacketDataEncoder.playerList(s))).toList();
    }
    public Packet playerJoined(Player player){
        return serverPacket(PacketCommand.SERVER_PLAYER_JOINED,PacketDataEncoder.playerInfo(player));
    }
    public Packet playerLeft(int playerId){
        return serverPacket(PacketCommand.SERVER_PLAYER_JOINED,PacketDataEncoder.playerDisconnect(playerId));
    }
    public Packet likeCurrentWorld(){
        return ping(PacketCommand.CLIENT_LIKE_WORLD);
    }
    public Packet dislikeCurrentWorld(){
        return ping(PacketCommand.CLIENT_DISLIKE_WORLD);
    }
    public Packet searchRooms(String query,int fromIndex){
        return clientPacket(PacketCommand.CLIENT_ROOM_SEARCH,PacketDataEncoder.roomsSearchReq(new RoomSearchReq(query,fromIndex)));
    }
    public Packet ping(){
        return ping(PacketCommand.CLIENT_PING);
    }
    public Packet pong(){
        return pong(PacketCommand.SERVER_PONG);
    }
    public Packet playerReport(int playerId,String reason){
        return clientPacket(PacketCommand.CLIENT_REPORT_REQ,PacketDataEncoder.reportPlayerRep(new PlayerReport(playerId,reason)));
    }
    public Packet playerReportResp(){
        return pong(PacketCommand.SERVER_REPORT_RESP);
    }
    public Packet displayAlert(PopupMessage data){
        return serverPacket(PacketCommand.SERVER_DIALOG_DISPLAY,PacketDataEncoder.popupMessage(data));
    }
    public Packet displayAlert(String message){
        return displayAlert(new PopupMessage(message));
    }
    public Packet updatePlayerApperance(String newUsername,short newSkinId){
        return clientPacket(PacketCommand.CLIENT_PLAYER_UPDATE_REQ,
                PacketDataEncoder.playerUpdateInfo(new PlayerInfo(0,newUsername,newSkinId)));
    }
    public Packet updatePlayerApperanceResp(){
        return pong(PacketCommand.SERVER_PLAYER_UPDATE_RES);
    }
    public Packet updateEnemyApperance(PlayerInfo enemy){
        return clientPacket(PacketCommand.CLIENT_PLAYER_UPDATE_REQ,
                PacketDataEncoder.enemyUpdateInfo(enemy));
    }
    public Packet saveEmptyPurchases(){
        return savePurchases(null);
    }
    public Packet savePurchases(PurchasesList shoppingList){
        return clientPacket(PacketCommand.CLIENT_PURCHASES_SAVE_REQ,PacketDataEncoder.purchaseSaveReq(shoppingList));
    }
    public Packet savePurchasesResp(){
        return pong(PacketCommand.SERVER_PURCHASES_SAVE_RESP);
    }
    public Packet loadPurchases(){
        return ping(PacketCommand.CLIENT_PURCHASES_LOAD_REQ);
    }
    public Packet loadPurchasesResp(PurchasesList shoppingList){
        return serverPacket(PacketCommand.SERVER_PURCHASES_LOAD_RESP,PacketDataEncoder.purchaseLoadResponse(shoppingList));
    }
    public Packet validatePurchase(PurchaseValidationReq req){
        return clientPacket(PacketCommand.CLIENT_PURCHASES_VALIDATE_REQ,PacketDataEncoder.validatePurchaseReq(req));
    }
    public Packet validatePurchaseResp(PurchaseValidationResp.Status status, String receipt){
        return serverPacket(PacketCommand.SERVER_PURCHASES_VALIDATE_RESP,
                PacketDataEncoder.validatePurchaseResp(new PurchaseValidationResp(status,receipt)));
    }
    private Packet ping(PacketCommand pinger){
        return clientPacket(pinger,new byte[0]);
    }
    private Packet pong(PacketCommand ponger){
        return serverPacket(ponger,new byte[0]);
    }
    public static <T> Collection<List<T>> prepareChunks(List<T> inputList, int chunkSize) {
        AtomicInteger counter = new AtomicInteger();
        return inputList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
    }
}
