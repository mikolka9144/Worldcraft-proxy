package com.mikolka9144.worldcraft.backend.client.socket;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.PacketInterceptor;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

/**
 * Blob-like class containing logic for sending packets.
 * This class is responsible for handling {@link PacketsFormula}
 */
@Slf4j
@RequiredArgsConstructor
public class WorldcraftThread {
    @Getter
    private final List<PacketInterceptor> interceptors;
    private final Consumer<Packet> onClientReturn;
    private final boolean isServer;
    /**
     * This method sends given packet as if it was sent by a client.
     *
     * @param packet                 packet to send
     */
    public void sendClientPacket(Packet packet) {
        sendClientPacket(List.of(packet));
    }
    public void sendClientPacket(List<Packet> packet) {
        PacketsFormula baseFormula = executeComunication(interceptors, packet);

        if (!baseFormula.getUpstreamPackets().isEmpty()) {
            log.warn("Packet formula for client has remaining server packets. Did everything got sent?");
        }

        if (baseFormula.getWritebackPackets().isEmpty()) return;
        sendServerPacket(baseFormula.getWritebackPackets());
    }
    /**
     * This method sends given packet as if it was sent by a server.
     *
     * @param packet                 packet to send
     */
    public void sendServerPacket(Packet packet) {
        sendServerPacket(List.of(packet));
    }


    public void sendServerPacket(List<Packet> packets) {
        PacketsFormula downstreamFormula = executeComunication(interceptors, packets);
        downstreamFormula.getUpstreamPackets().forEach(onClientReturn);

        if (!downstreamFormula.getWritebackPackets().isEmpty()) {
            if (isServer){
                log.error("Downstream packets generated more upstream packets.");
                log.error("To avoid looping " + downstreamFormula.getWritebackPackets().size() + " packets will be dropped");
                log.error("Make sure, that interceptors are working as intended.");
            }
            else {
                sendServerPacket(downstreamFormula.getWritebackPackets());
            }

        }
    }

    private PacketsFormula executeComunication(List<PacketInterceptor> socketInter, List<Packet> packets) {
        PacketsFormula baseFormula = new PacketsFormula();
        baseFormula.getUpstreamPackets().addAll(packets);
        baseFormula = executeInterceptors(socketInter, baseFormula);
        return baseFormula;
    }

    private PacketsFormula executeInterceptors(List<PacketInterceptor> socketInter, PacketsFormula formula) {
        var currentFormula = formula;
        for (PacketInterceptor interceptor : socketInter) {
            var nextFormula = new PacketsFormula();

            for (Packet item : currentFormula.getUpstreamPackets()) {
                var resultingFormula = interceptor.interceptRawPacket(item);
                nextFormula.add(resultingFormula);
            }
            nextFormula.getWritebackPackets().addAll(currentFormula.getWritebackPackets());
            currentFormula = nextFormula;
        }
        return currentFormula;
    }

}
