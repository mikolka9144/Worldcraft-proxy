package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.Vector3;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovementPacket {
    private int playerId;
    private Vector3 position;
    private Vector3 orientation;
    private Vector3 up;
}
