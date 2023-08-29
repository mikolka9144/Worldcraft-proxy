package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.PurchaseValidationResp;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PurchasesList;
import com.mikolka9144.worldcraft.socket.model.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("purchase-faker")
@Slf4j
public class PurchaseFaker extends FullPacketInterceptor {
    @Override
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {
        data.setStatus(PurchaseValidationResp.Status.SUCSESS);
        packet.setData(PacketContentSerializer.encodeValidatePurchaseResp(data));
    }

    @Override
    public void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet);
        PurchasesList list = new PurchasesList("12323212","100000","9144","10");
        Packet purchases = new Packet(PacketProtocol.SERVER,0, PacketCommand.S_LOAD_PURCHASES_RES,"",(byte)0,new byte[0]);
        purchases.setData(PacketContentSerializer.encodePurchaseLoadResponse(list));
        formula.addWriteback(purchases);
    }

    @Override
    public void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet);
    }
}