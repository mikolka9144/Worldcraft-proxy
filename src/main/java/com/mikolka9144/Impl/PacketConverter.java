package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.EventCodecs.LoginInfo;
import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketCommand;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Utills.PacketParsers.PacketDataReader;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

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
        @Override
        public void close() throws IOException {

        }
    }
    public static class Late extends FullPacketInterceptor{

        public Late(WorldCraftPacketIO connectionIO) {
            super(connectionIO);
        }

        @Override
        public void close() throws IOException {

        }


        @Override
        public void interceptRoomsPacket(Packet packet, RoomsPacket data) {
            packet.setData(PacketContentSerializer.encodeRoomsData(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data) {
            packet.setData(PacketContentSerializer.encodeChatMessage(data,PacketProtocol.WORLDCRAFT_V_2_8_7));
        }
    }

}
