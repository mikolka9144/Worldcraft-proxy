package com.mikolka9144.worldcraft.interceptors.unify;


import com.mikolka9144.worldcraft.backend.level.Terrain;
import com.mikolka9144.worldcraft.backend.level.World;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;
import com.mikolka9144.worldcraft.interceptors.unify.backend.PacketConverter;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component("worldSimplifyLate")
@Slf4j
public class LateWorldConvert implements HttpDownloadInterceptor {

    @Override
    public void getWorld(WorldDownloadRequest world) {
        if (world.getFlags().contains("convert")) {
            log.info("LOGGING WORLD!!!");
            Terrain chk = world.getWorld().getChunks();
            chk.enumerateWorld3D(
                    x -> convertBlock(world.getWorld(), x, PacketConverter::replaceForWorldcraft));
            log.info("DONE LOGGING WORLD!!!");
        }
        if (!world.getWorld().getLevel().getData().contains("LastPlayed")) {
            world.getWorld().getLevel().getData().putLong("LastPlayed", 0);
        }
    }

    private static void convertBlock(World in, Vector3Short at, UnaryOperator<Byte> to) {
        byte blockId = in.getChunks().at(at).getBlock();
        byte newId = to.apply(blockId);
        if (blockId != newId) in.getChunks().at(at).setBlock(newId);
    }
}
