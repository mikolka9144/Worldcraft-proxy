package com.mikolka9144.WoCserver.utills.level;

import com.mikolka9144.WoCserver.utills.level.gzip.GZipConverter;
import com.mikolka9144.WoCserver.utills.level.gzip.GzipEntry;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

//level.dat
//region/r.0.0.mcr
public class World {
    public static World fromTarGzBin(byte[] tar) throws IOException {
        List<GzipEntry> files = GZipConverter.unGtar(tar);
        GzipEntry levelEntry = files.stream().filter(s -> s.getHeader().getName().equals("level.dat")).findFirst().get();
        GzipEntry chunks = files.stream().filter(s -> s.getHeader().getName().equals("region/r.0.0.mcr")).findFirst().get();
        return new World(levelEntry,chunks);
    }
    public final CompoundTag level_dat;
    public byte[] chunks; //TODO
    private final GzipEntry levelEntry;
    private final GzipEntry chunksEntry;

    private World(GzipEntry levelEntry, GzipEntry regionEntry) throws IOException {
        this.level_dat = new Nbt().fromByteArray(GZipConverter.unGzip(levelEntry.getData()));
        this.chunks = regionEntry.getData();
        this.levelEntry = levelEntry;
        this.chunksEntry = regionEntry;
    }
    public byte[] toTarGzBin() throws IOException {
        levelEntry.setData(GZipConverter.Gzipify(saveNbt(level_dat)));
        chunksEntry.setData(chunks);
        return GZipConverter.Gtarify(List.of(levelEntry, chunksEntry));
    }
    private byte[] saveNbt(CompoundTag tag) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutput dataOutput = new DataOutputStream(byteOut);
        new Nbt().toStream(tag,dataOutput);
        return byteOut.toByteArray();
    }
}
