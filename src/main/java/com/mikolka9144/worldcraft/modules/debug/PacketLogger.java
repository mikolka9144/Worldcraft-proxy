package com.mikolka9144.worldcraft.modules.debug;

import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketCommand;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.common.api.packet.codecs.ClientBuildManifest;
import com.mikolka9144.worldcraft.common.api.packet.codecs.MovementPacket;
import com.mikolka9144.worldcraft.common.api.packet.codecs.PlayerAction;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("packet-logger")
public class PacketLogger extends CommandPacketInterceptor {

//    @Override
//    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {
//        log.info("--------------------------------");
//        log.info("Room Type: "+data.getRoomType() +"("+data.getRoomType().ordinal()+")");
//        log.info("Packet Index: "+data.getPacketIndex());
//        log.info("Packet Count: "+data.getAllPackets());
//        log.info("Init Size: "+data.getInitialRoomListSize());
//        log.info("Rooms Count: "+data.getRooms().size());
//        log.info("--------------------------------");
//    }

    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        log.info("--------------------------------");
        log.info("Proto: "+packet.getProtoId() +"("+packet.getProtoId().getProto()+")");
        log.info("Error code: "+packet.getErrorCode());
        log.info("Command: "+packet.getCommand()+" ("+packet.getCommand().getCommand()+")");
        log.info("PlayerId: "+packet.getPlayerId());
        log.info("Msg: "+packet.getMsg());
        log.info("Data lenght: "+ packet.getData().length);
        log.info("--------------------------------");
        return super.interceptRawPacket(packet);
    }

    @Override
    public void interceptUnknownPacket(Packet packet, PacketsFormula formula) {
        log.info("--------------------------------");
        log.info("Proto: "+packet.getProtoId() +"("+packet.getProtoId().getProto()+")");
        log.info("Error code: "+packet.getErrorCode());
        log.info("Command: "+packet.getCommand()+" ("+packet.getCommand().getCommand()+")");
        log.info("PlayerId: "+packet.getPlayerId());
        log.info("Msg: "+packet.getMsg());
        log.info("Data: "+ Arrays.toString(packet.getData()));
        log.info("--------------------------------");
        if(packet.getCommand() == PacketCommand.C_JOIN_ROOM_REQ){
            packet.setMsg("");
            log.info("Stripping msg!!!");
        }
        super.interceptUnknownPacket(packet, formula);
    }

    @Override
    public void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula) {
        super.interceptEnemyPosition(packet, data, formula);
    }


    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        log.info("Connected client ver: "+ clientBuildManifest.getClientVersion());
    }



    @Override
    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
        log.info(String.format("%d:player %d did %s",packet.getPlayerId(),playerAction.getPlayerId(),playerAction.getActionType().toString()));
    }
}
