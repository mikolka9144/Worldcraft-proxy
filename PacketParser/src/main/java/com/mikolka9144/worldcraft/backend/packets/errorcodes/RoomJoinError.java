package com.mikolka9144.worldcraft.backend.packets.errorcodes;

import java.util.Arrays;
import java.util.Optional;

public enum RoomJoinError {
    UNKNOWN((byte)-255),
    NO_ERROR((byte) 0),
    ROOM_DOES_NOT_EXIST((byte)-1),
    JOINING_FAILED((byte)-2),
    BAD_PASSWORD((byte)-3),
    ROOM_IS_FULL((byte)-4)
    ;
    private final byte code;
    public int getvalue() {return code;}

    RoomJoinError(byte code) {
        this.code = code;
    }
    public static RoomJoinError findErrorByCode(int code){
        Optional<RoomJoinError> command = Arrays.stream(RoomJoinError.values()).filter(s -> s.getvalue() == code).findFirst();
        return command.orElse(UNKNOWN);
    }
}
