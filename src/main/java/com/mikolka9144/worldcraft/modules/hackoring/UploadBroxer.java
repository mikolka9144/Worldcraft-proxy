package com.mikolka9144.worldcraft.modules.hackoring;

import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.common.api.level.chunks.ChunksMCR;
import com.mikolka9144.worldcraft.common.api.packet.enums.BlockType;
import com.mikolka9144.worldcraft.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
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
            for (int x = 0; x < ChunksMCR.MAX_COORD_CANON-5; x++) {
                for (int z = 0; z < ChunksMCR.MAX_COORD_CANON-5; z++) {
                    pack.getChunks().setBlockType(x,60,z, BlockType.IRON_BLOCK_ID);
                }
            }

        log.info("HAXXXXORED!!!!!");
        request.setWorld(pack.toTarGzBin());
    }
}
