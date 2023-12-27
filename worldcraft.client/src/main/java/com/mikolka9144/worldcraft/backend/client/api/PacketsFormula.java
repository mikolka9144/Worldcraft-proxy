package com.mikolka9144.worldcraft.backend.client.api;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *  {@link PacketsFormula} is an object for storing information about returned packets.</h3>
 *  It contains list of packets to be sent
 *  with the base packet ({@code upstreamPackets})
 *  and back to the sender ({@code writebackPackets}).
 *  <h3>Note</h3>
 *  By default, first packet in upstreamPackets is packet, that triggers a call
 */
@NoArgsConstructor
public class PacketsFormula {
    private Packet initialpacket;

    public PacketsFormula(Packet initialPacket){
        this.initialpacket = initialPacket;
        addUpstream(initialPacket);
    }
    @Getter
    private final List<Packet> upstreamPackets = new ArrayList<>();
    @Getter
    private final List<Packet> writebackPackets = new ArrayList<>();

    public void add(PacketsFormula s) {
        upstreamPackets.addAll(s.upstreamPackets);
        writebackPackets.addAll(s.writebackPackets);
    }
    public void addUpstream(Packet packet){
        upstreamPackets.add(packet);
    }
    public void addUpstream(List<Packet> packets){
        upstreamPackets.addAll(packets);
    }
    public void addWriteback(Packet packet){
        writebackPackets.add(packet);
    }
    public void addWriteback(List<Packet> packets){
        writebackPackets.addAll(packets);
    }
    public void clearInitialPacket(){
        upstreamPackets.remove(initialpacket);
    }
}

