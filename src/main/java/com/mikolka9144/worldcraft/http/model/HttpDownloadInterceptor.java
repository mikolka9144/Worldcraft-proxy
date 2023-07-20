package com.mikolka9144.worldcraft.http.model;

import com.mikolka9144.worldcraft.common.level.World;

public interface HttpDownloadInterceptor {
    World getWorld(int worldId, World world);
}
