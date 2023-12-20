package com.mikolka9144.worldcraft.backend.server.unify.convert;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import org.springframework.stereotype.Component;

@Component("gameVer-spoofer")
public class GameVersionSpoofer extends CommandPacketInterceptor {
    @Override
    public void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {
        // This line suppresses any "Critical update alerts", because we have this class to convert XD
        packet.setErrorCode((byte) 0);
    }
}
