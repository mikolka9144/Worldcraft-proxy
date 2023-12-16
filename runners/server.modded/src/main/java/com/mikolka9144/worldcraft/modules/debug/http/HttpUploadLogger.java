package com.mikolka9144.worldcraft.modules.debug.http;

import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.model.WorldUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("http-logger-upload")
public class HttpUploadLogger implements HttpUploadInterceptor {

    @Override
    public void uploadWorld(WorldUploadRequest request) {
        log.info(String.format("A request with token %s was sent",request.getToken()));
    }
}
