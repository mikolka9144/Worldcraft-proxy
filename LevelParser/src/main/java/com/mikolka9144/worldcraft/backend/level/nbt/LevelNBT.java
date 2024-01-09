package com.mikolka9144.worldcraft.backend.level.nbt;

import com.mikolka9144.worldcraft.backend.level.Level;
import com.mikolka9144.worldcraft.backend.level.gzip.GZipConverter;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public class LevelNBT {
    private LevelNBT() {
    }

    @SneakyThrows
    public static Level open(byte[] nbtBinary) {
        var decompressedNBT = GZipConverter.unGzip(nbtBinary);
        CompoundTag tag = new Nbt().fromByteArray(decompressedNBT);
        return new Level(tag);
    }

    @SneakyThrows
    public static byte[] build(Level nbt) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt.getNbt(), dataOutput);
        return GZipConverter.gzipify(byteOut.toByteArray());
    }
}
