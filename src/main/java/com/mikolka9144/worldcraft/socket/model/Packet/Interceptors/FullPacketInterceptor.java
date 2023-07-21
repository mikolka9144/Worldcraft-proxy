package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.PacketBuilder;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentDeserializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract class containing basic packet recognition for use as a base for {@code interceptors}.
 * In addition, it also initialises {@link PacketBuilder packager}, but only after it receives playerId from one of packets.
 */
@Slf4j
public abstract class FullPacketInterceptor extends PacketAlteringModule {

    protected PacketBuilder packager = null;

    // first packet is added by default
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        if(packager == null) configurePacketer(packet);
        PacketsFormula formula = super.InterceptRawPacket(packet);
        switch (packet.getCommand()){
            case C_LOGIN_REQ -> this.interceptLogin(packet,PacketContentDeserializer.decodeLogin(packet.getData()),formula);
            case S_LOGIN_RESP -> this.interceptLoginResp(packet,PacketContentDeserializer.decodeLoginResponse(packet.getData()),formula);
            case C_ROOM_LIST_REQ -> this.interceptRoomsReq(packet,PacketContentDeserializer.decodeRoomsReq(packet.getData()),formula);
            case S_ROOM_LIST_RESP -> this.interceptRoomsPacket(packet, PacketContentDeserializer.decodeRoomsData(packet.getData()),formula);
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
            case S_POPUP_MESSAGE -> this.interceptPopupMessage(packet,PacketContentDeserializer.decodePopupMessage(packet.getData()),formula);
            case S_PLAYER_MOVE_RESP -> this.interceptPlayerPositionResp(packet,formula);
            case S_SET_BLOCK_TYPE_RESP -> this.interceptPlaceBlockResp(packet,formula);
            case S_PLAYER_ACTION_RESP -> this.interceptPlayerActionResp(packet,formula);
            case S_CHECK_VERSION_RESP -> this.interceptVersionCheckResponse(packet,formula);
            case S_PLAYER_GRAPHICS_INITED_RESP -> this.interceptGraphicsInitializationResp(packet,formula);
            case C_PLAYER_GRAPHICS_INITED_REQ -> this.interceptGraphicsInitializationReq(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()),formula);
            default -> interceptUnknownPacket(packet,formula);
        }
        return formula;
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
        if(packet.getPlayerId() == 0) return;
        this.packager = new PacketBuilder(packet.getProtoId(), packet.getPlayerId());
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

    public void interceptRoomsPacket(Packet packet, RoomsPacket data, PacketsFormula formula) {

    }
    public void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {

    }
    public void interceptUnknownPacket(Packet packet,PacketsFormula formula){

    }
}
