package com.mikolka9144.worldcraft.http.modules;

import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("http-logger")
public class HttpDownloadLogger implements HttpDownloadInterceptor {
    @Override
    public byte[] getWorld(int worldId, byte[] worldBin) {
        log.info(String.format("A request was sent for a world with id %d",worldId));
        return worldBin;
    }
}
