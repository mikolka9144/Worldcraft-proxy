package com.mikolka9144.worldcraft.level.nbt;

import com.mikolka9144.worldcraft.level.gzip.GZipConverter;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public class LevelNBT {
    private LevelNBT(){}

    @SneakyThrows
    public static CompoundTag open(byte[] nbtBinary){
        var decompressedNBT = GZipConverter.unGzip(nbtBinary);
        return new Nbt().fromByteArray(decompressedNBT);
    }

    @SneakyThrows
    public static byte[] build(CompoundTag nbt)  {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt,dataOutput);
        return GZipConverter.gzipify(byteOut.toByteArray());
    }
}
