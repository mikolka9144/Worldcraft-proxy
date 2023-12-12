package com.mikolka9144.worldcraft.http.interceptors;

import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;

public interface HttpDownloadInterceptor {
    void getWorld(WorldDownloadRequest request);
}
