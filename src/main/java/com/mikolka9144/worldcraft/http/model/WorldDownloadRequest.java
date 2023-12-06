package com.mikolka9144.worldcraft.http.model;

import com.mikolka9144.worldcraft.common.api.level.World;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorldDownloadRequest{
    private int worldId;
    private World world;
}
