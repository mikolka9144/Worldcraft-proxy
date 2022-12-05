package com.mikolka9144.Models.EventCodecs;

public class PlayerAction {
    private int playerId;

    public PlayerAction(int playerId, ActionType actionType) {
        this.playerId = playerId;
        this.actionType = actionType;
    }

    private ActionType actionType;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public enum ActionType{
        NONE, // not used by world craft
        TAP,
        HOLD,
        RELEASE
    }
}
