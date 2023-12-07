package com.mikolka9144.worldcraft.common.api.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class RoomListRequest {

    private RoomsType roomsType;
    private int startingIndex;
    @Getter
    public enum RoomsType{
        ROOM_SEARCH((byte)0),
        MOST_ACTIVE((byte)1),
        MOST_ENTERED((byte)2),
        MOST_RATED((byte)3),
        READ_ONLY((byte)4);

        private final byte id;

        RoomsType(byte proto) {
            this.id = proto;
        }
        public static RoomsType findRoomTypeById(byte id){
            Optional<RoomsType> command = Arrays.stream(RoomsType.values()).filter(s -> s.getId() == id).findFirst();
            if(command.isPresent()) return command.get();
            log.warn("Protocol "+id+" is not declared. Returning ROOM_SEARCH");
            return ROOM_SEARCH;
        }
    }

}
