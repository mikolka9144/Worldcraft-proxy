package com.mikolka9144.worldcraft.backend.server.unify.convert;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.LoginInfo;
import com.mikolka9144.worldcraft.backend.packets.codecs.RoomsPacket;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import lombok.extern.slf4j.Slf4j;

import static com.mikolka9144.worldcraft.utills.enums.BlockType.*;


@Slf4j
public class PacketConverter {
    public static byte replaceForWorldcraft(byte block) {
        BlockType blockData = BlockType.findBlockById(block);
        if (blockData == UNKNOWN) log.warn(String.format("Unknown block %s! This might cause issues.", block));
        BlockType newBlock = switch (blockData) {
            case WHEAT_CROP_ID, CARROT_CROP_ID, POTATO_CROP_ID, MELON_STEM_ID, PUMPKIN_STEM_ID -> GRASS_ID;
            case BALE_ID -> SAND_ID;
            case DARK_BRICKS_ID, BIG_BRICKS_ID, BIG_STONE_BRICKS_ID, EXTRUDED_BRICKS_ID, BLACK_BRICKS_ID, LIGHT_GRAY_BRICKS_ID ->
                    BRICK_BLOCK_ID;
            case TABLING_ID -> SPONGE_ID;
            case BRONZE_PLATE_ID, STEEL_PLATE, STONE_PLATE_ID, METAL_CONTAINER_ID -> STONEWORK_ID;
            case RELIEF_STONE_ID, DECORATED_STONE_ID -> STONE_ID;
            case ONYX_ID -> MOSS_STONE_ID;
            case MALACHITE_BLOCK_ID -> FURNACE_ACTIVE_ID;
            case STONE_TILE_ID -> OBSIDIAN_ID;
            case ASPHALT_ID -> STONE_BRICK_ID;
            case DIRT_ROAD_ID -> FARMLAND_ID;
            case PUMPKIN_LANTERN_ID -> PUMPKIN_ID;
            case IRON_TRAP_DOOR_ID, WOODEN_TRAP_DOOR_ID -> AIR;
            case CELL_DOOR_ID -> OPENED_IRON_DOOR_ID;
            case FENCE_ID -> SLAB_ID;
            case FENCE_GATE_ID -> OPENED_WOOD_DOOR_ID;
            default -> blockData;
        };
        return newBlock == UNKNOWN ? block : newBlock.getId();
    }

    private PacketConverter() {
    }

    public static byte[] encodeLegacyRooms(RoomsPacket data) {
        PacketDataBuilder builder = new PacketDataBuilder()
                .append(data.getPacketIndex())
                .append(data.getAllPackets())
                .append(data.getInitialRoomListSize())
                .append(data.getRoomType().getId());
        for (RoomsPacket.Room room : data.getRooms()) {
            builder.append(room.getId())
                    .append(room.getName())
                    .append(room.isProtected())
                    .append(room.getActivePlayers())
                    .append(room.getRoomCapacity())
                    .append(room.getNumberOfEntrances())
                    .append(room.getLikes()); // We skip read olny status in package
        }
        return builder.build();
    }

    static LoginInfo prepareLegacyLoginInfo(Packet packet) {
        PacketDataReader reader = new PacketDataReader(packet.getData());
        return new LoginInfo(
                reader.getString(),
                reader.getShort(),
                reader.getString(),
                reader.getString(),
                reader.getString(), reader.getString(), reader.getString(), "play");
    }
}
