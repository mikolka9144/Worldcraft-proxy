package com.mikolka9144.worldcraft.backend.server.http.interceptors;

import com.mikolka9144.worldcraft.backend.server.http.model.WorldDownloadRequest;

public interface HttpDownloadInterceptor {
    void getWorld(WorldDownloadRequest request);
}
