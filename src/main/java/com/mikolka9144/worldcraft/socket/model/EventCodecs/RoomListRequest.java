package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class RoomListRequest {

    private RoomsType roomsType;
    private int startingIndex;
    public enum RoomsType{
        ROOM_SEARCH,
        MOST_PLAYED,
        REV1, // unknown for the time being
        MOST_RATED,
        READ_ONLY
    }

}
