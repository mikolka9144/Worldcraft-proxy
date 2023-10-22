package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomListRequest;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomsPacket;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import org.springframework.stereotype.Component;

@Component("experiments")
public class PlayGroundInterceptor extends CommandPacketInterceptor {
    @Override
    public void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {
//        if (data.getRoomsType() == RoomListRequest.RoomsType.MOST_RATED){
//            RoomListRequest request = new RoomListRequest(RoomListRequest.RoomsType.REV1,0);
//            formula.addUpstream(new Packet(
//                    packager.getClientProto(),
//                    0,
//                    PacketCommand.C_ROOM_LIST_REQ,
//                    "",
//                    (byte) 0,
//                    PacketContentSerializer.encodeRoomsReq(request)));
//        }
    }

    @Override
    public void interceptRoomsPacket(Packet packet, RoomsPacket data, PacketsFormula formula) {
        if (data.getRoomType() == (byte)2) {
            System.out.println("2:   :");
            data.getRooms().forEach(System.out::println);
        }
    }
}
