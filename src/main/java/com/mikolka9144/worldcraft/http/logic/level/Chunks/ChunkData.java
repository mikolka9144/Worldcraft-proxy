package com.mikolka9144.worldcraft.http.logic.level.Chunks;

import com.mikolka9144.worldcraft.http.logic.level.RegionNBT;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;


@AllArgsConstructor
public class ChunkData {
    private byte[] chunkBlob;
    public void execute(Consumer<RegionNBT> consumer){
        RegionNBT nbt = new RegionNBT(chunkBlob);
        consumer.accept(nbt);
        chunkBlob = nbt.build();
    }
    public byte[] build(){
        return chunkBlob;
    }
}
