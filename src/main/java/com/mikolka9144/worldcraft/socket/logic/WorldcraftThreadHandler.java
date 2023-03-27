package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.util.List;

@Slf4j
public abstract class WorldcraftThreadHandler {
    protected void attachToThread(WorldcraftSocket client, List<PacketInterceptor> socketInter, List<PacketInterceptor> loopbackInter) {
        new Thread(() -> worldcraftClientHandler(client, socketInter, loopbackInter)).start();
    }

    @SneakyThrows
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter, List<PacketInterceptor> server) {
        try {
            while (true) { //TODO
                Packet packet = socket.getChannel().recive();

                PacketsFormula baseFormula = new PacketsFormula();
                baseFormula.getUpstreamPackets().add(packet);
                baseFormula = executeInterceptors(socketInter, baseFormula);

                if (baseFormula.getWritebackPackets().isEmpty()) continue;

                PacketsFormula downstreamFormula = new PacketsFormula();
                downstreamFormula.getUpstreamPackets().addAll(baseFormula.getWritebackPackets());
                downstreamFormula = executeInterceptors(server, downstreamFormula);

                if (!downstreamFormula.getWritebackPackets().isEmpty()) {
                    log.error("Downstream packets generated more upstream packets.");
                    log.error("To avoid looping " + downstreamFormula.getWritebackPackets().size() + " pckets will be dropped");
                    log.error("Make sure, that interceptors are working as intended.");
                }
            }
        } catch (SocketException x) {
            log.warn(socket.getConnectedIp() + " closed connection");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) {
        } catch (IOException x) {
            log.error(x.getMessage());
        } finally {
            // if onClose throws an Exception, that will be his problem
            socket.close();
        }
    }

    private PacketsFormula executeInterceptors(List<PacketInterceptor> socketInter, PacketsFormula formula) {
        var currentFormula = formula;
        for (PacketInterceptor interceptor : socketInter) {
            var nextFormula = new PacketsFormula();

            for (Packet item : currentFormula.getUpstreamPackets()) {
                var resultingFormula = interceptor.InterceptRawPacket(item);
                nextFormula.add(resultingFormula);
            }
            nextFormula.getWritebackPackets().addAll(currentFormula.getWritebackPackets());
            currentFormula = nextFormula;
        }

        if (!currentFormula.getUpstreamPackets().isEmpty()) {
            log.warn("Packet furmula for client has remaining server packets. Did everything got sent?");
        }
        return currentFormula;
    }
}
