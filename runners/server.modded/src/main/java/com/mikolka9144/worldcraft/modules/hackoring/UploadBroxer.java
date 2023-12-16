package com.mikolka9144.worldcraft.modules.hackoring;

import com.mikolka9144.worldcraft.level.World;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.model.WorldUploadRequest;
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
