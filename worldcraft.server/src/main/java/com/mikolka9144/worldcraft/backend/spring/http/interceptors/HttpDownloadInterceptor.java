package com.mikolka9144.worldcraft.backend.spring.http.interceptors;

import com.mikolka9144.worldcraft.backend.spring.http.model.WorldDownloadRequest;

public interface HttpDownloadInterceptor {
    void getWorld(WorldDownloadRequest request);
}
