package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PopupMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomsPacket;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class PacketConverter  {
    private PacketConverter(){}
    @Component("packet-conv-early")
    public static class Early extends PacketAlteringModule {

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
    @Component("packet-conv-late")
    public static class Late extends FullPacketInterceptor{

        @Override
        public void interceptRoomsPacket(Packet packet, RoomsPacket data, PacketsFormula formula) {
            packet.setData(PacketContentSerializer.encodeRoomsData(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
            packet.setData(data.getMessage().getBytes());
        }

        @Override
        public void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {
            packet.setData(popupMessage.getBaseMessage().getBytes());
        }
    }

}
