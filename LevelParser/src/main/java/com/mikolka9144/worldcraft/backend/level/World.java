package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.backend.level.gzip.GZipConverter;
import com.mikolka9144.worldcraft.backend.level.gzip.GzipEntry;
import com.mikolka9144.worldcraft.backend.level.nbt.ChunksMCR;
import com.mikolka9144.worldcraft.backend.level.nbt.LevelNBT;
import com.mikolka9144.worldcraft.utills.exception.CompressionException;
import com.mikolka9144.worldcraft.utills.exception.LevelParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.util.List;
import java.util.Optional;

//level.dat
//region/r.0.0.mcr
@Slf4j
public class World {

    public static final String LEVEL_DAT = "level.dat";
    public static final String REGION = "region/r.0.0.mcr";
    @Getter
    private final Level level;
    @Getter
    private final Terrain chunks;
    private final GzipEntry levelEntry;
    private final GzipEntry chunksEntry;

    private World(GzipEntry levelEntry, GzipEntry regionEntry) {
        this.levelEntry = levelEntry;
        this.chunksEntry = regionEntry;
        level = new Level(LevelNBT.open(levelEntry.getData()));
        chunks = ChunksMCR.createFromMCR(chunksEntry.getData());
    }

    public World(Level level, Terrain terrain) {
        this.level = level;
        this.chunks = terrain;
        levelEntry = new GzipEntry(new TarArchiveEntry(LEVEL_DAT), new byte[0]);
        chunksEntry = new GzipEntry(new TarArchiveEntry(REGION), new byte[0]);
    }

    public static World fromTarGzBin(byte[] tar) throws LevelParseException {
        List<GzipEntry> files = GZipConverter.unGtar(tar);
        Optional<GzipEntry> levelEntry = files.stream().filter(s -> s.getHeader().getName().equals(LEVEL_DAT)).findFirst();
        Optional<GzipEntry> chunks = files.stream().filter(s -> s.getHeader().getName().equals(REGION)).findFirst();
        if (levelEntry.isEmpty() || chunks.isEmpty()) {
            var message = "";
            log.error(message);
            throw new LevelParseException("World zip is missing critical files. Either level.dat or r.0.0.mcr in 'region' folder");
        }
        return new World(levelEntry.get(), chunks.get());
    }

    public byte[] toTarGzBin() {
        levelEntry.setData(LevelNBT.build(level.getNbt()));
        chunksEntry.setData(ChunksMCR.build(chunks));
        try {
            return GZipConverter.gtarify(List.of(levelEntry, chunksEntry));
        } catch (CompressionException e) {
            String msg = "Fatal error occured while saving World. Something is REALLY wrong:";
            log.error(msg);
            throw new LevelParseException(msg, e);
        }
    }
}
