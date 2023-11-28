package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ClientVersion;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import org.springframework.stereotype.Component;

@Component("gameVer-spoofer")
public class GameVersionSpoofer extends CommandPacketInterceptor {
    @Override
    public void interceptVersionCheckReq(Packet packet, ClientVersion clientVersion, PacketsFormula formula) {
        clientVersion.setClientVersion(100);
        packet.setData(PacketDataEncoder.versionCheckReq(clientVersion));
    }
}
