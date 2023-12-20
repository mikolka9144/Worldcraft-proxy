package com.mikolka9144.worldcraft.backend.packets.codecs;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class RoomsPacket {
    /**
     * Represents packet index in a list of packets for room list. (from 0 to allPackets-1)
     */
    private int packetIndex;
    /**
     * Number of packets in a list of room packets
     */
    private int allPackets;
    /**
     * Initial size of list for ALL room packages for specific list
     */
    private short initialRoomListSize;
    private RoomListRequest.RoomsType roomType;
    private List<Room> rooms = new ArrayList<>();

    public RoomsPacket(int packetIndex, int allPackets, short initialRoomListSize,  RoomListRequest.RoomsType roomType) {
        this.packetIndex = packetIndex;
        this.allPackets = allPackets;
        this.initialRoomListSize = initialRoomListSize;
        this.roomType = roomType;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Room {
        private int id;
        private String name;
        private boolean isProtected;
        private short activePlayers;
        private short roomCapacity;
        private int numberOfEntrances;
        private int likes;
        private boolean isReadOnly;
    }
}
