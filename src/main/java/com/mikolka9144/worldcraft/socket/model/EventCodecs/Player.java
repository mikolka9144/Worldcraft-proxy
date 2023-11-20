package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import com.mikolka9144.worldcraft.socket.model.Vector3;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Player {
    private int Id;
    private String nickname;
    private short skinId;
    private Vector3 position;
    private Vector3 at;
    private Vector3 up;
}
