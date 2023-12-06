package com.mikolka9144.worldcraft.modules.simba;

import com.mikolka9144.worldcraft.common.api.level.World;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
@Component("direct-download")
public class DirectConnectHttpInterceptors implements HttpDownloadInterceptor {
    @SneakyThrows
    @Override
    public void getWorld(WorldDownloadRequest request){
        byte[] file = Files.readAllBytes(Path.of("./game.tar.gz"));
        request.setWorld(World.fromTarGzBin(file));
    }
}
