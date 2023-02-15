package com.mikolka9144.worldcraft.http.logic.level;

import com.mikolka9144.worldcraft.http.logic.level.Chunks.ChunksMCR;
import com.mikolka9144.worldcraft.http.logic.level.gzip.GZipConverter;
import com.mikolka9144.worldcraft.http.logic.level.gzip.GzipEntry;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

//level.dat
//region/r.0.0.mcr
public class World {
    public static World fromTarGzBin(byte[] tar) throws IOException {
        List<GzipEntry> files = GZipConverter.unGtar(tar);
        GzipEntry levelEntry = files.stream().filter(s -> s.getHeader().getName().equals("level.dat")).findFirst().get();
        GzipEntry chunks = files.stream().filter(s -> s.getHeader().getName().equals("region/r.0.0.mcr")).findFirst().get();
        return new World(levelEntry,chunks);
    }

    public void getLevel(Consumer<LevelNBT> action) {
        LevelNBT nbt = new LevelNBT(levelEntry.getData());
        action.accept(nbt);
        levelEntry.setData(nbt.build());
    }

    public void getRegion(Consumer<ChunksMCR> action) {
        ChunksMCR mcr = new ChunksMCR(chunksEntry.getData());
        action.accept(mcr);
        chunksEntry.setData(mcr.build());
    }

    private final GzipEntry levelEntry;
    private final GzipEntry chunksEntry;

    private World(GzipEntry levelEntry, GzipEntry regionEntry)  {
        this.levelEntry = levelEntry;
        this.chunksEntry = regionEntry;
    }
    public byte[] toTarGzBin() throws IOException {
        return GZipConverter.Gtarify(List.of(levelEntry, chunksEntry));
    }
}
