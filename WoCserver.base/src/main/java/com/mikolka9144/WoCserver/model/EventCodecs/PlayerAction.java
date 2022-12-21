package com.mikolka9144.WoCserver.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayerAction {
    private int playerId;
    private ActionType actionType;
    public enum ActionType{
        NONE, // not used by world craft
        TAP,
        HOLD,
        RELEASE
    }
}
