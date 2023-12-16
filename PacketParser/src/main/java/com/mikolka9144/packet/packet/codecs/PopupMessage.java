package com.mikolka9144.packet.packet.codecs;


import com.mikolka9144.worldcraft.utills.ParsingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class PopupMessage {
    public PopupMessage(PopupMessageType messagePreset, String textParameter,long timeParameter) {
        this(messagePreset,timeParameter,textParameter,"Preset message. No legacy message");
    }

    public PopupMessage(String baseMessage) {
        this(PopupMessageType.NONE,0,"",baseMessage);
    }

    private PopupMessageType messagePreset;
    private long timeParameter;
    private String textParameter;
    private String baseMessage;
    @Getter
    public enum PopupMessageType
    {
        NONE(0),
        READ_ONLY(1),
        REPORTED_ABUSE(2),
        ABUSE_AND_REMOVED(3),
        PLAYER_WILL_BE_BANNED(4),
        ABUSE_TOO_OFTEN(5),
        ABUSE_TEXT_IS_TOO_LONG(6);
        private final byte value;


        public static PopupMessageType findByValue(byte value){
            Optional<PopupMessageType> val = Arrays.stream(PopupMessageType.values()).filter(s -> s.getValue() == value).findFirst();
            if(val.isPresent()) return val.get();
            throw new ParsingException("Message of type "+value+" is not declared");
        }

        PopupMessageType(int value) {
            this.value = (byte)value;
        }
    }
}
