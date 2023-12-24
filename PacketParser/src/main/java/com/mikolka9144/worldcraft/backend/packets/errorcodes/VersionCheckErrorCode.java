package com.mikolka9144.worldcraft.backend.packets.errorcodes;

import java.util.Arrays;

public enum VersionCheckErrorCode {
    NO_ERROR((byte) 0),
    UPDATE_AVAILABLE((byte) -1),
    UPDATE_REQUIRED((byte) -2),
    UNKNOWN((byte) -255);
    private final byte code;
    public byte getvalue() {return code;}

    VersionCheckErrorCode(byte code) {
        this.code = code;
    }
    public static VersionCheckErrorCode findErrorByCode(int code){
        var command = Arrays.stream(VersionCheckErrorCode.values()).filter(s -> s.getvalue() == code).findFirst();
        return command.orElse(UNKNOWN);
    }
}
