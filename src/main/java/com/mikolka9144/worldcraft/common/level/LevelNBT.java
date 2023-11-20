package com.mikolka9144.worldcraft.common.level;

import com.mikolka9144.worldcraft.common.level.gzip.GZipConverter;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.IntTag;
import dev.dewy.nbt.tags.primitive.LongTag;
import dev.dewy.nbt.tags.primitive.StringTag;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public class LevelNBT {

    private final CompoundTag nbt;

    @SneakyThrows
    public LevelNBT(byte[] nbtBinary){
        var decompressedNBT = GZipConverter.unGzip(nbtBinary);
        nbt = new Nbt().fromByteArray(decompressedNBT);
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
    @SneakyThrows
    public byte[] build()  {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt,dataOutput);
        return GZipConverter.Gzipify(byteOut.toByteArray());
    }
}
