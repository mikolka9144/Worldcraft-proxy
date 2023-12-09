package com.mikolka9144.worldcraft.modules.simba;

import com.mikolka9144.worldcraft.common.api.level.Level;
import com.mikolka9144.worldcraft.common.api.level.Terrain;
import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.common.api.level.chunks.ChunkData;
import com.mikolka9144.worldcraft.common.api.packet.enums.BlockType;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.DoubleTag;
import dev.dewy.nbt.tags.primitive.FloatTag;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component("direct-download")
public class DirectConnectHttpInterceptors implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        switch (request.getWorldId()){
            case 1:{
                byte[] file = Files.readAllBytes(Path.of("./game.tar.gz"));
                request.setWorld(World.fromTarGzBin(file));
                return;
            }
            default:{
                Level lvl = new Level(new CompoundTag(""));

                CompoundTag player = new CompoundTag("Player");
                player.putList("Rotation", List.of(new FloatTag(0),new FloatTag(0)));
                player.putList("Pos", List.of(
                        new DoubleTag(10),
                        new DoubleTag(10),
                        new DoubleTag(10)
                        ));

                CompoundTag data = new CompoundTag("Data");
                data.putInt("SpawnX",10);
                data.putInt("GameType",1);
                data.putInt("MapType",1);
                data.putInt("SpawnX",10);
                data.putInt("SpawnY",10);
                data.putString("BuildVersion","2.5_u");
                data.putLong("LastPlayed",0); // xd
                data.putString("LevelName","textLOL");
                data.putInt("SpawnZ",10);
                data.put(player);
                lvl.getNbt().put(data);


                Terrain terrain = new Terrain();
                for (int x = 0; x < 32; x++) {
                    for (int z = 0; z < 32; z++) {
                        terrain.getChunks()[x][z] = new ChunkData((CompoundTag) null);
                    }
                }
                for (int x = 0; x < 20; x++) {
                    for (int z = 0; z < 20; z++) {
                        CompoundTag chk = new CompoundTag("Level");
                        chk.putInt("xPos",x);
                        chk.putInt("zPos",z);
                        chk.putByteArray("Blocks",new byte[32768]);
                        chk.putByteArray("SkyLight",new byte[16384]);
                        chk.putByteArray("BlockLight",new byte[16384]);
                        chk.putByteArray("Data",new byte[32768]);
                        chk.putByte("LightCalculated", (byte) 0);

                        CompoundTag enclosure = new CompoundTag("");
                        enclosure.put(chk);
                        terrain.getChunks()[x][z] = new ChunkData(enclosure);
                        for (int ix = 0; ix < 16; ix++) {
                            for (int xz = 0; xz < 16; xz++) {
                                terrain.getChunks()[x][z].setBlock(ix,1,xz, BlockType.GLASS_ID.getId());
                            }
                        }
                    }
                }
                request.setWorld(new World(lvl,terrain));
            }
        }
    }
}
