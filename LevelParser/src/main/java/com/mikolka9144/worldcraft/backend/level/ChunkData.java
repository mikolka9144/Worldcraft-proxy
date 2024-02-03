package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.backend.level.nbt.RegionNBT;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.IntTag;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

import static com.mikolka9144.worldcraft.backend.level.Terrain.*;

//Position ranges
//X:0-15
//Y:0-127
//Z:0-15
@Getter
public class ChunkData {
    public static final String BLOCKS = "Blocks";
    public static final String BLOCK_LIGHT = "BlockLight";
    public static final String SKY_LIGHT = "SkyLight";
    public static final String LIGHT_CALCULATED = "LightCalculated";
    public static final String DATA = "Data";

    private final CompoundTag rawNBT;
    @Getter
    @Setter
    private int timestamp = 0;

    public ChunkData(CompoundTag chunkBlob,int timestamp) {
        rawNBT = chunkBlob;
        this.timestamp = timestamp;
    }

    public ChunkData(int chunkX, int chunkZ) {
        CompoundTag chk = new CompoundTag("Level");
        chk.putByte(LIGHT_CALCULATED, (byte) 1);
        chk.putInt("xPos", chunkX);
        chk.putInt("zPos", chunkZ);
        chk.putByteArray("Biomes",new byte[256]);
        chk.putByteArray(BLOCK_LIGHT, new byte[16384]);
        chk.putByteArray(BLOCKS, new byte[32768]);
        chk.putByteArray(DATA, new byte[32768]);
        chk.putByteArray(SKY_LIGHT, new byte[16384]);

        rawNBT = new CompoundTag("");
        rawNBT.put(chk);
    }
    public IntTag xPos(){
        return getLevel().getInt("xPos");
    }
    public IntTag zPos(){
        return getLevel().getInt("zPos");
    }
    public TerrainBlock at(int x, int y, int z) {
        return new TerrainBlock(this, calculatePosition(x, y, z));
    }

    public boolean getLightCalculated() {
        return getLevel().getByte(LIGHT_CALCULATED).getValue() == 1;
    }

    public void setLightCalculated(boolean value) {
        getLevel().getByte(LIGHT_CALCULATED).setValue((byte) (value ? 1 : 0));
    }
    /**
     * Iterates through every block in chunk.
     *
     * @param step action to run
     */
    public void enumerateChunk3D(Consumer<Vector3Short> step) {
        enumerate3D(16, MAX_Y, 16, step);
    }

    /**
     * Iterates through every block at the given Y.
     *
     * @param constantY Y position of blocks to iterate
     * @param step      action to run
     */
    public void enumerateChunk2D(int constantY, Consumer<Vector3Short> step) {
        enumerate2D(16, constantY, 16, step);
    }

    public CompoundTag getLevel() {
        return rawNBT.getCompound("Level");
    }

    private int calculatePosition(int x, int y, int z) {
        int position = 0;
        position += y;
        position += z << 7; // LevelConsts.CHUNK_HEIGHT
        position += x << 11;// LevelConsts.CHUNK_HEIGHT* LEVEL_CHUNK_SIZE
        return position;
    }

    public byte[] build() {
        return RegionNBT.build(rawNBT);
    }

}
