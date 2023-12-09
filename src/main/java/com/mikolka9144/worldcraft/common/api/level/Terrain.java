package com.mikolka9144.worldcraft.common.api.level;

import com.mikolka9144.worldcraft.common.api.level.chunks.ChunkData;
import com.mikolka9144.worldcraft.common.api.packet.enums.BlockType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mikolka9144.worldcraft.common.api.level.chunks.ChunksMCR.CHUNK_SIZE;
import static com.mikolka9144.worldcraft.common.api.level.chunks.ChunksMCR.SECTOR_SIZE;

@Getter
@NoArgsConstructor
public class Terrain {
    private final ChunkData[][] chunks = new ChunkData[32][32];
    private byte[] timestamps = new byte[SECTOR_SIZE];
    public Terrain(byte[] timestamps){
        this.timestamps = timestamps;
    }

    public void setBlockType(int x, int y, int z, BlockType block){
        setBlock(x,y,z,block.getId());
    }
    public void setBlock(int x, int y, int z, byte block){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        chunks[chunkX][chunkZ].setBlock(xPos,y,zPos,block);
    }
    public void setData(int x, int y, int z, byte data){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        chunks[chunkX][chunkZ].setData(xPos,y,zPos,data);
    }
    public BlockType getBlockType(int x, int y, int z){
        return BlockType.findBlockById(getBlock(x,y,z));
    }
    public byte getBlock(int x, int y, int z){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        return chunks[chunkX][chunkZ].getBlock(xPos,y,zPos);
    }
    public byte getData(int x, int y, int z){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        return chunks[chunkX][chunkZ].getData(xPos,y,zPos);
    }
}
