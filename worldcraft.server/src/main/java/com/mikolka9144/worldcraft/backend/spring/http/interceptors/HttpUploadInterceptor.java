package com.mikolka9144.worldcraft.backend.spring.http.interceptors;

import com.mikolka9144.worldcraft.backend.spring.http.model.WorldUploadRequest;

public interface HttpUploadInterceptor {
    void uploadWorld(WorldUploadRequest request);
}
