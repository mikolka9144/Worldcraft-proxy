package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class RoomsPacket {
    private int packetIndex;
    private int allPackets;
    private short initialRoomListSize;
    private byte roomType;
    private List<Room> rooms = new ArrayList<>();

    public RoomsPacket(int packetIndex, int allPackets, short initialRoomListSize, byte roomType) {

        this.packetIndex = packetIndex;
        this.allPackets = allPackets;
        this.initialRoomListSize = initialRoomListSize;
        this.roomType = roomType;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Room {
        private int Id;
        private String name;
        private boolean isProtected;
        private short activePlayers;
        private short roomCapacity;
        private int numberOfEntrances;
        private int likes;
        private boolean isReadOlny;
    }
}
