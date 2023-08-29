package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.util.List;
/**
 * Blob-like abstract class containing logic for sending packets.
 * This class is responsible for handling {@link PacketsFormula}
 */
@Slf4j
public abstract class WorldcraftThreadHandler {
    /**
     * Attaches {@code handleClientSocket} to a runnable thread
     * @param socket connection to handle
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     * @return A thread to execute {@code handleClientSocket}
     */
    protected Thread attachToThread(WorldcraftSocket socket, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors) {
        return new Thread(() -> handleClientSocket(socket, upstreamInterceptors, downstreamInterceptors), socket.getConnectedIp());
    }

    /**
     * This method starts packet receiving and interpretation from given socket.
     * This method is a blocking one, so it's recommended to create a separate thread for it or use {@code attachToThread()} method.
     * @param socket connection to handle
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     */
    @SneakyThrows
    protected void handleClientSocket(WorldcraftSocket socket, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors) {
        try {
            //Before someone whines at this loop:
            // "socket.getChannel().receive()" is a blocking method, that either
            // 1. Returns a packet
            // 2. yeets an exception
            while (true) {
                    Packet initialPacket = socket.getChannel().receive();
                    sendPacket(initialPacket,upstreamInterceptors,downstreamInterceptors);
            }
        } catch (SocketException x) {
            log.warn(socket.getConnectedIp() + " closed connection");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) { // Duplicate exception
        } catch (IOException x) {
            log.error(x.getMessage());
        } finally {
            // if onClose throws an Exception, that will be his problem
            socket.close();
        }
    }
    /**
     * This method sends given packet as if it was sent by a client.
     * @param packet packet to send
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code target}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code target}
     */
    protected void sendPacket(Packet packet,List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors){
        PacketsFormula baseFormula = executeComunication(upstreamInterceptors, List.of(packet));

        if (baseFormula.getWritebackPackets().isEmpty()) return;

        PacketsFormula downstreamFormula = executeComunication(downstreamInterceptors,baseFormula.getWritebackPackets());

        if (!downstreamFormula.getWritebackPackets().isEmpty()) {
            log.error("Downstream packets generated more upstream packets.");
            log.error("To avoid looping " + downstreamFormula.getWritebackPackets().size() + " packets will be dropped");
            log.error("Make sure, that interceptors are working as intended.");
        }
    }
    private PacketsFormula executeComunication(List<PacketAlteringModule> socketInter, List<Packet> packets) {
        PacketsFormula baseFormula = new PacketsFormula();
        baseFormula.getUpstreamPackets().addAll(packets);
        baseFormula = executeInterceptors(socketInter, baseFormula);
        return baseFormula;
    }

    private PacketsFormula executeInterceptors(List<PacketAlteringModule> socketInter, PacketsFormula formula) {
        var currentFormula = formula;
        for (PacketAlteringModule interceptor : socketInter) {
            var nextFormula = new PacketsFormula();

            for (Packet item : currentFormula.getUpstreamPackets()) {
                var resultingFormula = interceptor.InterceptRawPacket(item);
                nextFormula.add(resultingFormula);
            }
            nextFormula.getWritebackPackets().addAll(currentFormula.getWritebackPackets());
            currentFormula = nextFormula;
        }

        if (!currentFormula.getUpstreamPackets().isEmpty()) {
            log.warn("Packet formula for client has remaining server packets. Did everything got sent?");
        }
        return currentFormula;
    }
}
