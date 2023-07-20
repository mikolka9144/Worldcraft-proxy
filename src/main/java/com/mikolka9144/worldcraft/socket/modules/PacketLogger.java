package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginResponse;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PlayerAction;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component("packet-logger")
public class PacketLogger extends FullPacketInterceptor {

    @Override
    public void interceptUnknownPacket(Packet packet, PacketsFormula formula) {
        log.info("--------------------------------");
        log.info("Proto: "+packet.getProtoId());
        log.info("Error code: "+packet.getError());
        log.info("Command: "+packet.getCommand());
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
        //System.out.println(data.toString());
        super.interceptEnemyPosition(packet, data, formula);
    }

    @Override
    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
        log.info(String.format("%d:player %d did %s",packet.getPlayerId(),playerAction.getPlayerId(),playerAction.getActionType().toString()));
        super.interceptPlayerActionReq(packet, playerAction, formula);
    }

    @Override
    public void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {
        super.interceptLoginResp(packet, loginResponse, formula);
    }
}
