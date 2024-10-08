package com.mikolka9144.worldcraft.backend.server.http.model;

import com.mikolka9144.worldcraft.backend.level.World;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorldDownloadRequest {
    private int worldId;
    private World world;
    private List<String> flags;
}
