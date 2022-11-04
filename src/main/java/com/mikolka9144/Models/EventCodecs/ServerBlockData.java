package com.mikolka9144.Models.EventCodecs;

import java.util.List;

public class ServerBlockData {
    public ServerBlockData(int packetIndex, int allPackets, List<BlockData> blocks) {
        this.packetIndex = packetIndex;
        this.allPackets = allPackets;
        this.blocks = blocks;
    }

    public int getPacketIndex() {
        return packetIndex;
    }

    public void setPacketIndex(int packetIndex) {
        this.packetIndex = packetIndex;
    }

    public int getAllPackets() {
        return allPackets;
    }

    public void setAllPackets(int allPackets) {
        this.allPackets = allPackets;
    }

    public List<BlockData> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<BlockData> blocks) {
        this.blocks = blocks;
    }

    private int packetIndex;
    private int allPackets;
    private List<BlockData> blocks;
}
