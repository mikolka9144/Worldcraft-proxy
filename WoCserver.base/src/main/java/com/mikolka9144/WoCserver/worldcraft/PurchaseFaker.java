package com.mikolka9144.WoCserver.worldcraft;

import com.mikolka9144.WoCserver.Models.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.WoCserver.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.WoCserver.Models.Packet.Packet;
import com.mikolka9144.WoCserver.Utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldCraftPacketIO;

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
