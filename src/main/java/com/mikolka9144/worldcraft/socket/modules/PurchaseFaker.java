package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public class PurchaseFaker extends FullPacketInterceptor {
    @Override
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {
        data.setStatus(PurchaseValidationResp.Status.Sucsess);
        packet.setData(PacketContentSerializer.encodeValidatePurchaseResp(data));
    }
}
