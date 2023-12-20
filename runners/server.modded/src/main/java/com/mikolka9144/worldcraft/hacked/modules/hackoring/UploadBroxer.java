package com.mikolka9144.worldcraft.hacked.modules.hackoring;

import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldUploadRequest;
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
        pack.getChunks().enumerateWorld2D(60,s ->
                pack.getChunks().at(s).setBlockType(BlockType.IRON_BLOCK_ID));

        log.info("HAXXXXORED!!!!!");
        request.setWorld(pack.toTarGzBin());
    }
}
