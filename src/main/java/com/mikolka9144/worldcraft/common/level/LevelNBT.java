package com.mikolka9144.worldcraft.common.level;

import com.mikolka9144.worldcraft.common.level.gzip.GZipConverter;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public class LevelNBT {
    public CompoundTag getNbt() {
        return nbt;
    }

    private final CompoundTag nbt;

    @SneakyThrows
    public LevelNBT(byte[] nbtBinary){
        var decompressedNBT = GZipConverter.unGzip(nbtBinary);
        nbt = new Nbt().fromByteArray(decompressedNBT);
    }
    @SneakyThrows
    public byte[] build()  {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt,dataOutput);
        return GZipConverter.Gzipify(byteOut.toByteArray());
    }
}
