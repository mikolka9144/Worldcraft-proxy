package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.VersionFlags;
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
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
public class PacketConverter  {


    private PacketConverter(){}
    @Component("packet-conv-early")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Early extends PacketAlteringModule {

        @Override
        public PacketsFormula InterceptRawPacket(Packet packet) {
            if(packet.getCommand() == PacketCommand.C_LOGIN_REQ){
                VersionFlags flags = new VersionFlags(packet.getProtoId());
                if (!flags.shoudFixLoginMarket) return super.InterceptRawPacket(packet);

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
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Late extends FullPacketInterceptor{
        private VersionFlags flags;

        @Override
        public PacketsFormula InterceptRawPacket(Packet packet) {
            if (packet.getProtoId() != PacketProtocol.SERVER){
                if(flags == null) flags = new VersionFlags(packet.getProtoId());
            }
            return super.InterceptRawPacket(packet);
        }

        @Override
        public void interceptRoomsPacket(Packet packet, RoomsPacket data, PacketsFormula formula) {
            if(!flags.shoudIncludeReadOlnyRoomStatus){
                packet.setData(PacketContentSerializer.encodeRoomsData(data,false));
            }
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
            if (flags.simplifyChat){
                packet.setData(data.getMessage().getBytes());
            }
        }

        @Override
        public void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {
            if (flags.simplifyAlerts){
                packet.setData(popupMessage.getBaseMessage().getBytes());
            }
        }
    }

}
