package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PopupMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomsPacket;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.model.VersionFlags;
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

                LoginInfo ret = pareLegacyLoginInfo(packet);

                packet.setData(PacketDataEncoder.login(ret));
            }
            return super.InterceptRawPacket(packet);
        }

        private static LoginInfo pareLegacyLoginInfo(Packet packet) {
            PacketDataReader reader = new PacketDataReader(packet.getData());
            return new LoginInfo(
                    reader.getString(),
                    reader.getShort(),
                    reader.getString(),
                    reader.getString(),
                    reader.getString(), reader.getString(), reader.getString(),"play");
        }
    }
    @Component("packet-conv-late")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class Late extends CommandPacketInterceptor {
        private VersionFlags flags;

        @Override
        public PacketsFormula InterceptRawPacket(Packet packet) {
            if (packet.getProtoId() != PacketProtocol.SERVER){
                if(flags == null) flags = new VersionFlags(packet.getProtoId());
            }
            return super.InterceptRawPacket(packet);
        }

        @Override
        public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {
            if(!flags.shoudIncludeReadOlnyRoomStatus){
                PacketDataBuilder builder = new PacketDataBuilder()
                        .append(data.getPacketIndex())
                        .append(data.getAllPackets())
                        .append(data.getInitialRoomListSize())
                        .append(data.getRoomType().getId());
                for (RoomsPacket.Room room : data.getRooms()) {
                    builder.append(room.getId())
                            .append(room.getName())
                            .append(room.isProtected())
                            .append(room.getActivePlayers())
                            .append(room.getRoomCapacity())
                            .append(room.getNumberOfEntrances())
                            .append(room.getLikes()); // We skip read olny status in package
                }
                packet.setData(builder.build());
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
