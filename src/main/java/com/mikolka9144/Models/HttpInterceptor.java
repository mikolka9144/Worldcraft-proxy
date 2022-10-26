package com.mikolka9144.Models;

public interface HttpInterceptor {
    byte[] getWorld(int worldId,byte[] worldBin);
    byte[] uploadWorld(byte[] worldBin, String ContentType);
}
