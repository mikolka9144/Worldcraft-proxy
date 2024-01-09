package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.backend.level.nbt.WorldBIN;
import com.mikolka9144.worldcraft.utills.exception.LevelParseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//level.dat
//region/r.0.0.mcr
@Slf4j
public class World {

    @Getter
    private final Level level;
    @Getter
    private final Terrain chunks;
    public World(Level level, Terrain terrain) {
        this.level = level;
        this.chunks = terrain;
    }

    public static World fromTarGzBin(byte[] tar) throws LevelParseException {
        return WorldBIN.open(tar);
    }

    public byte[] toTarGzBin() {
        return WorldBIN.build(this);
    }
}
