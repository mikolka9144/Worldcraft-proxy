package com.mikolka9144.worldcraft.modules.worldcraft.unify;

import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.common.api.level.chunks.ChunksMCR;
import com.mikolka9144.worldcraft.common.models.Vector3Short;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import com.mikolka9144.worldcraft.modules.officialConnect.OfficialDownloader;
import com.mikolka9144.worldcraft.modules.worldcraft.unify.convert.PacketConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Slf4j
@Component("world-official-simplify")
public class DownloadCrawler implements HttpDownloadInterceptor {
    private final OfficialDownloader officialDownloader;

    public DownloadCrawler(OfficialDownloader officialDownloader) {
        this.officialDownloader = officialDownloader;
    }

    @Override
    public void getWorld(WorldDownloadRequest world) {
        log.info("LOGGING WORLD!!!");
        boolean isNegative = false;
        if (world.getWorldId() < 0) {
            world.setWorldId(-world.getWorldId());
            log.info("WORLD IS NEGATIVE");
            isNegative = true;
        }
        officialDownloader.getWorld(world);
        if(isNegative) enumerateWorld(x -> convertBlock(world.getWorld(), x, PacketConverter::replaceForWorldcraft));

        log.info("DONE LOGGING WORLD!!!");

    }

    private static void convertBlock(World in, Vector3Short at, UnaryOperator<Byte> to) {
        try {
            byte blockId = in.getChunks().getBlock(at.getX(), at.getY(), at.getZ());
            byte newId = to.apply(blockId);
            if (blockId != newId) in.getChunks().setBlock(at.getX(), at.getY(), at.getZ(), newId);
        } catch (NullPointerException exp) {
            log.warn(String.format("Block at %d %d %d is not avaliable. Is this fine?", at.getX(), at.getY(), at.getZ()));
        }
    }

    private void enumerateWorld(Consumer<Vector3Short> step) {
        for (short x = 0; x < ChunksMCR.MAX_COORD_CANON; x++) {
            for (short y = 10; y < ChunksMCR.MAX_COORD_CANON; y++) {
                for (short z = 0; z < ChunksMCR.MAX_COORD_CANON; z++) {
                    step.accept(new Vector3Short(x, y, z));
                }
            }
        }
    }
}
