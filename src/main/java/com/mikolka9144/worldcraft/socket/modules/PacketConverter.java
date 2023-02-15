package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomsPacket;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class PacketConverter  {
    private PacketConverter(){}
    public static class Early extends PacketInterceptor {

        @Override
        public PacketsFormula InterceptRawPacket(Packet packet) {
            if(packet.getCommand() == PacketCommand.C_LOGIN_REQ){
                PacketDataReader reader = new PacketDataReader(packet.getData());
                LoginInfo ret = new LoginInfo(
                        reader.getString(),
                        reader.getShort(),
                        reader.getString(),
                        reader.getString(),
                        reader.getString(), reader.getString(), reader.getString(),"play");

                packet.setData(PacketContentSerializer.encodeLogin(ret));
            }
            return super.InterceptRawPacket(packet);
        }
    }
    public static class Late extends FullPacketInterceptor{

        @Override
        public void interceptRoomsPacket(Packet packet, RoomsPacket data, PacketsFormula formula) {
            packet.setData(PacketContentSerializer.encodeRoomsData(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
            packet.setData(data.getMessage().getBytes());
        }
    }

}
