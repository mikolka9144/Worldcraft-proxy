package com.mikolka9144.worldcraft.socket.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
@Slf4j
public enum PacketProtocol{
    WORLDCRAFT_V_2_8_7(1,false),
    WORLD_OF_CUBES_V_1_7(2,false),
    UNKNOWN(-1,false),
    WORLDCRAFT_V_2_7_4(0,false),
    SERVER(7,true),
    WORLD_OF_CRAFT_V_3_8_5(70,true),
    WORLD_OF_CRAFT_V_3_8_6(71,true),
    WORLD_OF_CRAFT_V_3_8_7(72,true),
    WORLD_OF_CRAFT_V_3_8_8(73,true),
    WORLD_OF_CRAFT_V_3_8_9(74,true),
    WORLD_OF_CRAFT_V_3_8_10(75,true),
    WORLD_OF_CRAFT_V_3_8_11(76,true),
    WORLD_OF_CRAFT_V_3_8_12(77,true);
    private final int Proto;

    public boolean isWorldOfCraft() {
        return isWorldOfCraft;
    }

    private final boolean isWorldOfCraft;

    public int getProto() {
        return Proto;
    }

    PacketProtocol(int proto,boolean isWorldOfCraft) {
        this.Proto = proto;
        this.isWorldOfCraft = isWorldOfCraft;
    }
    public static PacketProtocol findPacketProtoById(int Id){
        Optional<PacketProtocol> command = Arrays.stream(PacketProtocol.values()).filter(s -> s.getProto() == Id).findFirst();
        if(command.isPresent()) return command.get();
        log.warn("Protocol "+Id+" is not declared. Returning UNKNOWN");
        return UNKNOWN;
    }
}
