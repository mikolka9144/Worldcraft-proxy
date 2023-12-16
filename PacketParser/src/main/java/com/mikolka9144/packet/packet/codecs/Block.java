package com.mikolka9144.packet.packet.codecs;

import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private static final int CHUNK_SIZE = 16;
    private short x;
    private short y;
    private short z;

    private short chunkX;
    private short chunkZ;
    private byte blockType;
    private byte blockData;
    private byte prevBlockType;
    private byte prevBlockData;
    public Vector3Short getPosition(){
        return new Vector3Short(
                (x+(chunkX*CHUNK_SIZE)),
                y,
                (z+(chunkZ*CHUNK_SIZE)));
    }
    public BlockType getBlockName(){
        return BlockType.findBlockById(blockType);
    }
    public void setPosition(Vector3Short position){
        x = (short) (position.getX()%CHUNK_SIZE);
        y = position.getY();
        z = (short) (position.getZ()%CHUNK_SIZE);
        chunkX = (short) (position.getX()/CHUNK_SIZE);
        chunkZ = (short) (position.getZ()/CHUNK_SIZE);
    }

    public Block(short x, short y, short z, short chunkX, short chunkZ, byte blockType, byte blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockType = blockType;
        this.blockData = blockData;
    }
    public Block(Vector3Short position, BlockType blockType, byte blockData) {
        setPosition(position);
        this.blockType = blockType.getId();
        this.blockData = blockData;
    }
    public Block(Vector3Short position, BlockType blockType, byte blockData, byte prevBlockType, byte prevBlockData) {
        setPosition(position);
        this.blockType = blockType.getId();
        this.blockData = blockData;
        this.prevBlockType = prevBlockType;
        this.prevBlockData = prevBlockData;
    }

    public void setBlockName(BlockType value) {
        blockType = value.getId();
    }

}
