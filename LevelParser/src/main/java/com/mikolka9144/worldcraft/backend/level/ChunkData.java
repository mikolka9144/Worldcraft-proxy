package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.utills.LevelConsts;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.level.nbt.RegionNBT;
import dev.dewy.nbt.tags.array.ByteArrayTag;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;

import java.time.temporal.ValueRange;

import static com.mikolka9144.worldcraft.utills.LevelConsts.LEVEL_CHUNK_SIZE;

//Position ranges
//X:0-15
//Y:0-127
//Z:0-15
@Getter
public class ChunkData {
    public static final String BLOCKS = "Blocks";
    public static final String BLOCK_LIGHT = "BlockLight";
    public static final String SKY_LIGHT = "SkyLight";

    private CompoundTag rawNBT;
    public ChunkData(byte[] chunkBlob){
            rawNBT = RegionNBT.open(chunkBlob);
    }
    public ChunkData(int chunkX,int chunkZ){
        CompoundTag chk = new CompoundTag("Level");
        chk.putInt("xPos",chunkX);
        chk.putInt("zPos",chunkZ);
        chk.putByteArray(BLOCKS,new byte[32768]);
        chk.putByteArray(SKY_LIGHT,new byte[16384]);
        chk.putByteArray(BLOCK_LIGHT,new byte[16384]);
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
        position += z* LevelConsts.CHUNK_HEIGHT;
        position += x* LevelConsts.CHUNK_HEIGHT* LEVEL_CHUNK_SIZE;
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
        public void setBlockLight(byte value){
            setLight(getLevel().getByteArray(BLOCK_LIGHT),value);
        }
        public void setSkyLight(byte value){
            setLight(getLevel().getByteArray(SKY_LIGHT),value);
        }
        public byte getBlock(){
            return getLevel().getByteArray(BLOCKS).get(index);
        }
        public byte getData(){
            return getLevel().getByteArray("Data").get(index);
        }
        public byte getBlockLight(){
            return getLight(getLevel().getByteArray(BLOCK_LIGHT));
        }
        public byte getSkyLight(){
            return getLight(getLevel().getByteArray(SKY_LIGHT));
        }
        private byte getLight(ByteArrayTag set){
            int targetByteIndex = index>>1;
            boolean isDivisiable = (index & 1) == 1;
            byte targetByte = set.get(targetByteIndex);
            return (byte) (isDivisiable? targetByte&15: targetByte>>4);
        }
        private void setLight(ByteArrayTag set,byte value){
            if(!ValueRange.of(0,15).isValidValue(value)) throw new IllegalArgumentException("Light must be between 0 and 15");
            int targetByteIndex = index>>1;
            boolean isDivisiable = (index & 1) == 1;
            byte targetByte = set.get(targetByteIndex);
            if (isDivisiable){
                byte leftPart = (byte) (targetByte>>4);
                byte stub = (byte) (leftPart<<4);
                byte result = (byte) (stub+value);
                set.set(targetByteIndex,result);
            }
            else {
                byte rightPart = (byte) (targetByte&15);
                byte stub = (byte) (value<<4);
                byte result = (byte) (stub+rightPart);
                set.set(targetByteIndex,result);
            }
        }
    }
}
