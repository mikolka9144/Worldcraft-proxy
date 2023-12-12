package com.mikolka9144.level;

import com.mikolka9144.level.nbt.RegionNBT;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;
import com.mikolka9144.packet.packet.enums.*;
//Position ranges
//X:0-15
//Y:0-127
//Z:0-15
@Getter
public class ChunkData {
    public static final String BLOCKS = "Blocks";
    private static final int CHUNK_HEIGHT = 128;
    private static final int CHUNK_LENGTH = 16;

    private CompoundTag rawNBT;
    public ChunkData(byte[] chunkBlob){
            rawNBT = RegionNBT.open(chunkBlob);
    }
    public ChunkData(int chunkX,int chunkZ){
        CompoundTag chk = new CompoundTag("Level");
        chk.putInt("xPos",chunkX);
        chk.putInt("zPos",chunkZ);
        chk.putByteArray(BLOCKS,new byte[32768]);
        chk.putByteArray("SkyLight",new byte[16384]);
        chk.putByteArray("BlockLight",new byte[16384]);
        chk.putByteArray("Data",new byte[32768]);
        chk.putByte("LightCalculated", (byte) 0);

        rawNBT = new CompoundTag("");
        rawNBT.put(chk);
    }
    public StepBlock at(int x,int y,int z){
        return new StepBlock(calculatePosition(x,y,z));
    }

    public CompoundTag getLevel(){
        return rawNBT.getCompound("Level");
    }
    private int calculatePosition(int x,int y,int z){
        int position = 0;
        position += y;
        position += z*CHUNK_HEIGHT;
        position += x*CHUNK_HEIGHT* CHUNK_LENGTH;
        return position;
    }
    public byte[] build(){
        return RegionNBT.build(rawNBT);
    }
    public class StepBlock{
        private final int index;

        private StepBlock(int index){
            this.index = index;
        }
        public void setBlock(byte block){
            getLevel().getByteArray(BLOCKS).set(index,block);
        }
        public void setData(byte data){
            getLevel().getByteArray("Data").set(index,data);
        }
        public BlockType getBlockType(){
            return BlockType.findBlockById(getBlock());
        }
        public void setBlockType(BlockType block){
            setBlock(block.getId());
        }
        public byte getBlock(){
            return getLevel().getByteArray(BLOCKS).get(index);
        }
        public byte getData(){
            return getLevel().getByteArray("Data").get(index);
        }
    }
}
