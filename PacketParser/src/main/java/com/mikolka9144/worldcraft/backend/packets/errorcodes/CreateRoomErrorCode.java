package com.mikolka9144.worldcraft.backend.packets.errorcodes;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
@Slf4j
public enum CreateRoomErrorCode {
    UNKNOWN((byte)-255),
    NO_ERROR((byte) 0),
    USER_IS_NULL((byte)-1),
    LOGIN_REQUIRED((byte)-2),
    ROOM_ALREADY_EXISTS((byte)-3),
    RESERVED((byte)-4),
    CREATING_ROOM_FAILED((byte)-5),
    ROOM_NAME_HAS_BLACKLISTED_WORD((byte)-6),
    ROOM_NAME_TOO_LONG((byte)-7),
    ROOM_NAME_IS_BLACKLISTED((byte)-8),
    ROOM_PASSWORD_TOO_LONG((byte)-9)
    ;
    private final byte code;
    public byte getvalue() {return code;}

    CreateRoomErrorCode(byte code) {
        this.code = code;
    }
    public static CreateRoomErrorCode findErrorByCode(int code){
        Optional<CreateRoomErrorCode> command = Arrays.stream(CreateRoomErrorCode.values()).filter(s -> s.getvalue() == code).findFirst();
        return command.orElse(UNKNOWN);
    }
}
