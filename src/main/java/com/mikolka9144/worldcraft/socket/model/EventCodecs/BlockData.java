package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class BlockData {
    private short x;
    private short y;
    private short z;

    private short chunkX;
    private short chunkZ;
    private byte blockType;
    private byte blockData;
    private byte prevBlockData;
    private byte prevBlockType;


    public BlockData(short x, short y, short z, short chunkX, short chunkZ, byte blockType, byte blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.blockType = blockType;
        this.blockData = blockData;
    }
    public enum BlockType{
        AIR(0),
        BEDROCK(7),
        BED(75),
        BOOKSHELF(47),
        BRICK_BLOCK(45),
        BRICK(120),
        CACTUS(81),
        CHEST(54),
        CLAY_ORE(82),
        CLOSED_IRON_DOOR(65),
        CLOSED_WOOD_DOOR(62),
        COAL_ORE(16),
        COBBLE(4),
        CRAFTING_TABLE(58),
        DIAMOND_BLOCK(57),
        DIAMOND_INGOT(111),
        DIAMOND_ORE(56),
        DIRT(3),
        DIRT_WITH_GRASS(2),
        DISPENSER(23),
        DOUBLE_SLAB(43),
        EMERALD(112),
        FARMLAND(60),
        FLOWER(122),
        FURNACE_ACTIVE(119),
        FURNACE(61),
        GLASS(20),
        GLOW_STONE(89),
        GOLD_BLOCK(41),
        GOLD_INGOT(108),
        GOLD_ORE(14),
        GRASS(121),
        GRAVEL(13),
        ICE(80),
        IRON_BLOCK(42),
        IRON_ORE(15),
        JUKEBOX(84),
        LADDER(76),
        LAPIS_LAZULI_BLOCK(22),
        LAPIS_LAZULI_ORE(21),
        LAVA(10),
        LEAVES(18),
        LEAVES_JUNGLE(118),
        MELON(110),
        MOSS_STONE2(114),
        MOSS_STONE(48),
        NETHERRACK(87),
        NETHER_BRICK(105),
        NOTE_BLOCK(25),
        OBSIDIAN2(106),
        OBSIDIAN(49),
        OPENED_IRON_DOOR(66),
        OPENED_WOOD_DOOR(64),
        PUMPKIN(86),
        REDSTONE_ORE(73),
        ROTTEN_FLESH(70),
        SANDSTONE2(113),
        SAND(12),
        SAND_STONE(24),
        SLAB(44),
        SNOWY_GRASS(78),
        SNOW(79);
        private byte id;


        public byte getId() {
            return id;
        }

        public static PacketCommand findBlockById(byte Id){
            Optional<PacketCommand> command = Arrays.stream(PacketCommand.values()).filter(s -> s.getCommand() == Id).findFirst();
            if(command.isPresent()) return command.get();
            throw new RuntimeException("Block "+Id+" is not declared");
        }

        BlockType(int blockId) {
            this.id = (byte)blockId;
        }
    }
}
