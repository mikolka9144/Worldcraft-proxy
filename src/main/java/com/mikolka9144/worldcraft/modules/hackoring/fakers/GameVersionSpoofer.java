package com.mikolka9144.worldcraft.modules.hackoring.fakers;

import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.common.api.packet.codecs.ClientBuildManifest;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import org.springframework.stereotype.Component;

@Component("gameVer-spoofer")
public class GameVersionSpoofer extends CommandPacketInterceptor {
    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        clientBuildManifest.setClientVersion(100);
        packet.setData(PacketDataEncoder.versionCheckReq(clientBuildManifest));
    }
}
