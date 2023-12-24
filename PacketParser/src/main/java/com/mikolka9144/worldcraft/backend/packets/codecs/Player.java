package com.mikolka9144.worldcraft.backend.packets.codecs;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Player {
    private int id;
    private String nickname;
    private short skinId;
    private MovementPacket pos;
}
