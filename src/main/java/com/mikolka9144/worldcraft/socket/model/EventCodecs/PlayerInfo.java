package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerInfo {
    private int Id;
    private String nickname;
    private short skinId;
}
