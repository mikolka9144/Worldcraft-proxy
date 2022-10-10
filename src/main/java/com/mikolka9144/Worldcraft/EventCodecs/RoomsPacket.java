package com.mikolka9144.Worldcraft.EventCodecs;

import java.util.List;

public class RoomsPacket {
    private int packetIndex;
    private int allPackets;
    private short initialRoomListSize;
    private byte roomType;
    private List<Room> rooms;

    public int getPacketIndex() {
        return packetIndex;
    }

    public int getAllPackets() {
        return allPackets;
    }

    public short getInitialRoomListSize() {
        return initialRoomListSize;
    }

    public byte getRoomType() {
        return roomType;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public RoomsPacket(int packetIndex, int allPackets, short initialRoomListSize, byte roomType, List<Room> rooms){

        this.packetIndex = packetIndex;
        this.allPackets = allPackets;
        this.initialRoomListSize = initialRoomListSize;
        this.roomType = roomType;
        this.rooms = rooms;
    }
    public class Room{
        private int Id;
        private String name;
        private Boolean isProtected;
        private short activePlayers;
        private short roomCapacity;
        private int numberOfEntrances;
        private int likes;
        private Boolean isReadOlny;

        public Room(int id, String name, Boolean isProtected, short activePlayers,
                    short roomCapacity, int numberOfEntrances, int likes, Boolean isReadOlny) {
            Id = id;
            this.name = name;
            this.isProtected = isProtected;
            this.activePlayers = activePlayers;
            this.roomCapacity = roomCapacity;
            this.numberOfEntrances = numberOfEntrances;
            this.likes = likes;
            this.isReadOlny = isReadOlny;
        }

        public int getId() {
            return Id;
        }

        public String getName() {
            return name;
        }

        public Boolean getProtected() {
            return isProtected;
        }
        public short getActivePlayers() {
            return activePlayers;
        }

        public short getRoomCapacity() {
            return roomCapacity;
        }

        public int getNumberOfEntrances() {
            return numberOfEntrances;
        }


        public int getLikes() {
            return likes;
        }

        public Boolean getReadOlny() {
            return isReadOlny;
        }
    }
}
