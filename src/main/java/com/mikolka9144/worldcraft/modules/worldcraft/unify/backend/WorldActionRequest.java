package com.mikolka9144.worldcraft.modules.worldcraft.unify.backend;

import com.mikolka9144.worldcraft.common.api.level.World;

import java.util.function.Consumer;

public record WorldActionRequest(int worldId, Consumer<World> action) {
}
