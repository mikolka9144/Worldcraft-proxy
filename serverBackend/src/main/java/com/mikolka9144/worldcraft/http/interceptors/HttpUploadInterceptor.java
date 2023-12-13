package com.mikolka9144.worldcraft.http.interceptors;

import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;

public interface HttpUploadInterceptor {
    void uploadWorld(WorldUploadRequest request);
}
