package com.mikolka9144.worldcraft.backend.level.nbt;

import com.mikolka9144.worldcraft.backend.level.gzip.GZipConverter;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.Arrays;

@Getter
public class RegionNBT {
    private RegionNBT(){}

    @SneakyThrows
    public static CompoundTag open(byte[] chunkBlob)  {
        PacketDataReader reader = new PacketDataReader(chunkBlob);
        int length = reader.getInt();
        reader.getByte(); // gets compression, which is always 2
        byte[] compressedNbt = new ByteArrayInputStream(reader.getBytes()).readNBytes(length-1);

        var decompressedNBT = GZipConverter.unZlib(compressedNbt);
        return new Nbt().fromByteArray(decompressedNBT);
    }
    @SneakyThrows
    public static byte[] build(CompoundTag nbt)  {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(nbt,dataOutput);
        var compressedNbt =  GZipConverter.zlibify(byteOut.toByteArray());

        PacketDataBuilder builder = new PacketDataBuilder();
        builder.append(compressedNbt.length+1);
        builder.append((byte) 2);
        builder.append(compressedNbt);

        byte[] rawChunk = builder.build();
        int sectorCount = (int) Math.ceil(  (double) rawChunk.length / ChunksMCR.SECTOR_SIZE);
        return Arrays.copyOf(rawChunk,sectorCount*ChunksMCR.SECTOR_SIZE);
    }
}
