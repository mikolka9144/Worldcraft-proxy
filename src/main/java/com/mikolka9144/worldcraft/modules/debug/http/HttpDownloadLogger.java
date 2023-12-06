package com.mikolka9144.worldcraft.modules.debug.http;

import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("http-logger")
public class HttpDownloadLogger implements HttpDownloadInterceptor {
    @Override
    public void getWorld(WorldDownloadRequest request){
        log.info(String.format("A request was sent for a world with id %d",request.getWorldId()));
    }
}
