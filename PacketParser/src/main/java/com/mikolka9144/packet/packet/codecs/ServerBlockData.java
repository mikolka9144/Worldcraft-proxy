package com.mikolka9144.packet.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ServerBlockData {
    private int packetIndex;
    private int allPackets;
    private List<Block> blocks;
}
