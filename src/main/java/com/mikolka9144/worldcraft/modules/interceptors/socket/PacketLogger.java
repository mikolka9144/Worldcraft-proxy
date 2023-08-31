package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import com.mikolka9144.worldcraft.socket.model.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component("packet-logger")
public class PacketLogger extends FullPacketInterceptor {

    @Override
    public void interceptUnknownPacket(Packet packet, PacketsFormula formula) {
        log.info("--------------------------------");
        log.info("Proto: "+packet.getProtoId() +"("+packet.getProtoId().getProto()+")");
        log.info("Error code: "+packet.getError());
        log.info("Command: "+packet.getCommand()+" ("+packet.getCommand().getCommand()+")");
        log.info("PlayerId: "+packet.getPlayerId());
        log.info("Msg: "+packet.getMessage());
        log.info("Data: "+ Arrays.toString(packet.getData()));
        log.info("--------------------------------");
        if(packet.getCommand() == PacketCommand.C_JOIN_ROOM_REQ){
            packet.setMessage("");
            log.info("Stripping msg!!!");
        }
        super.interceptUnknownPacket(packet, formula);
    }

    @Override
    public void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula) {
        super.interceptEnemyPosition(packet, data, formula);
    }


    @Override
    public void interceptVersionCheckReq(Packet packet, ClientVersion clientVersion, PacketsFormula formula) {
        log.info("Connected client ver: "+clientVersion.getClientVersion());
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
        super.interceptLogin(packet, data, formula);
    }

    @Override
    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
        log.info(String.format("%d:player %d did %s",packet.getPlayerId(),playerAction.getPlayerId(),playerAction.getActionType().toString()));
        super.interceptPlayerActionReq(packet, playerAction, formula);
    }

    @Override
    public void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {
        loginResponse.setPurchaseValidated(true);

        packet.setData(PacketContentSerializer.encodeLoginResponse(loginResponse));
    }
}
