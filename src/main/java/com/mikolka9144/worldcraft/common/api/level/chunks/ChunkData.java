package com.mikolka9144.worldcraft.common.api.level.chunks;

import com.mikolka9144.worldcraft.common.api.level.nbt.RegionNBT;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;

//Position ranges
//X:0-15
//Y:0-127
//Z:0-15
@Getter
public class ChunkData {
    private CompoundTag rawNBT;
    private static final int CHUNK_HEIGHT = 128;
    private static final int CHUNK_LENGHT = 16;
    public ChunkData(byte[] chunkBlob){
        try {
            rawNBT = RegionNBT.open(chunkBlob);
        }
        catch (Exception ignored){
        }
    }
    public ChunkData(CompoundTag tag){
        rawNBT = tag;
    }
    public void setBlock(int x, int y, int z, byte block){
        int index = calculatePosition(x,y,z);
        getLevel().getByteArray("Blocks").set(index,block);
    }
    public void setData(int x, int y, int z, byte data){
        int index = calculatePosition(x,y,z);
        getLevel().getByteArray("Data").set(index,data);
    }
    public byte getBlock(int x, int y, int z){
        int index = calculatePosition(x,y,z);
        return getLevel().getByteArray("Blocks").get(index);
    }
    public byte getData(int x, int y, int z){
        int index = calculatePosition(x,y,z);
        return getLevel().getByteArray("Data").get(index);
    }
    public CompoundTag getLevel(){
        return rawNBT.getCompound("Level");
    }
    private int calculatePosition(int x,int y,int z){
        int position = 0;
        position += y;
        position += z*CHUNK_HEIGHT;
        position += x*CHUNK_HEIGHT*CHUNK_LENGHT;
        return position;
    }
    public byte[] build(){
        return rawNBT != null ? RegionNBT.build(rawNBT) : new byte[0];
    }
}
