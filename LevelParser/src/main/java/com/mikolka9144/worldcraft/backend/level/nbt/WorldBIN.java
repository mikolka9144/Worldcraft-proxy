package com.mikolka9144.worldcraft.backend.level.nbt;

import com.mikolka9144.worldcraft.backend.level.Level;
import com.mikolka9144.worldcraft.backend.level.Terrain;
import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.backend.level.gzip.GZipConverter;
import com.mikolka9144.worldcraft.backend.level.gzip.GzipEntry;
import com.mikolka9144.worldcraft.utills.exception.CompressionException;
import com.mikolka9144.worldcraft.utills.exception.LevelParseException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.util.List;
import java.util.Optional;

public class WorldBIN {
    public static final String LEVEL_DAT = "level.dat";
    public static final String REGION = "region/r.0.0.mcr";
    private WorldBIN(){}
    public static World open(byte[] tar) throws LevelParseException {
        List<GzipEntry> files = GZipConverter.unGtar(tar);
        Optional<GzipEntry> levelEntry = files.stream().filter(s -> s.getHeader().getName().equals(LEVEL_DAT)).findFirst();
        Optional<GzipEntry> chunks = files.stream().filter(s -> s.getHeader().getName().equals(REGION)).findFirst();
        if (levelEntry.isEmpty() || chunks.isEmpty()) {
            throw new LevelParseException("World zip is missing critical files. Either level.dat or r.0.0.mcr in 'region' folder");
        }
        Level lvl = LevelNBT.open(levelEntry.orElseThrow().getData());
        Terrain trn = ChunksMCR.createFromMCR(chunks.orElseThrow().getData());
        return new World(lvl,trn);
    }

    public static byte[] build(World world) {
        try {
            return GZipConverter.gtarify(List.of(
                    createNode(LEVEL_DAT,world.getLevel().toLevelDat()),
                    createNode(REGION,world.getChunks().toMcrFile())));
        } catch (CompressionException e) {
            String msg = "Fatal error occured while saving World. Something is REALLY wrong:";
            throw new LevelParseException(msg, e);
        }
    }
    private static GzipEntry createNode(String name,byte[] data){
        GzipEntry entry = new GzipEntry(new TarArchiveEntry(name), new byte[0]);
        entry.setData(data);
        return entry;
    }
}
