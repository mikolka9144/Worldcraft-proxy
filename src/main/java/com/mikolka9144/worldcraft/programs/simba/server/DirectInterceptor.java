package com.mikolka9144.worldcraft.programs.simba.server;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.*;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.Packet.PacketCommand;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("direct-interceptor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DirectInterceptor extends CommandPacketInterceptor {

    @Override
    public void interceptPingReq(Packet packet, PacketsFormula formula) {
        addToFormula(formula,PacketCommand.S_PING_RESP,new byte[0]);
    }

    @Override
    public void interceptVersionCheckReq(Packet packet, ClientVersion clientVersion, PacketsFormula formula) {
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
        RoomsPacket rooms = new RoomsPacket(0,1,(short)20,data.getRoomsType());
        RoomsPacket.Room room = new RoomsPacket.Room(
                1,
                "simba",
                false,
                (short)200,
                (short)205,
                20,
                69,
                false
        );
        rooms.getRooms().add(room);
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
