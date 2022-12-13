package com.mikolka9144.WoCserver.model.EventCodecs;

import com.mikolka9144.WoCserver.utills.PacketParsers.Vector3;

public class MovementPacket {
    private int playerId;

    public MovementPacket(int playerId, Vector3 position, Vector3 orientation, Vector3 up) {
        this.playerId = playerId;
        this.position = position;
        this.orientation = orientation;
        this.up = up;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getOrientation() {
        return orientation;
    }

    public void setOrientation(Vector3 orientation) {
        this.orientation = orientation;
    }

    public Vector3 getUp() {
        return up;
    }

    public void setUp(Vector3 up) {
        this.up = up;
    }

    private Vector3 position;
    private Vector3 orientation;
    private Vector3 up;
}
