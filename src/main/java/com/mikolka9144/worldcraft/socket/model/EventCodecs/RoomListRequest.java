package com.mikolka9144.worldcraft.socket.model.EventCodecs;

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
    public enum RoomsType{
        ROOM_SEARCH((byte)0),
        MOST_ACTIVE((byte)1),
        MOST_ENTERED((byte)2),
        MOST_RATED((byte)3),
        READ_ONLY((byte)4);

        private final byte Id;

        public byte getId() {
            return Id;
        }

        RoomsType(byte proto) {
            this.Id = proto;
        }
        public static RoomsType findRoomTypeById(byte Id){
            Optional<RoomsType> command = Arrays.stream(RoomsType.values()).filter(s -> s.getId() == Id).findFirst();
            if(command.isPresent()) return command.get();
            log.warn("Protocol "+Id+" is not declared. Returning ROOM_SEARCH");
            return ROOM_SEARCH;
        }
    }

}
