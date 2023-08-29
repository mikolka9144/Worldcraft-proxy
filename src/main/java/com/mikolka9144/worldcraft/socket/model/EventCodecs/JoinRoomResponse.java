package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JoinRoomResponse {
    private boolean isUserOwner;
    private boolean isWorldReadOnly;
}
