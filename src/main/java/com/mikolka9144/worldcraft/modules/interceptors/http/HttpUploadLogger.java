package com.mikolka9144.worldcraft.modules.interceptors.http;

import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("http-logger-upload")
public class HttpUploadLogger implements HttpUploadInterceptor {

    @Override
    public void uploadWorld(WorldUploadRequest request) {

    }
}
