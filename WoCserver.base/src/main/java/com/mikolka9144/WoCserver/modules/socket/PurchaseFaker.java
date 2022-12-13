package com.mikolka9144.WoCserver.modules.socket;

import com.mikolka9144.WoCserver.model.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.WoCserver.model.Packet.FullPacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

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
