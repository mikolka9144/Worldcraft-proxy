package com.mikolka9144.worldcraft.socket.interceptor;

import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.common.api.packet.errorcodes.CreateRoomErrorCode;
import com.mikolka9144.worldcraft.common.api.packet.errorcodes.LoginErrorCode;
import com.mikolka9144.worldcraft.common.api.packet.errorcodes.RoomJoinError;
import com.mikolka9144.worldcraft.common.api.packet.Packet;

public abstract class ErrorPacketInterceptor extends CommandPacketInterceptor {
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if (packet.getErrorCode() != 0){
            PacketsFormula formula = new PacketsFormula();
            formula.addUpstream(packet);

            switch (packet.getCommand()){
                case S_CREATE_ROOM_RESP -> this.interceptErrorCreateRoom(packet, CreateRoomErrorCode.findErrorByCode(packet.getErrorCode()),formula);
                case S_LOGIN_RESP -> this.interceptErrorLogin(packet, LoginErrorCode.findErrorByCode(packet.getErrorCode()),formula);
                case S_JOIN_ROOM_RESP -> this.interceptErrorJoinRoom(packet, RoomJoinError.findErrorByCode(packet.getErrorCode()),formula);
                default -> interceptUnknownErrorPacket(packet,formula);
            }
        }
        return super.interceptRawPacket(packet);
    }

    public void interceptErrorLogin(Packet packet, LoginErrorCode errorByCode, PacketsFormula formula) {
    }

    public void interceptErrorJoinRoom(Packet packet, RoomJoinError errorByCode, PacketsFormula formula) {

    }

    public void interceptErrorCreateRoom(Packet packet, CreateRoomErrorCode errorByCode, PacketsFormula formula) {

    }
    public void interceptUnknownErrorPacket(Packet packet, PacketsFormula formula) {

    }
}
