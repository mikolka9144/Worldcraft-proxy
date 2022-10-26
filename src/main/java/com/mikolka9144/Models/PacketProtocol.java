package com.mikolka9144.Models;

import java.util.Arrays;
import java.util.Optional;

public enum PacketProtocol{
    WORLDCRAFT_V_2_8_7(1),
    WORLDCRAFT_V_2_7_4(0),
    SERVER(7),
    WORLD_OF_CRAFT_V_3_8_5(70);
    private int Proto;

    public int getProto() {
        return Proto;
    }

    PacketProtocol(int proto) {
        this.Proto = proto;
    }
    public static PacketProtocol findPacketProtoById(int Id){
        Optional<PacketProtocol> command = Arrays.stream(PacketProtocol.values()).filter(s -> s.getProto() == Id).findFirst();
        if(command.isPresent()) return command.get();
        throw new RuntimeException("Protocol "+Id+" is not declared");
    }
}
