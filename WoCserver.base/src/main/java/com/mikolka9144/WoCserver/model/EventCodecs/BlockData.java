package com.mikolka9144.WoCserver.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BlockData {
    private short x;
    private short y;
    private short z;

    private short chunkX;
    private short chunkZ;
    private byte blockType;
    private byte blockData;
    private byte prevBlockData;
    private byte prevBlockType;


    public BlockData(short x, short y, short z, short chunkX, short chunkZ, byte blockType, byte blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockType = blockType;
        this.blockData = blockData;
    }
}
