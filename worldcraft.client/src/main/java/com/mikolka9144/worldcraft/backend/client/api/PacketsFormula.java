package com.mikolka9144.worldcraft.backend.client.api;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import lombok.Getter;

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
@Getter
public class PacketsFormula {
    private final List<Packet> upstreamPackets = new ArrayList<>();
    private final List<Packet> writebackPackets = new ArrayList<>();

    public void add(PacketsFormula s) {
        upstreamPackets.addAll(s.getUpstreamPackets());
        writebackPackets.addAll(s.getWritebackPackets());
    }
    public void addUpstream(Packet packet){
        upstreamPackets.add(packet);
    }
    public void addWriteback(Packet packet){
        writebackPackets.add(packet);
    }
}

