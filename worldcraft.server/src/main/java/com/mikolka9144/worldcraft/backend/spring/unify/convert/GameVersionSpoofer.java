package com.mikolka9144.worldcraft.backend.spring.unify.convert;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.worldcraft.backend.base.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.spring.socket.interceptor.CommandPacketInterceptor;
import org.springframework.stereotype.Component;

@Component("gameVer-spoofer")
public class GameVersionSpoofer extends CommandPacketInterceptor {
    @Override
    public void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {
        // This line suppresses any "Critical update alerts", because we have this class to convert XD
        packet.setErrorCode((byte) 0);
    }
}
