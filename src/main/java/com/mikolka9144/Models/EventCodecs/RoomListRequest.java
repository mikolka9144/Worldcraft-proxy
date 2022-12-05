package com.mikolka9144.Models.EventCodecs;

public class RoomListRequest {
    public RoomsType getRoomsType() {
        return roomsType;
    }

    public RoomListRequest(RoomsType roomsType, int startingIndex) {
        this.roomsType = roomsType;
        this.startingIndex = startingIndex;
    }

    public void setRoomsType(RoomsType roomsType) {
        this.roomsType = roomsType;
    }

    public int getStartingIndex() {
        return startingIndex;
    }

    public void setStartingIndex(int startingIndex) {
        this.startingIndex = startingIndex;
    }

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
