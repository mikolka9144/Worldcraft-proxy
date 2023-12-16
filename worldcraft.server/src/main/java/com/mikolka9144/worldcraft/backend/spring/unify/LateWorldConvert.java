package com.mikolka9144.worldcraft.backend.spring.unify;


import com.mikolka9144.worldcraft.level.Terrain;
import com.mikolka9144.worldcraft.level.World;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.model.WorldDownloadRequest;
import com.mikolka9144.worldcraft.backend.spring.unify.convert.PacketConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@Component("world-simplify-late")
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
    }

    private static void convertBlock(World in, Vector3Short at, UnaryOperator<Byte> to) {
        byte blockId = in.getChunks().at(at).getBlock();
        byte newId = to.apply(blockId);
        if (blockId != newId) in.getChunks().at(at).setBlock(newId);
    }
}
