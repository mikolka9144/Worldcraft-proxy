package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;

public class PurchaseFaker extends FullPacketInterceptor {
    public PurchaseFaker(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data) {
        data.setStatus(PurchaseValidationResp.Status.Sucsess);
        packet.setData(PacketContentSerializer.encodeValidatePurchaseResp(data));
    }
}
