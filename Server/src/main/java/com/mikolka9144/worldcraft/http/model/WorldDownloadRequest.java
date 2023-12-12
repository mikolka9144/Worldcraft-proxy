package com.mikolka9144.worldcraft.http.model;

import com.mikolka9144.level.World;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorldDownloadRequest{
    private int worldId;
    private World world;
    private List<String> flags;
}
