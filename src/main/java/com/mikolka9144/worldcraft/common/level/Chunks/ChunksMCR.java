package com.mikolka9144.worldcraft.common.level.Chunks;

import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

// https://minecraft.fandom.com/wiki/Region_file_format
// X: 0-111
// Z: 0-111
// Y: 0-127
public class ChunksMCR {
    private static final int CHUNK_SIZE = 16;
    public static final int SECTOR_SIZE = 4096;
    private final ChunkData[][] chunks = new ChunkData[32][32];
    private byte[] timeStamps;

    public ChunksMCR(byte[] data){
        createFromMCR(data);
    }
    public void setBlock(int x, int y, int z, BlockData.BlockType block){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        chunks[chunkX][chunkZ].setBlock(xPos,y,zPos,block);
    }
    public void setData(int x, int y, int z, byte data){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        chunks[chunkX][chunkZ].setData(xPos,y,zPos,data);
    }
    public BlockData.BlockType getBlock(int x, int y, int z){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        return chunks[chunkX][chunkZ].getBlock(xPos,y,zPos);
    }
    public byte getData(int x, int y, int z){
        int xPos = x%CHUNK_SIZE;
        int zPos = z%CHUNK_SIZE;
        int chunkX = x/CHUNK_SIZE;
        int chunkZ = z/CHUNK_SIZE;
        return chunks[chunkX][chunkZ].getData(xPos,y,zPos);
    }
    public byte[] build(){
        PacketDataBuilder builder = new PacketDataBuilder();
        PacketDataBuilder chunksSectionBuilder = new PacketDataBuilder();
        short sectorsWritten = 2;
        for (int z = 0; z < 32; z++) {
            for (int x = 0; x < 32; x++) {
                byte[] sector = chunks[x][z].build(); //TODO
                int sectorCount = (int) Math.ceil(  (double) sector.length / ChunksMCR.SECTOR_SIZE);

                builder.append((byte) 0);
                builder.append(sectorCount!=0 ? sectorsWritten : 0);
                builder.append((byte) sectorCount);

                chunksSectionBuilder.append(sector);
                sectorsWritten+=sectorCount;
            }
        }

        builder.append(timeStamps);
        builder.append(chunksSectionBuilder.build());
        return builder.build();
    }
    private void createFromMCR(byte[] data) {
        // split binary to 4KiB sectors
        List<byte[]> sections = splitToSectors(data);

        byte[] headerPositions = sections.get(0);
        timeStamps = sections.get(1);

        var reader = new PacketDataReader(headerPositions);
        int i = 0;
        while (reader.hasNext(4)){
            reader.getByte();
            int sectorLocation = reader.getShort();
            byte sectorCount = reader.getByte();

            byte[] chunkBlob = readSectors(sections,sectorLocation,sectorCount);

            chunks[i%32][i/32] = new ChunkData(chunkBlob);
            i++;
        }
    }
    private byte[] readSectors(List<byte[]> sections, int sectorLocation, byte sectorCount) {
        PacketDataBuilder builder = new PacketDataBuilder();
        for (int i = 0; i < sectorCount; i++) {
            builder.append(sections.get(sectorLocation+i));
        }
        return builder.build();
    }

    @SneakyThrows
    private List<byte[]> splitToSectors(byte[] data){
        List<byte[]> result = new ArrayList<>();
        var stream = new ByteArrayInputStream(data);
        while (stream.available()>0){
            result.add(stream.readNBytes(SECTOR_SIZE));
        }
        return result;
    }
}
