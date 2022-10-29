package com.mikolka9144.Impl;

import com.mikolka9144.Utills.gzip.GZipConverter;
import com.mikolka9144.Utills.gzip.GzipEntry;
import com.mikolka9144.Models.HttpInterceptor;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class WoC287WorldFixer implements HttpInterceptor {
    @Override
    public byte[] getWorld(int worldId, byte[] worldBin) {
        try {
            List<GzipEntry> files = GZipConverter.unGtar(worldBin);
            GzipEntry levelEntry = files.stream().filter(s -> s.getHeader().getName().equals("level.dat")).findFirst().get();
            CompoundTag levelNbt = new Nbt().fromByteArray(levelEntry.getData());
            // Modify
            if(!levelNbt.getCompound("Data").contains("LastPlayed")){
                levelNbt.getCompound("Data").putLong("LastPlayed",0);
            }
            //Saving
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutput dataOutput = new DataOutputStream(byteOut);
            new Nbt().toStream(levelNbt,dataOutput);
            levelEntry.setData(byteOut.toByteArray());
            return GZipConverter.Gtarify(files);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] uploadWorld(byte[] worldBin, String ContentType) {
        return worldBin;
    }


}
