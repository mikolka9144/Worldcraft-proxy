package com.mikolka9144.worldcraft.hacked.modules.debug;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.packets.codecs.ClientBuildManifest;
import com.mikolka9144.worldcraft.backend.packets.codecs.LoginInfo;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.ErrorPacketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component("position-logger")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class PositionLogger extends ErrorPacketInterceptor {

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        log.info(String.format("Player %s said, that: {%s}",data.getPlayerNicknameArg(),data.getMessage()));
    }

    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        log.info("--------------------------------");
        log.info("Android API value: "+data.getAndroidAPI());
        log.info("Client version: "+data.getClientVer());
        log.info("Device Id: "+data.getDeviceId());
        log.info("Build source: "+data.getMarketName());
        log.info("Username: "+ data.getUsername());
        log.info("--------------------------------");
    }

    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        log.info("--------------------------------");
        log.info("Build Version: "+ clientBuildManifest.getClientVersion());
        log.info("Build source: "+ clientBuildManifest.getMarket());
        log.info("--------------------------------");
    }

    @Override
    public void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {
        log.info(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(packet.getData())).toString());
        packet.setData(PacketDataEncoder.roomCreateResp("NO DICK, NO BALLS, and probably no butthole since this guy feeds on radiation *vine boom*"));
        packet.setErrorCode((byte) -1);
        super.interceptVersionCheckResponse(packet, formula);
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
        log.info(String.format("putting %d(%s)",data.getBlockType(),data.getBlockName()));
    }
}
