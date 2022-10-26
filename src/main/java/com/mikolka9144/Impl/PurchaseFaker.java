package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;

public class PurchaseFaker extends FullPacketInterceptor {
    public PurchaseFaker(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data) {
        data.setStatus(PurchaseValidationResp.Status.Sucsess);
        packet.setData(PacketContentSerializer.encodeValidatePurchaseResp(data));
    }

    @Override
    public void close() throws IOException {

    }
}
