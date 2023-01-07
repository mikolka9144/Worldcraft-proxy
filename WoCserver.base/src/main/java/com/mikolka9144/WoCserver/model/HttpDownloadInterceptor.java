package com.mikolka9144.WoCserver.model;

public interface HttpDownloadInterceptor {
    byte[] getWorld(int worldId,byte[] worldBin);
}
