package com.mikolka9144.WoCserver.logic.socket;

import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.WorldcraftSocket;
import com.mikolka9144.WoCserver.utills.PacketAlreadyInterceptedException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.util.List;
@Slf4j
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
            log.warn(socket.getConnectedIp()+" closed connection");
            log.debug(x.getMessage());
        }
        catch (BufferUnderflowException ignore){}
        catch (IOException x) {
            log.error(x.getMessage());
        }
        finally {
            try {
                for (PacketInterceptor packetInterceptor : socketInter) {
                    packetInterceptor.close();
                }
            }
            catch (IOException y){
                log.error("Failed to close socket for "+socket.getConnectedIp());
            }
        }
    }
}
