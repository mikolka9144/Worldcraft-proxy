package com.mikolka9144.Models.EventCodecs;

import java.util.ArrayList;
import java.util.List;

public class RoomsPacket {
    private int packetIndex;
    private int allPackets;
    private short initialRoomListSize;
    private byte roomType;
    private List<Room> rooms = new ArrayList<>();

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

    public void setPacketIndex(int packetIndex) {
        this.packetIndex = packetIndex;
    }

    public void setAllPackets(int allPackets) {
        this.allPackets = allPackets;
    }

    public void setInitialRoomListSize(short initialRoomListSize) {
        this.initialRoomListSize = initialRoomListSize;
    }

    public void setRoomType(byte roomType) {
        this.roomType = roomType;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public RoomsPacket(int packetIndex, int allPackets, short initialRoomListSize, byte roomType) {

        this.packetIndex = packetIndex;
        this.allPackets = allPackets;
        this.initialRoomListSize = initialRoomListSize;
        this.roomType = roomType;
    }

    public static class Room {
        private int Id;
        private String name;
        private boolean isProtected;
        private short activePlayers;

        public void setId(int id) {
            Id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setProtected(boolean aProtected) {
            isProtected = aProtected;
        }

        public void setActivePlayers(short activePlayers) {
            this.activePlayers = activePlayers;
        }

        public void setRoomCapacity(short roomCapacity) {
            this.roomCapacity = roomCapacity;
        }

        public void setNumberOfEntrances(int numberOfEntrances) {
            this.numberOfEntrances = numberOfEntrances;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public void setReadOlny(boolean readOlny) {
            isReadOlny = readOlny;
        }

        private short roomCapacity;
        private int numberOfEntrances;
        private int likes;
        private boolean isReadOlny;

        public Room(int id, String name, boolean isProtected, short activePlayers,
                    short roomCapacity, int numberOfEntrances, int likes, boolean isReadOlny) {
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

        public boolean isProtected() {
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

        public boolean isReadOlny() {
            return isReadOlny;
        }
    }
}
