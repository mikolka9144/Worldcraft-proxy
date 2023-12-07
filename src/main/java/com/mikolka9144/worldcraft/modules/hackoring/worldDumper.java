package com.mikolka9144.worldcraft.modules.hackoring;

import com.mikolka9144.worldcraft.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
@Component("dump")
public class worldDumper implements HttpUploadInterceptor {
    @Override
    @SneakyThrows
    public void uploadWorld(WorldUploadRequest request)  {
        Files.write(Path.of("./test.tar.gz"),request.getWorld(), StandardOpenOption.CREATE_NEW);
    }
}
