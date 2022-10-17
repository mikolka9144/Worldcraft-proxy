package com.mikolka9144.Models.Packets;

import com.mikolka9144.Models.EventCodecs.PurchaseValidationReq;
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
            case C_VALIDATE_PURCHASE_REQ -> this.interceptPurchaseValidationRequest(packet,PacketContentDeserializer.decodeValidatePurchaseReq(packet.getData()));
        }
    }

    public void interceptPurchaseValidationRequest(Packet packet,PurchaseValidationReq data) {

    }

    public void interceptRoomsPacket(Packet packet, RoomsPacket data) {

    }
}
