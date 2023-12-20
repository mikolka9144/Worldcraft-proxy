package com.mikolka9144.worldcraft.hacked.modules.hackoring.fakers;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.backend.packets.codecs.PlayerAction;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("blocker")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockFaker extends CommandPacketInterceptor {
    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
        data.setY((short) (data.getY()+2));
        formula.addUpstream(packager.sendBlockClientPacket(data ));
    }

    @Override
    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet);
    }
}
