package com.mikolka9144.WoCserver.model;

public interface HttpUploadInterceptor {
    byte[] uploadWorld(byte[] worldBin, String ContentType);
}
