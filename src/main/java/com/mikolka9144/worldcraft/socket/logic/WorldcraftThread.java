package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.List;
/**
 * Blob-like class containing logic for sending packets.
 * This class is responsible for handling {@link PacketsFormula}
 */
@Slf4j
public class WorldcraftThread implements Closeable {
    private final WorldcraftSocket socket;
    private final List<PacketAlteringModule> upstreamInterceptors;
    private final List<PacketAlteringModule> downstreamInterceptors;
    private boolean haltThread = false;

    /**
     *
     * @param socket connection to handle
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     */
    public WorldcraftThread(WorldcraftSocket socket, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors) {
        this.socket = socket;
        this.upstreamInterceptors = upstreamInterceptors;
        this.downstreamInterceptors = downstreamInterceptors;
    }
    /**
     * Attaches {@code handleClientSocket} to a runnable thread
     * @return A thread to execute {@code handleClientSocket}
     */
    public Thread attachToThread() {
        return new Thread(this::handleThread, socket.getConnectedIp());
    }

    /**
     * This method starts packet receiving and interpretation from given socket.
     * This method is a blocking one, so it's recommended to create a separate thread for it or use {@code attachToThread()} method.

     */
    @SneakyThrows
    public void handleThread(){
        try {
            while (!haltThread) {
                    Packet initialPacket = socket.getChannel().receive();
                    sendPacket(initialPacket);
            }
            log.info(String.format("Connection for %s was HALTED!",socket.getConnectedIp()));
        } catch (IOException x) {
            log.warn(socket.getConnectedIp() + " had IO Exception");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) { // Duplicate exception
            log.warn(socket.getConnectedIp() + " had Buffer underflow");
        } finally {
            // if onClose throws an Exception, that will be his problem
            socket.close();
            log.warn(socket.getConnectedIp() + " closed!");
        }
    }

    /**
     * This method sends given packet as if it was sent by a client.
     * @param packet packet to send
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     */
    public static void sendPacket(Packet packet,List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors){
        PacketsFormula baseFormula = executeComunication(upstreamInterceptors, List.of(packet));

        if (baseFormula.getWritebackPackets().isEmpty()) return;

        PacketsFormula downstreamFormula = executeComunication(downstreamInterceptors,baseFormula.getWritebackPackets());

        if (!downstreamFormula.getWritebackPackets().isEmpty()) {
            log.error("Downstream packets generated more upstream packets.");
            log.error("To avoid looping " + downstreamFormula.getWritebackPackets().size() + " packets will be dropped");
            log.error("Make sure, that interceptors are working as intended.");
        }
    }
/**
 * This method sends given packet as if it was sent by a client.
 * @param packet packet to send
 */
    public void sendPacket(Packet packet){
        sendPacket(packet,upstreamInterceptors,downstreamInterceptors);
    }
    private static PacketsFormula executeComunication(List<PacketAlteringModule> socketInter, List<Packet> packets) {
        PacketsFormula baseFormula = new PacketsFormula();
        baseFormula.getUpstreamPackets().addAll(packets);
        baseFormula = executeInterceptors(socketInter, baseFormula);
        return baseFormula;
    }

    private static PacketsFormula executeInterceptors(List<PacketAlteringModule> socketInter, PacketsFormula formula) {
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

    @Override
    public void close() throws IOException {
        haltThread = true;
        socket.close();
    }
}
