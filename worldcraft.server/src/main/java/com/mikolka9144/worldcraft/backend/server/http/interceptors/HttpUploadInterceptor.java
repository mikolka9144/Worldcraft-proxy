package com.mikolka9144.worldcraft.backend.server.http.interceptors;

import com.mikolka9144.worldcraft.backend.server.http.model.WorldUploadRequest;

public interface HttpUploadInterceptor {
    void uploadWorld(WorldUploadRequest request);
}
