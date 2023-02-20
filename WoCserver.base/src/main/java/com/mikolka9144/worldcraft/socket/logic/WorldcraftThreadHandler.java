package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.model.PacketAlreadyInterceptedException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.util.List;

@Slf4j
public abstract class WorldcraftThreadHandler {
    protected void attachToThread(WorldcraftSocket socket, List<PacketInterceptor> socketInter,Runnable onClose){
        new Thread(() -> worldcraftClientHandler(socket,socketInter,onClose)).start();
    }
    @SneakyThrows
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter, Runnable onClose) {
        try {
            while (true) {
                    Packet packet = socket.getChannel().recive();
                for (PacketInterceptor interceptor:socketInter) {
                    try {
                        interceptor.InterceptRawPacket(packet);
                    }
                    catch (PacketAlreadyInterceptedException ignored){break;} //TODO change the way of cancelling execution
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
            // if onClose throws an Exception, that will be his problem
            onClose.run();
        }
    }
}
