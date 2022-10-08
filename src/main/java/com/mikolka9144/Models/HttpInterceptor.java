package com.mikolka9144.Models;

public interface HttpInterceptor {
    byte[] getWorld(int worldId,byte[] worldBin);
    byte[] uploadWorld(int worldId,byte[] worldBin);
}
