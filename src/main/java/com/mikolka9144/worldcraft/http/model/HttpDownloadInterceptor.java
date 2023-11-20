package com.mikolka9144.worldcraft.http.model;

import com.mikolka9144.worldcraft.common.World;

public interface HttpDownloadInterceptor {
    World getWorld(int worldId, World world);
}
