package com.mikolka9144.worldcraft.common.level;

import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.common.level.Chunks.ChunksMCR;
import com.mikolka9144.worldcraft.common.level.gzip.GZipConverter;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Arrays;

public class RegionNBT {
    @Getter
    private final CompoundTag nbt;

    @SneakyThrows
    public RegionNBT(byte[] chunkBlob)  {
        PacketDataReader reader = new PacketDataReader(chunkBlob);
        int length = reader.getInt();
        reader.getByte(); // gets compression, which is always 2
        byte[] compressedNbt = new ByteArrayInputStream(reader.getBytes()).readNBytes(length-1);

        var decompressedNBT = GZipConverter.unZlib(compressedNbt);
        nbt = new Nbt().fromByteArray(decompressedNBT);
    }
    @SneakyThrows
    public byte[] build()  {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt,dataOutput);
        var compressedNbt =  GZipConverter.Zlibify(byteOut.toByteArray());

        PacketDataBuilder builder = new PacketDataBuilder();
        builder.append(compressedNbt.length+1);
        builder.append((byte) 2);
        builder.append(compressedNbt);

        byte[] rawChunk = builder.build();
        int sectorCount = (int) Math.ceil(  (double) rawChunk.length / ChunksMCR.SECTOR_SIZE);
        return Arrays.copyOf(rawChunk,sectorCount*ChunksMCR.SECTOR_SIZE);
    }
}