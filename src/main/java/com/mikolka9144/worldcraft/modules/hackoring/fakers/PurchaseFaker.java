package com.mikolka9144.worldcraft.modules.hackoring.fakers;

import com.mikolka9144.worldcraft.common.api.packet.codecs.PurchaseValidationResp;
import com.mikolka9144.worldcraft.common.api.packet.codecs.PurchasesList;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketCommand;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("purchase-faker")
@Slf4j
public class PurchaseFaker extends CommandPacketInterceptor {
    @Override
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {
        data.setStatus(PurchaseValidationResp.Status.SUCSESS);
        packet.setData(PacketDataEncoder.validatePurchaseResp(data));
    }

    @Override
    public void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet);
        PurchasesList list = new PurchasesList("12323212","100000","9144","10");
        Packet purchases = new Packet(PacketProtocol.SERVER,0, PacketCommand.S_LOAD_PURCHASES_RES,"",(byte)0,new byte[0]);
        purchases.setData(PacketDataEncoder.purchaseLoadResponse(list));
        formula.addWriteback(purchases);
    }

    @Override
    public void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet);
    }
}
