package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import com.mikolka9144.worldcraft.socket.model.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MovementPacket {
    private int playerId;
    private Vector3 position;
    private Vector3 orientation;
    private Vector3 up;
}
