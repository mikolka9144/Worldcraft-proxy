package com.mikolka9144.worldcraft.socket.model.Errors;

import java.util.Arrays;
import java.util.Optional;

public enum LoginErrorCode {
    UNKNOWN((byte)-255),
    USER_ALREADY_LOGGED_IN((byte)-1),
    USER_IS_NULL((byte)-2),
    BANNED_IP((byte)-3),
    BANNED_DEVIDE((byte)-4),
    USERNAME_HAS_BLACKLISTED_WORD((byte)-5),
    USERNAME_TOO_LONG((byte)-6),
    USERNAME_IS_BLACKLISTED((byte)-7)
    ;
    private final byte code;
    public int getvalue() {return code;}

    LoginErrorCode(byte code) {
        this.code = code;
    }
    public static LoginErrorCode findErrorByCode(int code){
        Optional<LoginErrorCode> command = Arrays.stream(LoginErrorCode.values()).filter(s -> s.getvalue() == code).findFirst();
        return command.orElse(UNKNOWN);
    }
}
