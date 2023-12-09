package com.mikolka9144.worldcraft.common.api.level;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.IntTag;
import dev.dewy.nbt.tags.primitive.LongTag;
import dev.dewy.nbt.tags.primitive.StringTag;
import lombok.Getter;

@Getter
public class Level {
    private final CompoundTag nbt;

    public Level(CompoundTag nbt){

        this.nbt = nbt;
    }
    public CompoundTag getData(){
        return nbt.getCompound("Data");
    }
    public LongTag time(){
        return getData().getLong("Time");
    }
    public IntTag spawnX(){
        return getData().getInt("SpawnX");
    }
    public IntTag spawnY(){
        return getData().getInt("SpawnY");
    }
    public IntTag spawnZ(){
        return getData().getInt("SpawnZ");
    }
    public IntTag mapType(){
        return getData().getInt("MapType");
    }
    public LongTag seed(){
        return getData().getLong("RandomSeed");
    }
    public IntTag sizeOnDisk(){
        return getData().getInt("SizeOnDisk");
    }
    public IntTag gameType(){
        return getData().getInt("GameType");
    }
    public StringTag buildVersion(){
        return getData().getString("BuildVersion");
    }
    public StringTag serverVersion(){
        return getData().getString("ServerVersion");
    }
}
