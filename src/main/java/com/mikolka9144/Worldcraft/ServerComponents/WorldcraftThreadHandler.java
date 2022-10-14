package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Models.EventCodecs.Packet;
import com.mikolka9144.Models.EventCodecs.PacketInterceptor;
import com.mikolka9144.Models.WorldcraftSocket;

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
