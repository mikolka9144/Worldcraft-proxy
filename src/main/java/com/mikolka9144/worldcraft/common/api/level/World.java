package com.mikolka9144.worldcraft.common.api.level;

import com.mikolka9144.worldcraft.common.api.level.chunks.ChunksMCR;
import com.mikolka9144.worldcraft.common.api.level.nbt.LevelNBT;
import com.mikolka9144.worldcraft.common.api.level.gzip.GZipConverter;
import com.mikolka9144.worldcraft.common.api.level.gzip.GzipEntry;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.compressors.CompressorException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//level.dat
//region/r.0.0.mcr
@Slf4j
public class World {

    public static World fromTarGzBin(byte[] tar) throws IOException {
        List<GzipEntry> files = GZipConverter.unGtar(tar);
        Optional<GzipEntry> levelEntry = files.stream().filter(s -> s.getHeader().getName().equals("level.dat")).findFirst();
        Optional<GzipEntry> chunks = files.stream().filter(s -> s.getHeader().getName().equals("region/r.0.0.mcr")).findFirst();
        if(levelEntry.isEmpty() || chunks.isEmpty()){
            var message = "World zip is missing critical files. Either level.dat or r.0.0.mcr in 'region' folder";
            log.error(message);
            throw new IOException(message);
        }
        return new World(levelEntry.get(),chunks.get());
    }
    private World(GzipEntry levelEntry, GzipEntry regionEntry)  {
        this.levelEntry = levelEntry;
        this.chunksEntry = regionEntry;
        level = new LevelNBT(levelEntry.getData());
        chunks = new ChunksMCR(chunksEntry.getData());
    }

    @Getter
    private final LevelNBT level;
    @Getter
    private final ChunksMCR chunks;
    private final GzipEntry levelEntry;

    private final GzipEntry chunksEntry;
    @SneakyThrows
    public byte[] toTarGzBin()  {
        levelEntry.setData(level.build());
        chunksEntry.setData(chunks.build());
        try {
            return GZipConverter.gtarify(List.of(levelEntry, chunksEntry));
        } catch (IOException e) {
            String msg = "Fatal error occured while saving World. Something is REALLY wrong:";
            log.error(msg);
            throw new CompressorException(msg,e);
        }
    }
}
