package com.mikolka9144.worldcraft.modules.worldcraft.unify;

import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("world-simplify-early")
@Slf4j
public class EarlyWorldConvert implements HttpDownloadInterceptor {

    @Override
    public void getWorld(WorldDownloadRequest request) {
        if (request.getWorldId() < 0) {
            log.info("WORLD IS NEGATIVE");
            request.setWorldId(-request.getWorldId());
            request.getFlags().add("convert");
        }
    }
}
