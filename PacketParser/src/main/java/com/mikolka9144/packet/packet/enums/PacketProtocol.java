package com.mikolka9144.packet.packet.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
@Getter
@Slf4j
public enum PacketProtocol{
    LEGACY_VERSION(0),
    WORLDCRAFT_2_8_7(1),
    WORLD_OF_CRAFT(2),
    SERVER(7),
    WORLD_OF_CRAFT_2_4_5(23),
    WORLD_OF_CRAFT_2_4_7(24),
    WORLD_OF_CRAFT_2_4_8(25),
    WORLD_OF_CRAFT_2_4_9(26),
    WORLD_OF_CRAFT_2_5(27),
    WORLD_OF_CRAFT_3_X(28),
    WORLD_OF_CRAFT_3_4(29),
    WORLD_OF_CRAFT_3_4_1(30),
    WORLD_OF_CRAFT_3_4_2(31),
    WORLD_OF_CRAFT_3_4_3(32),
    WORLD_OF_CRAFT_3_4_4(33),
    WORLD_OF_CRAFT_3_4_5(34),
    WORLD_OF_CRAFT_3_4_6(35),
    WORLD_OF_CRAFT_3_4_7(36),
    WORLD_OF_CRAFT_3_4_8(37),
    WORLD_OF_CRAFT_3_4_9(38),
    WORLD_OF_CRAFT_3_4_10(39),
    WORLD_OF_CRAFT_3_4_11(40),
    WORLD_OF_CRAFT_3_5(41),
    WORLD_OF_CRAFT_3_5_1(42),
    WORLD_OF_CRAFT_3_5_2(43),
    WORLD_OF_CRAFT_3_5_3(44),
    WORLD_OF_CRAFT_3_5_4(45),
    WORLD_OF_CRAFT_3_5_5(46),
    WORLD_OF_CRAFT_3_5_6(47),
    WORLD_OF_CRAFT_3_5_7(48),
    WORLD_OF_CRAFT_3_5_8(49),
    WORLD_OF_CRAFT_3_5_9(50),
    WORLD_OF_CRAFT_3_5_10(51),
    WORLD_OF_CRAFT_3_5_11(52),
    WORLD_OF_CRAFT_3_5_12(53),
    WORLD_OF_CRAFT_3_6(54),
    WORLD_OF_CRAFT_3_6_1(55),
    WORLD_OF_CRAFT_3_6_2(56),
    WORLD_OF_CRAFT_3_6_3(57),
    WORLD_OF_CRAFT_3_7_1(58),
    WORLD_OF_CRAFT_3_7_2(59),
    WORLD_OF_CRAFT_3_7_3(60),
    WORLD_OF_CRAFT_3_7_4(61),
    WORLD_OF_CRAFT_3_7_5(62),
    WORLD_OF_CRAFT_3_7_6(63),
    WORLD_OF_CRAFT_3_7_7(64),
    WORLD_OF_CRAFT_3_8(65),
    WORLD_OF_CRAFT_3_8_1(66),
    WORLD_OF_CRAFT_3_8_2(67),
    WORLD_OF_CRAFT_3_8_3(68),
    WORLD_OF_CRAFT_3_8_4(69),
    WORLD_OF_CRAFT_3_8_5(70),
    WORLD_OF_CRAFT_3_8_6(71),
    WORLD_OF_CRAFT_3_8_7(72),
    WORLD_OF_CRAFT_3_8_8(73),
    WORLD_OF_CRAFT_3_8_9(74),
    WORLD_OF_CRAFT_3_8_10(75),
    WORLD_OF_CRAFT_3_8_11(76),
    WORLD_OF_CRAFT_3_8_12(77),
    WORLD_OF_CRAFT_3_8_13(78),
    UNKNOWN(200),
    WORLDCRAFT_2_7_4(201),
    WORLD_OF_CRAFT_1_2(202);
    private final int proto;

    PacketProtocol(int proto) {
        this.proto = proto;
    }
    public static PacketProtocol findPacketProtoById(int id){
        Optional<PacketProtocol> command = Arrays.stream(PacketProtocol.values()).filter(s -> s.getProto() == id).findFirst();
        if(command.isPresent()) return command.get();
        log.warn("Protocol "+id+" is not declared. Returning UNKNOWN");
        return UNKNOWN;
    }
}
