package com.mikolka9144.worldcraft.socket.model.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Errors.CreateRoomErrorCode;
import com.mikolka9144.worldcraft.socket.model.Errors.LoginErrorCode;
import com.mikolka9144.worldcraft.socket.model.Errors.RoomJoinError;
import com.mikolka9144.worldcraft.socket.Packet.Packet;

public abstract class ErrorPacketInterceptor extends CommandPacketInterceptor {
    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        if (packet.getError() != 0){
            PacketsFormula formula = new PacketsFormula();
            formula.addUpstream(packet);

            switch (packet.getCommand()){
                case S_CREATE_ROOM_RESP -> this.interceptErrorCreateRoom(packet, CreateRoomErrorCode.findErrorByCode(packet.getError()),formula);
                case S_LOGIN_RESP -> this.interceptErrorLogin(packet, LoginErrorCode.findErrorByCode(packet.getError()),formula);
                case S_JOIN_ROOM_RESP -> this.interceptErrorJoinRoom(packet, RoomJoinError.findErrorByCode(packet.getError()),formula);
                default -> { return super.InterceptRawPacket(packet); }
            }
        }
        return super.InterceptRawPacket(packet);
    }

    public void interceptErrorLogin(Packet packet, LoginErrorCode errorByCode, PacketsFormula formula) {
    }

    public void interceptErrorJoinRoom(Packet packet, RoomJoinError errorByCode, PacketsFormula formula) {

    }

    public void interceptErrorCreateRoom(Packet packet, CreateRoomErrorCode errorByCode, PacketsFormula formula) {

    }
}
