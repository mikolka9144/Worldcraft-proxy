package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketBuilder;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentDeserializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Abstract class containing basic packet recognition for use as a base for {@code interceptors}.
 * In addition, it also initialises {@link PacketBuilder packager}, but only after it receives playerId from one of packets.
 */
@Slf4j
public abstract class CommandPacketInterceptor extends PacketAlteringModule {

    protected PacketBuilder packager = null;

    // first packet is added by default
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        configurePacketer(packet);
        PacketsFormula formula = super.InterceptRawPacket(packet);

        switch (packet.getCommand()){
            case C_LOGIN_REQ -> this.interceptLogin(packet,PacketContentDeserializer.decodeLogin(packet.getData()),formula);
            case S_LOGIN_RESP -> this.interceptLoginResp(packet,PacketContentDeserializer.decodeLoginResponse(packet.getData()),formula);
            case C_ROOM_LIST_REQ -> this.interceptRoomsResp(packet,PacketContentDeserializer.decodeRoomsReq(packet.getData()),formula);
            case S_ROOM_LIST_RESP -> this.interceptRoomsResp(packet, PacketContentDeserializer.decodeRoomsData(packet.getData()),formula);
            case C_VALIDATE_PURCHASE_REQ -> this.interceptPurchaseValidationRequest(packet, PacketContentDeserializer.decodeValidatePurchaseReq(packet.getData()),formula);
            case S_VALIDATE_PURCHASE_RES -> this.interceptPurchaseValidationResponse(packet, PacketContentDeserializer.decodeValidatePurchaseResp(packet.getData()),formula);
            case SB_CHAT_MSG -> this.interceptChatMessage(packet,PacketContentDeserializer.decodeChatMessage(packet.getData()),formula);
            case C_CHAT_MSG -> this.interceptPlayerMessage(packet,PacketContentDeserializer.decodePlayerMessage(packet.getData()),formula);
            case C_PLAYER_MOVE_REQ -> this.interceptPlayerPositionReq(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()),formula);
            case C_SET_BLOCK_TYPE_REQ -> this.interceptPlaceBlockReq(packet,PacketContentDeserializer.decodePlaceBlockReq(packet.getData()),formula);
            case S_MODIFIED_BLOCKS -> this.interceptServerBlocks(packet,PacketContentDeserializer.decodeServerBlocks(packet.getData()),formula);
            case S_SET_BLOCK_TYPE -> this.interceptServerPlaceBlock(packet,PacketContentDeserializer.decodeServerPlaceBlock(packet.getData()),formula);
            case S_ENEMY_ACTION -> this.interceptEnemyAction(packet,PacketContentDeserializer.decodeEnemyAction(packet.getData()),formula);
            case C_PLAYER_ACTION_REQ -> this.interceptPlayerActionReq(packet,PacketContentDeserializer.decodeEnemyAction(packet.getData()),formula);
            case C_CHECK_VERSION_REQ -> this.interceptVersionCheckReq(packet,PacketContentDeserializer.decodeVersionCheckReq(packet.getData()),formula);
            case S_ENEMY_MOVE -> this.interceptEnemyPosition(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()),formula);
            case S_PLAYER_DISCONNECTED -> this.interceptPlayerDisconnect(packet,PacketContentDeserializer.decodePlayerDisconnect(packet.getData()),formula);
            case C_JOIN_ROOM_REQ -> this.interceptJoinRoomReq(packet,PacketContentDeserializer.decodeJoinRoomRequest(packet.getData()),formula);
            case S_JOIN_ROOM_RESP -> this.interceptJoinRoomResp(packet,PacketContentDeserializer.decodeJoinRoomResponse(packet.getData()),formula);
            case S_POPUP_MESSAGE -> this.interceptPopupMessage(packet,PacketContentDeserializer.decodePopupMessage(packet.getData()),formula);
            case SB_PLAYER_JOINED_ROOM -> this.interceptPlayerJoinedInfo(packet,PacketContentDeserializer.decodePlayerInfo(packet.getData()),formula);
            case SB_PLAYER_UPDATED -> this.interceptPlayerUpdateInfo(packet,PacketContentDeserializer.decodePlayerUpdateInfo(packet.getData()),formula);
            case S_UPDATE_PROFILE_RES -> this.interceptUserProfileUpdateResp(packet,formula);
            case C_UPDATE_PROFILE_REQ -> this.interceptUserProfileUpdateReq(packet,PacketContentDeserializer.decodePlayerInfo(packet.getData()),formula);
            case S_PLAYER_MOVE_RESP -> this.interceptPlayerPositionResp(packet,formula);
            case S_SET_BLOCK_TYPE_RESP -> this.interceptPlaceBlockResp(packet,formula);
            case S_LOAD_PURCHASES_RES -> this.interceptPurchaseLoadingResp(packet,PacketContentDeserializer.decodePurchaseLoadResponse(packet.getData()),formula);
            case C_SAVE_PURCHASES_REQ -> this.interceptPurchaseSavingReq(packet,PacketContentDeserializer.decodePurchaseLoadResponse(packet.getData()),formula);
            case S_SAVE_PURCHASES_RES -> this.interceptPurchaseSavingResp(packet,formula);
            case C_PING_REQ -> this.interceptPingReq(packet,formula);
            case S_PING_RESP -> this.interceptPingResp(packet,formula);
            case S_PLAYER_ACTION_RESP -> this.interceptPlayerActionResp(packet,formula);
            case C_DISLIKE_WORLD_REQ -> this.interceptDislikeWorld(packet,formula);
            case C_LIKE_WORLD_REQ -> this.interceptLikeWorld(packet,formula);
            case C_LOAD_PURCHASES_REQ -> this.interceptPurchaseLoadingReq(packet,formula);
            case S_CHECK_VERSION_RESP -> this.interceptVersionCheckResponse(packet,formula);
            case S_PLAYERS_INFO -> this.interceptPlayerList(packet,PacketContentDeserializer.decodePlayerList(packet.getData()),formula);
            case S_PLAYER_GRAPHICS_INITED_RESP -> this.interceptGraphicsInitializationResp(packet,formula);
            case C_PLAYER_GRAPHICS_INITED_REQ -> this.interceptGraphicsInitializationReq(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()),formula);
            case C_ROOM_SEARCH_REQ -> this.interceptRoomsSearchReq(packet,PacketContentDeserializer.decodeRoomsSearchReq(packet.getData()),formula);
            case C_CREATE_ROOM_REQ -> this.interceptCreateRoomReq(packet,PacketContentDeserializer.decodeRoomCreateReq(packet.getData()),formula);
            case S_CREATE_ROOM_RESP -> this.interceptCreateRoomResp(packet,PacketContentDeserializer.decodeRoomCreateResp(packet.getData()),formula);
            default -> interceptUnknownPacket(packet,formula);
        }
        return formula;
    }



    public void interceptCreateRoomResp(Packet packet, String worldUploadToken, PacketsFormula formula) {

    }

    public void interceptCreateRoomReq(Packet packet, RoomCreateReq roomCreateReq, PacketsFormula formula) {

    }

    public void interceptRoomsSearchReq(Packet packet, RoomSearchReq roomSearchReq, PacketsFormula formula) {

    }

    public void interceptPurchaseSavingResp(Packet packet, PacketsFormula formula) {
    }

    public void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {
    }

    public void interceptPurchaseLoadingResp(Packet packet, @Nullable PurchasesList purchasesList, PacketsFormula formula) {
    }

    public void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {
    }

    public void interceptPingResp(Packet packet, PacketsFormula formula) {
    }

    public void interceptPingReq(Packet packet, PacketsFormula formula) {
    }

    public void interceptLikeWorld(Packet packet, PacketsFormula formula) {
    }

    public void interceptDislikeWorld(Packet packet, PacketsFormula formula) {
    }

    public void interceptUserProfileUpdateReq(Packet packet, Player player, PacketsFormula formula) {
    }

    public void interceptUserProfileUpdateResp(Packet packet, PacketsFormula formula) {
    }

    public void interceptPlayerUpdateInfo(Packet packet, PlayerInfo playerInfo, PacketsFormula formula) {
    }

    public void interceptPlayerJoinedInfo(Packet packet, Player player, PacketsFormula formula) {
    }

    public void interceptPlayerList(Packet packet, List<Player> players, PacketsFormula formula) {
    }

    public void interceptJoinRoomResp(Packet packet, JoinRoomResponse joinRoomResponse, PacketsFormula formula) {
    }

    public void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {

    }

    public void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {

    }

    public void interceptPlayerDisconnect(Packet packet, int playerId, PacketsFormula formula) {

    }

    public void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {

    }

    public void interceptGraphicsInitializationResp(Packet packet, PacketsFormula formula) {

    }

    public void interceptPlayerActionResp(Packet packet, PacketsFormula formula) {
    }

    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
    }

    public void interceptPlaceBlockResp(Packet packet, PacketsFormula formula) {
    }

    public void interceptPlayerPositionResp(Packet packet, PacketsFormula formula) {

    }

    private void configurePacketer(Packet packet) {
        if (packager == null){
            if (packet.getProtoId() != PacketProtocol.SERVER){
                this.packager = new PacketBuilder(packet.getProtoId());
            }
            return;
        }
        if(packet.getPlayerId() != 0 && packager.getPlayerId() == 0) {
            packager.setPlayerId(packet.getPlayerId());
        }
    }

    public void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {
    }

    public void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {
    }

    public void interceptVersionCheckReq(Packet packet, ClientVersion clientVersion, PacketsFormula formula) {
    }

    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
    }

    public void interceptServerPlaceBlock(Packet packet, BlockData data, PacketsFormula formula) {
    }

    public void interceptPlaceBlockReq(Packet packet,BlockData data, PacketsFormula formula){

    }
    public void interceptServerBlocks(Packet packet,ServerBlockData data, PacketsFormula formula){

    }
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula){

    }
    public void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula){

    }
    public void interceptEnemyAction(Packet packet, PlayerAction data, PacketsFormula formula){

    }
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
    }

    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {

    }
    public void interceptPlayerMessage(Packet packet,String message, PacketsFormula formula){

    }
    public void interceptPurchaseValidationRequest(Packet packet, PurchaseValidationReq data, PacketsFormula formula) {

    }

    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {

    }
    public void interceptRoomsResp(Packet packet, RoomListRequest data, PacketsFormula formula) {

    }
    public void interceptUnknownPacket(Packet packet,PacketsFormula formula){

    }
}
