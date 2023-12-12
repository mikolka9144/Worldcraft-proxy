package com.mikolka9144.packet.packet.codecs;

import com.mikolka9144.packet.Vector3;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MovementPacket {
    private int playerId;
    private Vector3 position;
    private Vector3 orientation;
    private Vector3 base;
}
