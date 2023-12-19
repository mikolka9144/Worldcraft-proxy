package com.mikolka9144.worldcraft.simba;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.codecs.*;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import com.mikolka9144.worldcraft.backend.base.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.spring.socket.interceptor.CommandPacketInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("direct-interceptor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DirectInterceptor extends CommandPacketInterceptor {
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        PacketsFormula form = super.interceptRawPacket(packet);
        form.getUpstreamPackets().removeIf(s -> s.getProtoId() != PacketProtocol.SERVER);
        return form;
    }

    @Override
    public void interceptPingReq(Packet packet, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_PING_RESP,new byte[0]);
    }

    @Override
    public void interceptCreateRoomReq(Packet packet, RoomCreateReq roomCreateReq, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_CREATE_ROOM_RESP,new byte[]{0,2,56,57});
    }

    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_CHECK_VERSION_RESP,new byte[1]);
    }

    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        LoginResponse resp = new LoginResponse();
        resp.setPlayerId(0);
        resp.setPlayerName(data.getUsername());
        resp.setPurchaseValidated(false);

        addToFormula(formula,
                PacketCommand.S_LOGIN_RESP,
                PacketDataEncoder.loginResponse(resp)
        );
    }

    @Override
    public void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_PLAYER_GRAPHICS_INITED_RESP,new byte[2]);
    }

    @Override
    public void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_JOIN_ROOM_RESP, PacketDataEncoder.joinRoomResponse(new JoinRoomResponse(true,false)));
    }

    @Override
    public void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {
        RoomsPacket rooms = new RoomsPacket(0,0,(short)20,data.getRoomsType());
        rooms.getRooms().add(new RoomsPacket.Room(
                1,
                "simba",
                false,
                (short)0,
                (short)100,
                20,
                69,
                false
        ));
        rooms.getRooms().add(new RoomsPacket.Room(
                2,
                "exp2",
                false,
                (short)0,
                (short)100,
                0,
                0,
                false
        ));
        rooms.getRooms().add(new RoomsPacket.Room(
                3,
                "ezz",
                false,
                (short)0,
                (short)100,
                0,
                0,
                false
        ));
        rooms.getRooms().add(new RoomsPacket.Room(
                10,
                "pumba",
                false,
                (short)0,
                (short)100,
                0,
                69,
                false
        ));
        addToFormula(formula,PacketCommand.S_ROOM_LIST_RESP, PacketDataEncoder.roomsQueryResponse(rooms));
    }

    @Override
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_PLAYER_MOVE_RESP,new byte[0]);
    }

    private void addToFormula(PacketsFormula formula, PacketCommand command, byte[] data){
        Packet varpacket = packager.serverPacket(command, data);
        formula.addWriteback(varpacket);
    }
}
