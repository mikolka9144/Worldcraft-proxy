package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.common.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
@Component("direct-download")
public class DirectConnectHttpInterceptors implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public World getWorld(int worldId, World world) {
        byte[] file = Files.readAllBytes(Path.of("./game.tar.gz"));
        return World.fromTarGzBin(file);
    }
}
