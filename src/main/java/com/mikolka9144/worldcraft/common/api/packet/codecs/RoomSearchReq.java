package com.mikolka9144.worldcraft.common.api.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchReq {
    private String query;
    private int startingIndex;
}
