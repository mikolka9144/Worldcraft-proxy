package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class PopupMessage {
    private PopupMessageType messagePreset;
    private long timeParameter;
    private String textParameter;
    private String baseMessage;
    public enum PopupMessageType
    {
        NONE(0),
        READ_ONLY(1),
        REPORTED_ABUSE(2),
        ABUSE_AND_REMOVED(3),
        PLAYER_WILL_BE_BANNED(4),
        ABUSE_TOO_OFTEN(5),
        ABUSE_TEXT_IS_TOO_LONG(6);
        private byte value;


        public byte getValue() {
            return value;
        }

        public static PopupMessageType findByValue(byte value){
            Optional<PopupMessageType> val = Arrays.stream(PopupMessageType.values()).filter(s -> s.getValue() == value).findFirst();
            if(val.isPresent()) return val.get();
            throw new RuntimeException("Message of type "+value+" is not declared");
        }

        PopupMessageType(int value) {
            this.value = (byte)value;
        }
    }
}
