package com.mikolka9144.WoCserver.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    private String msgTypeArg;
    private String message;
    private MsgType type;

    public enum MsgType{
        STANDARD,
        PLAYER_JOINED,
        PLAYER_GOT_WARNING,
        PLAYER_LEFT,
        WORLDGUARD_BONK
    }
}
