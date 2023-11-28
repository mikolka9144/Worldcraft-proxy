package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockData {
    private static final int CHUNK_SIZE = 16;
    private short x;
    private short y;
    private short z;

    private short chunkX;
    private short chunkZ;
    private BlockType blockType;
    private byte blockData;
    private byte prevBlockType;
    private byte prevBlockData;
    public Vector3Short getPosition(){
        return new Vector3Short(
                (short)(x+(chunkX*CHUNK_SIZE)),
                y,
                (short)(z+(chunkZ*CHUNK_SIZE)));
    }
    public void setPosition(Vector3Short position){
        x = (short) (position.getX()%CHUNK_SIZE);
        y = position.getY();
        z = (short) (position.getZ()%CHUNK_SIZE);
        chunkX = (short) (position.getX()/CHUNK_SIZE);
        chunkZ = (short) (position.getZ()/CHUNK_SIZE);
    }
    public BlockData(short x, short y, short z, short chunkX, short chunkZ, BlockType blockType, byte blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockType = blockType;
        this.blockData = blockData;
    }
    public BlockData(Vector3Short position, BlockType blockType, byte blockData) {
        setPosition(position);
        this.blockType = blockType;
        this.blockData = blockData;
    }
    public BlockData(Vector3Short position, BlockType blockType, byte blockData,byte prevBlockType,byte prevBlockData) {
        setPosition(position);
        this.blockType = blockType;
        this.blockData = blockData;
        this.prevBlockType = prevBlockType;
        this.prevBlockData = prevBlockData;
    }

    public enum BlockType {
        UNKNOWN(-1),
        AIR(0),
        STONE_ID(1),
        DIRT_WITH_GRASS_ID(2),
        DIRT_ID(3),

        COBBLE_ID(4),

        WOODEN_PLANKS_ID(5),

        BEDROCK_ID(7),

        WATER_ID(8),

        STILL_WATER_ID(9),

        LAVA_ID(10),

        STILL_LAVA_ID(11),

        SAND_ID(12),

        GRAVEL_ID(13),

        GOLD_ORE_ID(14),

        IRON_ORE_ID(15),

        COAL_ORE_ID(16),

        WOOD_ID(17),

        LEAVES_ID(18),

        SPONGE_ID(19),

        GLASS_ID(20),

        LAPIS_LAZULI_ORE_ID(21),

        LAPIS_LAZULI_BLOCK_ID(22),

        DISPENSER_ID(23),

        SAND_STONE_ID(24),

        NOTE_BLOCK_ID(25),

        WOOL_ID(35),

        GOLD_BLOCK_ID(41),

        IRON_BLOCK_ID(42),

        DOUBLE_SLAB_ID(43),

        SLAB_ID(44),

        BRICK_BLOCK_ID(45),

        TNT_ID(46),

        BOOKSHELF_ID(47),

        MOSS_STONE_ID(48),

        OBSIDIAN_ID(49),

        CHEST_ID(54),

        DIAMOND_ORE_ID(56),

        DIAMOND_BLOCK_ID(57),

        WORK_BENCH_ID(58),

        FARMLAND_ID(60),

        FURNACE_ID(61),

        CLOSED_WOOD_DOOR_ID(62),

        STICK_ID(63),

        OPENED_WOOD_DOOR_ID(64),

        CLOSED_IRON_DOOR_ID(65),

        OPENED_IRON_DOOR_ID(66),

        REDSTONE_ORE_ID(73),

        BED_ID(75),

        LADDER_ID(76),

        SnowId(78),

        SnowBlockId(79),

        ICE_ID(80),

        CACTUS_ID(81),

        CLAY_ORE_ID(82),

        JUKEBOX_ID(84),

        PUMPKIN_ID(86),

        NETHERRACK_ID(87),

        SOUL_SAND_ID(88),

        GLOW_STONE_ID(89),

        TORCH_ID(90),

        BLACK_ROCK_ID(91),

        PAVING_STONE_ID(92),

        WOOL_RED_ID(93),

        INLAID_BRICK_ID(94),

        WALLPAPER_ID(95),

        TILE_ID(96),

        WOOL_BROWN_ID(97),

        WOOL_YELLOW_ID(98),

        INLAID_MARBLE_ID(99),

        MARBLE_ID(100),

        MOSSY_BRICK_ID(101),

        PAVING_SANDSTONE_ID(102),

        DALLE_RED_ID(103),

        STONEWORK_ID(104),

        NETHER_BRICK_ID(105),

        OBSIDIAN2_ID(106),

        IRON_INGOT_ID(107),

        GOLD_INGOT_ID(108),

        STONE_BRICK_ID(109),

        MELON_ID(110),
        EMERALD_ID(112),

        CHISELED_SANDSTONE_ID(113),

        MOSS_STONE2_ID(114),

        STONE_BRICK_MOSSY_ID(115),

        WOOD_PLANK_PINE_ID(116),

        WOOD_PLANK_JUNGLE_ID(117),

        LEAVES_JUNGLE_ID(118),

        FURNACE_ACTIVE_ID(119),

        GRASS_ID(121),

        FLOWER_ID(122),

        BRICK_ID(120),

        CROPS_BLOCK_ID(154),

        CARROT_BLOCK_ID(167),

        POTATO_BLOCK_ID(169),

        DARK_BRICKS_ID(174),

        TABLING_ID(175),

        BIG_STONE_BRICKS_ID(176),

        BigBricks(177),

        ExtrudedBricks(178),

        LightGrayBricks(179),

        STEEL_PLATE(180),

        MetalContainer(181),
        BRONZE_PLATE_ID(182),
        RELIEF_STONE_ID(183),
        ONYX_ID(184),
        DECORATED_STONE_ID(185),
        STONE_PLATE_ID(186),
        MALACHITE_BLOCK_ID(187),
        STONE_TILE_ID(188),
        BLACK_BRICKS_ID(189),
        ASPHALT_ID(190),
        DIRT_ROAD_ID(191),
        PUMPKIN_LANTERN_ID(192),
        PUMPKIN_STEM_ID(195),
        MELON_STEM_ID(196),
        MOB_SPAWNER_ID(200),
        CELL_DOOR_ID(197),
        WOODEN_TRAP_DOOR_ID(201),
        IRON_TRAP_DOOR_ID(202),
        FENCE_ID(85),
        FENCE_ITEM_ID(205),
        FENCE_GATE_ID(216),
        FENCE_GATE_ITEM_ID(225),


        Mushroom(236);

        private final byte id;


        BlockType(int blockId) {
            this.id = (byte) blockId;
        }

        public static BlockType findBlockById(byte Id) {
            Optional<BlockType> command = Arrays.stream(BlockType.values()).filter(s -> s.getId() == Id).findFirst();
            return command.orElse(UNKNOWN);
        }

        public byte getId() {
            return id;
        }
    }
}
