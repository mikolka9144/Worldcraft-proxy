package com.mikolka9144.WoCserver.logic.socket;

import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.model.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.WorldcraftSocket;
import com.mikolka9144.WoCserver.utills.PacketAlreadyInterceptedException;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
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
        catch (SocketException x){
            System.out.println("Thread died");
        }
        catch (BufferUnderflowException ignore){}
        catch (IOException x) {
            System.out.println(x);
        }
        finally {
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