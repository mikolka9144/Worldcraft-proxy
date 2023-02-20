package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.RoomsPacket;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.logic.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.logic.PacketParsers.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;

import java.io.IOException;

public class PacketConverter  {
    public static class Early extends PacketInterceptor {

        public Early(WorldCraftPacketIO connectionIO) {
            super(connectionIO);
        }
        @Override
        public void InterceptRawPacket(Packet packet) {
            try {
                if(packet.getCommand() == PacketCommand.C_LOGIN_REQ){
                    PacketDataReader reader = new PacketDataReader(packet.getData());
                    LoginInfo ret = new LoginInfo(
                            reader.getString(),
                            reader.getShort(),
                            reader.getString(),
                            reader.getString(),
                            reader.getString(), reader.getString(), reader.getString(),"");

                    packet.setData(PacketContentSerializer.encodeLogin(ret));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            super.InterceptRawPacket(packet);
        }
    }
    public static class Late extends FullPacketInterceptor{

        public Late(WorldCraftPacketIO connectionIO) {
            super(connectionIO);
        }

        @Override
        public void interceptRoomsPacket(Packet packet, RoomsPacket data) {
            packet.setData(PacketContentSerializer.encodeRoomsData(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data) {
            packet.setData(data.getMessage().getBytes());
        }
    }

}
