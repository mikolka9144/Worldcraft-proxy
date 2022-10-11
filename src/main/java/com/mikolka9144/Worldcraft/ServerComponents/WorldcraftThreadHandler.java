package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Models.EventCodecs.Packet;
import com.mikolka9144.Models.PacketInterceptor;
import com.mikolka9144.Models.WorldcraftSocket;
import com.mikolka9144.Worldcraft.ContentParsers.PacketContentDeserializer;

import java.io.IOException;
import java.util.List;

public class WorldcraftThreadHandler {
    protected void attachToThread(WorldcraftSocket socket, List<PacketInterceptor> socketInter){
        new Thread(() -> worldcraftClientHandler(socket,socketInter)).start();
    }
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter){
        try {
            while (true) {
                Packet packet = socket.getChannel().recive();
                for (PacketInterceptor interceptor:socketInter) {
                    interceptor.InterceptRawPacket(packet);
                    switch (packet.getCommand()){
                        case S_ROOM_LIST_RESP -> interceptor.InterceptRoomsPacket(packet,
                                PacketContentDeserializer.decodeRoomsData(packet.getData()));
                    }
                }
            }
        }
        catch (IOException x) {
            System.out.println(x);
            try {
                for (PacketInterceptor packetInterceptor : socketInter) {
                    packetInterceptor.close();
                }
            }
            catch (IOException y){
                System.out.println(y);
            }
        }
    }
}
