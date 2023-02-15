package com.mikolka9144.worldcraft.http.model;

public interface HttpDownloadInterceptor {
    byte[] getWorld(int worldId,byte[] worldBin);
}
