package com.mikolka9144.Models.Packet;

import com.mikolka9144.Models.EventCodecs.*;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.ContentParsers.PacketContentDeserializer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

public abstract class FullPacketInterceptor extends PacketInterceptor {
    public FullPacketInterceptor(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        switch (packet.getCommand()){
            case S_ROOM_LIST_RESP -> this.interceptRoomsPacket(packet,
                    PacketContentDeserializer.decodeRoomsData(packet.getData()));
            case C_VALIDATE_PURCHASE_REQ -> this.interceptPurchaseValidationRequest(packet,
                    PacketContentDeserializer.decodeValidatePurchaseReq(packet.getData()));
            case S_VALIDATE_PURCHASE_RES -> this.interceptPurchaseValidationResponse(packet,
                    PacketContentDeserializer.decodeValidatePurchaseResp(packet.getData()));
            case SB_CHAT_MSG -> this.interceptChatMessage(packet,PacketContentDeserializer.decodeChatMessage(packet.getData()));
            case C_CHAT_MSG -> this.interceptPlayerMessage(packet,PacketContentDeserializer.decodePlayerMessage(packet.getData()));
            case C_PLAYER_MOVE_REQ -> this.interceptPlayerPositionReq(packet,PacketContentDeserializer.decodeMovementPacket(packet.getData()));
        }
    }

    public void interceptPlayerPositionReq(Packet packet, MovementPacket data){

    }
    public void interceptEnemyPosition(Packet packet, MovementPacket data){

    }
    public void interceptChatMessage(Packet packet, ChatMessage data) {
    }

    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data) {

    }
    public void interceptPlayerMessage(Packet packet,String message){

    }
    public void interceptPurchaseValidationRequest(Packet packet,PurchaseValidationReq data) {

    }

    public void interceptRoomsPacket(Packet packet, RoomsPacket data) {

    }
}
