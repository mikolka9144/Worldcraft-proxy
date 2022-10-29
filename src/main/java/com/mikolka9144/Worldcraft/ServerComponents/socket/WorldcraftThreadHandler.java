package com.mikolka9144.Worldcraft.ServerComponents.socket;

import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.Packet.WorldcraftSocket;
import com.mikolka9144.Utills.PacketAlreadyInterceptedException;

import java.io.IOException;
import java.util.List;

public abstract class WorldcraftThreadHandler {
    protected void attachToThread(WorldcraftSocket socket, List<PacketInterceptor> socketInter){
        new Thread(() -> worldcraftClientHandler(socket,socketInter)).start();
    }
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter){
        try {
            while (true) {
                Packet packet = socket.getChannel().recive();
                for (PacketInterceptor interceptor:socketInter) {
                    try {
                        interceptor.InterceptRawPacket(packet);
                    }
                    catch (PacketAlreadyInterceptedException ignored){break;}
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
