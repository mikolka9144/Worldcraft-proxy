package com.mikolka9144.worldcraft.backend.spring.unify.backend;

import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record VersionFlags(
        boolean simplifyLogin,
        boolean shoudIncludeReadOlnyRoomStatus,
        boolean simplifyChat,
        boolean simplifyAlerts,
        boolean simplifyBlocks) {

    public static VersionFlags getFlags(PacketProtocol proto) {
        if(proto == PacketProtocol.LEGACY_VERSION)
            log.warn("Getting feature flags for LEGACY_VERSION? Is everything alright?");
        return switch (proto) {
            case WORLDCRAFT_2_8_7,LEGACY_VERSION, WORLD_OF_CRAFT_1_2, WORLDCRAFT_2_7_4 -> new VersionFlags(true, false, true, true,true);
            case WORLD_OF_CRAFT -> new VersionFlags(true, true, false, false,false);
            default -> new VersionFlags(false, true, false, false,false);
        };
    }
}
