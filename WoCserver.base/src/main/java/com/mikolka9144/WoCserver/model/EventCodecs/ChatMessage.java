package com.mikolka9144.WoCserver.model.EventCodecs;

public class ChatMessage {
    private String message;
    private String msgTypeArg;

    public ChatMessage(String msgTypeArg, String message, MsgType type) {
        this.message = message;
        this.msgTypeArg = msgTypeArg;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgTypeArg() {
        return msgTypeArg;
    }

    public void setMsgTypeArg(String msgTypeArg) {
        this.msgTypeArg = msgTypeArg;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    private MsgType type;
    public enum MsgType{
        STANDARD,
        PLAYER_JOINED,
        PLAYER_GOT_WARNING,
        PLAYER_LEFT,
        WORLDGUARD_BONK
    }
}
