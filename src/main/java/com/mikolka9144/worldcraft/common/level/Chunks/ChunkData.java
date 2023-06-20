package com.mikolka9144.worldcraft.common.level.Chunks;

import com.mikolka9144.worldcraft.common.level.RegionNBT;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import lombok.Getter;

//Position ranges
//X:0-15
//Y:0-127
//Z:0-15
public class ChunkData {
    @Getter
    private RegionNBT rawNBT;
    private static final int CHUNK_HEIGHT = 128;
    private static final int CHUNK_LENGHT = 16;
    public ChunkData(byte[] chunkBlob){
        try {
            rawNBT = new RegionNBT(chunkBlob);
        }
        catch (Exception ignored){
        }
    }
    public void setBlock(int x, int y, int z, BlockData.BlockType block){
        int index = calculatePosition(x,y,z);
        rawNBT.getNbt().getCompound("Level").getByteArray("Blocks").set(index,block.getId());
    }
    public void setData(int x, int y, int z, byte data){
        int index = calculatePosition(x,y,z);
        rawNBT.getNbt().getCompound("Level").getByteArray("Data").set(index,data);
    }
    public BlockData.BlockType getBlock(int x, int y, int z){
        int index = calculatePosition(x,y,z);
        return BlockData.BlockType.findBlockById(rawNBT.getNbt().getCompound("Level").getByteArray("Blocks").get(index));
    }
    public byte getData(int x, int y, int z){
        int index = calculatePosition(x,y,z);
        return rawNBT.getNbt().getCompound("Level").getByteArray("Blocks").get(index);
    }
    private int calculatePosition(int x,int y,int z){
        int position = 0;
        position += y;
        position += z*CHUNK_HEIGHT;
        position += x*CHUNK_HEIGHT*CHUNK_LENGHT;
        return position;
    }
    public byte[] build(){
        return rawNBT != null ? rawNBT.build() : new byte[0];
    }
}
