package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentDeserializer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public abstract class FullPacketInterceptor extends PacketInterceptor {

    // first packet is added by default
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        PacketsFormula formula = new PacketsFormula();
        formula.addUpstream(packet);
        switch (packet.getCommand()){
            case C_LOGIN_REQ -> this.interceptLogin(packet,PacketContentDeserializer.decodeLogin(packet.getData()),formula);
            case S_ROOM_LIST_RESP -> this.interceptRoomsPacket(packet,
                    PacketContentDeserializer.decodeRoomsData(packet.getData()),formula);
            case C_VALIDATE_PURCHASE_REQ -> this.interceptPurchaseValidationRequest(packet,
                    PacketContentDeserializer.decodeValidatePurchaseReq(packet.getData()),formula);
            case S_VALIDATE_PURCHASE_RES -> this.interceptPurchaseValidationResponse(packet,
                    PacketContentDeserializer.decodeValidatePurchaseResp(packet.getData()),formula);
            case SB_CHAT_MSG -> this.interceptChatMessage(packet,PacketContentDeserializer.decodeChatMessage(packet.getData()),formula);
            case C_CHAT_MSG -> this.interceptPlayerMessage(packet,PacketContentDeserializer.decodePlayerMessage(packet.getData()),formula);
            case C_PLAYER_MOVE_REQ -> this.interceptPlayerPositionReq(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()),formula);
            case C_SET_BLOCK_TYPE_REQ -> this.interceptPlaceBlockReq(packet,PacketContentDeserializer.decodePlaceBlockReq(packet.getData()),formula);
            case S_MODIFIED_BLOCKS -> this.interceptServerBlocks(packet,PacketContentDeserializer.decodeServerBlocks(packet.getData()),formula);
            case S_SET_BLOCK_TYPE -> this.interceptServerPlaceBlock(packet,PacketContentDeserializer.decodeServerPlaceBlock(packet.getData()),formula);
            case C_ROOM_LIST_REQ -> this.interceptRoomsReq(packet,PacketContentDeserializer.decodeRoomsReq(packet.getData()),formula);
            case S_ENEMY_ACTION -> this.interceptEnemyAction(packet,PacketContentDeserializer.decodeEnemyAction(packet.getData()),formula);
            case C_CHECK_VERSION_REQ -> this.interceptVersionCheckReq(packet,PacketContentDeserializer.decodeVersionCheckReq(packet.getData()),formula);
        }
        return formula;
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
}
