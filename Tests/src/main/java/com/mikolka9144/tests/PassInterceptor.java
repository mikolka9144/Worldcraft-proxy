package com.mikolka9144.tests;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.*;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.packets.errorcodes.VersionCheckErrorCode;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("pass")
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PassInterceptor extends CommandPacketInterceptor {

    public static final String TAINT = "taint";

    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        PacketsFormula formula = super.interceptRawPacket(packet);
        if(!packet.getMsg().equals(TAINT)){
            formula.clearInitialPacket();
        }
        return formula;
    }

    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        formula.addWriteback(packager.checkVersionResp(VersionCheckErrorCode.NO_ERROR,""));
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        formula.addWriteback(packager.respondToLogin(0, data.getUsername()));
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {
        formula.getWritebackPackets().addAll(packager.respondWithRooms(List.of(new RoomsPacket.Room(
                0,
                "test",
                false,
                (short) 0,
                (short) 0,
                0,
                0,
                false
        ),
                new RoomsPacket.Room(
                        2,
                        "test",
                        false,
                        (short) 0,
                        (short) 0,
                        0,
                        0,
                        false
                )
                ),data.getRoomsType(),5));
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {
        formula.addWriteback(packager.serverPacket(PacketCommand.SERVER_ROOM_JOIN_RESP, PacketDataEncoder.joinRoomResponse(
                new JoinRoomResponse(true,false))));
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {
        formula.addWriteback(packager.movePlayerResponse());
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
        formula.addWriteback(packager.respondToClientBlock());
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {
        formula.addWriteback(packager.readyPlayerResp());
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }

    @Override
    public void interceptPingReq(Packet packet, PacketsFormula formula) {
        formula.addWriteback(packager.pong());
        formula.getWritebackPackets().get(0).setMsg(TAINT);
    }
}