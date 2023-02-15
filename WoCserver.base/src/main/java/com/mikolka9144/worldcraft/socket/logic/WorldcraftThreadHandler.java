package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
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
    protected void attachToThread(WorldcraftSocket client, List<PacketInterceptor> socketInter, PacketServer server){
        new Thread(() -> worldcraftClientHandler(client,socketInter,server)).start();
    }
    @SneakyThrows
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter, PacketServer server) {
        try {
            while (true) {
                    Packet packet = socket.getChannel().recive();
                for (PacketInterceptor interceptor:socketInter) {
                    PacketsFormula resultingFormula = interceptor.InterceptRawPacket(packet);
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
