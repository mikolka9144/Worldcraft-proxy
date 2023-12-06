package com.mikolka9144.worldcraft.common.api.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessage {
    private String playerNicknameArg;
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
