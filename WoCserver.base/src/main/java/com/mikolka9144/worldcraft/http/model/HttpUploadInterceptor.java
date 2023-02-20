package com.mikolka9144.worldcraft.http.model;

public interface HttpUploadInterceptor {
    byte[] uploadWorld(byte[] worldBin, String contentType);
}
