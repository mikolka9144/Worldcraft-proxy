package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.utills.enums.BlockType;
import dev.dewy.nbt.tags.array.ByteArrayTag;

import java.time.temporal.ValueRange;

public class TerrainBlock {
    private final ChunkData chunkData;
    private final int index;

    TerrainBlock(ChunkData chunkData, int index) {
        this.chunkData = chunkData;
        this.index = index;
    }

    public TerrainBlock setBlock(byte block) {
        chunkData.getLevel().getByteArray(ChunkData.BLOCKS).set(index, block);
        return this;
    }

    public TerrainBlock setData(byte data) {
        chunkData.getLevel().getByteArray(ChunkData.DATA).set(index, data);
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
        setLight(chunkData.getLevel().getByteArray(ChunkData.BLOCK_LIGHT), value);
        return this;
    }

    public TerrainBlock setSkyLight(byte value) {
        setLight(chunkData.getLevel().getByteArray(ChunkData.SKY_LIGHT), value);
        return this;
    }

    public byte getBlock() {
        return chunkData.getLevel().getByteArray(ChunkData.BLOCKS).get(index);
    }

    public byte getData() {
        return chunkData.getLevel().getByteArray(ChunkData.DATA).get(index);
    }

    public byte getBlockLight() {
        return getLight(chunkData.getLevel().getByteArray(ChunkData.BLOCK_LIGHT));
    }

    public byte getSkyLight() {
        return getLight(chunkData.getLevel().getByteArray(ChunkData.SKY_LIGHT));
    }

    private byte getLight(ByteArrayTag set) {
        int targetByteIndex = index >> 1;
        boolean isNotDivisiable = (index & 1) == 1;
        byte targetByte = set.get(targetByteIndex);
        byte b = (byte) (isNotDivisiable ? targetByte >> 4 : targetByte & 15);
        return b < 0 ? (byte) (16 + b) : b; // this fixes minus light levels
    }

    private void setLight(ByteArrayTag set, byte value) {
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
}
