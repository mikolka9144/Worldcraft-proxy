package com.mikolka9144.Models.Packet;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.EventCodecs.PurchaseValidationReq;
import com.mikolka9144.Models.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
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
        }
    }

    public void interceptChatMessage(Packet packet, ChatMessage data) {
    }

    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data) {

    }

    public void interceptPurchaseValidationRequest(Packet packet,PurchaseValidationReq data) {

    }

    public void interceptRoomsPacket(Packet packet, RoomsPacket data) {

    }
}
