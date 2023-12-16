package com.mikolka9144.worldcraft.level.nbt;

import com.mikolka9144.worldcraft.level.Terrain;
import com.mikolka9144.worldcraft.level.ChunkData;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

// https://minecraft.fandom.com/wiki/Region_file_format
// X: 0-111
// Z: 0-111
// Y: 0-127

public class ChunksMCR {
    public static final int CHUNK_SIZE = 16;
    public static final int SECTOR_SIZE = 4096;
    private ChunksMCR(){}
    public static byte[] build(Terrain terrain){
        PacketDataBuilder builder = new PacketDataBuilder();
        PacketDataBuilder chunksSectionBuilder = new PacketDataBuilder();
        short sectorsWritten = 2;

        for (int z = 0; z < 32; z++) {
            for (int x = 0; x < 32; x++) {
                ChunkData chunk = terrain.getChunks()[x][z];
                byte[] sector = chunk != null ? chunk.build(): new byte[0];
                int sectorCount = (int) Math.ceil(  (double) sector.length / ChunksMCR.SECTOR_SIZE);

                builder.append((byte) 0);
                builder.append(sectorCount!=0 ? sectorsWritten : 0);
                builder.append((byte) sectorCount);

                chunksSectionBuilder.append(sector);
                sectorsWritten+= (short) sectorCount;
            }
        }

        builder.append(terrain.getTimestamps());
        builder.append(chunksSectionBuilder.build());
        return builder.build();
    }
    public static Terrain createFromMCR(byte[] data) {
        // split binary to 4KiB sectors
        List<byte[]> sections = splitToSectors(data);

        byte[] headerPositions = sections.get(0);
        byte[] timeStamps = sections.get(1);

        Terrain newterrain = new Terrain(timeStamps);

        var reader = new PacketDataReader(headerPositions);
        int i = 0;
        while (reader.hasNext(4)){
            reader.getByte();
            int sectorLocation = reader.getShort();
            byte sectorCount = reader.getByte();

            byte[] chunkBlob = readSectors(sections,sectorLocation,sectorCount);
            ChunkData chunk;
            try{
                chunk = new ChunkData(chunkBlob);
            }
            catch (Exception ignore){
                chunk = null;
            }
            newterrain.getChunks()[i%32][i/32] = chunk;
            i++;
        }
        return newterrain;
    }
    private static byte[] readSectors(List<byte[]> sections, int sectorLocation, byte sectorCount) {
        PacketDataBuilder builder = new PacketDataBuilder();
        for (int i = 0; i < sectorCount; i++) {
            builder.append(sections.get(sectorLocation+i));
        }
        return builder.build();
    }

    @SneakyThrows
    private static List<byte[]> splitToSectors(byte[] data){
        List<byte[]> result = new ArrayList<>();
        var stream = new ByteArrayInputStream(data);
        while (stream.available()>0){
            result.add(stream.readNBytes(SECTOR_SIZE));
        }
        return result;
    }
}
