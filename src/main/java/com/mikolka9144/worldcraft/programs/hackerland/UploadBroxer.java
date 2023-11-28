package com.mikolka9144.worldcraft.programs.hackerland;

import com.mikolka9144.worldcraft.common.World;
import com.mikolka9144.worldcraft.common.level.Chunks.ChunksMCR;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("haxor")
@Slf4j
public class UploadBroxer implements HttpUploadInterceptor {
    @SneakyThrows
    @Override
    public void uploadWorld(WorldUploadRequest request) {
        World pack = World.fromTarGzBin(request.getWorld());
            for (int x = 0; x < ChunksMCR.MAX_X_CANON-5; x++) {
                for (int z = 0; z < ChunksMCR.MAX_Z_CANON-5; z++) {
                    pack.getChunks().setBlock(x,60,z, BlockData.BlockType.IRON_BLOCK_ID);
                }
            }

        log.info("HAXXXXORED!!!!!");
        request.setWorld(pack.toTarGzBin());
    }
}
