package com.mikolka9144.worldcraft.socket.model.Packet;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
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
