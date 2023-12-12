package com.mikolka9144.worldcraft.socket.server;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.worldcraft.socket.WorldcraftClient;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.socket.interceptor.PacketAlteringModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.List;
/**
 * Blob-like class containing logic for sending packets.
 * This class is responsible for handling {@link PacketsFormula}
 */
@Slf4j
public class WorldcraftThread implements Closeable {
    @Getter
    private final WorldcraftClient connection;
    private final List<PacketAlteringModule> upstreamInterceptors;
    private final List<PacketAlteringModule> downstreamInterceptors;

    /**
     *
     * @param socket connection to handle
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     */
    public WorldcraftThread(WorldcraftSocket socket, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors) {
        this.connection = new WorldcraftClient(socket,this::sendPacket);
        this.upstreamInterceptors = upstreamInterceptors;
        this.downstreamInterceptors = downstreamInterceptors;
    }
    /**
     * Attaches {@code handleClientSocket} to a runnable thread
     */
    public void startThread() {
        connection.start();
    }

    /**
     * This method sends given packet as if it was sent by a client.
     * @param packet packet to send
     * @param upstreamInterceptors List of {@link PacketAlteringModule} to use for packets received from {@code socket}
     * @param downstreamInterceptors List of {@link PacketAlteringModule} to use for packets sent to {@code socket}
     */
    public static void sendPacket(Packet packet, List<PacketAlteringModule> upstreamInterceptors, List<PacketAlteringModule> downstreamInterceptors){
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
                var resultingFormula = interceptor.interceptRawPacket(item);
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
    public void close() {
        connection.close();
    }
}
