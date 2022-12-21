package com.mikolka9144.WoCserver.model.EventCodecs;

import com.mikolka9144.WoCserver.utills.PacketParsers.Vector3;
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
