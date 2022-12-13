package com.mikolka9144.WoCserver.model.EventCodecs;

public class BlockData {
    private short x;
    private short y;
    private short z;

    public BlockData(short x, short y, short z, short chunkX, short chunkZ, byte blockType, byte blockData, byte prevBlockData, byte prevBlockType) {
        this(x,y,z,chunkX,chunkZ,blockType, blockData);
        this.prevBlockData = prevBlockData;
        this.prevBlockType = prevBlockType;
    }

    public BlockData(short x, short y, short z, short chunkX, short chunkZ, byte blockType, byte blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockType = blockType;
        this.blockData = blockData;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public short getZ() {
        return z;
    }

    public void setZ(short z) {
        this.z = z;
    }

    public short getChunkX() {
        return chunkX;
    }

    public void setChunkX(short chunkX) {
        this.chunkX = chunkX;
    }

    public short getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(short chunkZ) {
        this.chunkZ = chunkZ;
    }

    public byte getBlockType() {
        return blockType;
    }

    public void setBlockType(byte blockType) {
        this.blockType = blockType;
    }

    public byte getBlockData() {
        return blockData;
    }

    public void setBlockData(byte blockData) {
        this.blockData = blockData;
    }

    public byte getPrevBlockData() {
        return prevBlockData;
    }

    public void setPrevBlockData(byte prevBlockData) {
        this.prevBlockData = prevBlockData;
    }

    public byte getPrevBlockType() {
        return prevBlockType;
    }

    public void setPrevBlockType(byte prevBlockType) {
        this.prevBlockType = prevBlockType;
    }

    private short chunkX;
    private short chunkZ;
    private byte blockType;
    private byte blockData;
    private byte prevBlockData;
    private byte prevBlockType;
}
