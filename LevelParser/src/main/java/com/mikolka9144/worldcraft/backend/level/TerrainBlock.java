package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.utills.enums.BlockType;
import dev.dewy.nbt.tags.array.ByteArrayTag;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.time.temporal.ValueRange;

public class TerrainBlock {
    private final CompoundTag chunkData;
    private final int index;

    TerrainBlock(ChunkData chunkData, int index) {
        this.chunkData = chunkData.getLevel();
        this.index = index;
    }

    public TerrainBlock setBlock(byte block) {
        setDoubleHexValue(array(ChunkData.BLOCKS),block);
        return this;
    }

    public TerrainBlock setData(byte data) {
        setDoubleHexValue(array(ChunkData.DATA),data);
        return this;
    }

    public BlockType getBlockType() {
        return BlockType.findBlockById(getBlock());
    }

    public TerrainBlock setBlockType(BlockType block) {
        setBlock(block.getId());
        return this;
    }

    public TerrainBlock setBlockLight(int value) {
        return setBlockLight((byte) value);
    }

    public TerrainBlock setSkyLight(int value) {
        return setSkyLight((byte) value);
    }

    public TerrainBlock setBlockLight(byte value) {
        setSingleHexValue(chunkData.getByteArray(ChunkData.BLOCK_LIGHT), value);
        return this;
    }

    public TerrainBlock setSkyLight(byte value) {
        setSingleHexValue(chunkData.getByteArray(ChunkData.SKY_LIGHT), value);
        return this;
    }

    public byte getBlock() {
        return getDoubleHexValue(array(ChunkData.BLOCKS));
    }

    public byte getData() {
        return getDoubleHexValue(array(ChunkData.DATA));
    }

    public byte getBlockLight() {
        return getSingleHexValue(array(ChunkData.BLOCK_LIGHT));
    }

    public byte getSkyLight() {
        return getSingleHexValue(array(ChunkData.SKY_LIGHT));
    }
    public byte getDoubleHexValue(ByteArrayTag set){
        return set.get(index);
    }
    public void setDoubleHexValue(ByteArrayTag set, byte value) {
        set.set(index,value);
    }
    public byte getSingleHexValue(ByteArrayTag set) {
        int targetByteIndex = index >> 1;
        boolean isNotDivisiable = (index & 1) == 1;
        byte targetByte = set.get(targetByteIndex);
        byte b = (byte) (isNotDivisiable ? targetByte >> 4 : targetByte & 15);
        return b < 0 ? (byte) (16 + b) : b; // this fixes minus light levels
    }

    public void setSingleHexValue(ByteArrayTag set, byte value) {
        if (!ValueRange.of(0, 15).isValidValue(value))
            throw new IllegalArgumentException("Light must be between 0 and 15");
        int targetByteIndex = index >> 1;
        boolean isNotDivisiable = (index & 1) == 1;
        byte targetByte = set.get(targetByteIndex);
        if (isNotDivisiable) {
            byte rightPart = (byte) (targetByte & 15);
            byte stub = (byte) (value << 4);
            byte result = (byte) (stub + rightPart);
            set.set(targetByteIndex, result);
        } else {
            byte leftPart = (byte) (targetByte >> 4);
            byte stub = (byte) (leftPart << 4);
            byte result = (byte) (stub + value);
            set.set(targetByteIndex, result);
        }
    }
    private ByteArrayTag array(String name){
        return chunkData.getByteArray(name);
    }
}
