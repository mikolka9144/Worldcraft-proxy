package com.mikolka9144.WoCserver.model.Packet;

import com.mikolka9144.WoCserver.model.PacketProtocol;

public class Packet {
    private PacketProtocol ProtoId;
    private int PlayerId;
    private PacketCommand Command;
    private String Message;
    private byte Error;

    private byte[] Data;

    public Packet(PacketProtocol protoId, int playerId, PacketCommand command, String message, byte error, byte[] data) {
        ProtoId = protoId;
        PlayerId = playerId;
        Command = command;
        Message = message;
        Error = error;
        Data = data;
    }

    public void setProtoId(PacketProtocol protoId) {
        ProtoId = protoId;
    }

    public void setPlayerId(int playerId) {
        PlayerId = playerId;
    }

    public void setCommand(PacketCommand command) {
        Command = command;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setError(byte error) {
        Error = error;
    }

    public void setData(byte[] data) {
        Data = data;
    }

    public PacketProtocol getProtoId() {
        return ProtoId;
    }

    public int getPlayerId() {
        return PlayerId;
    }

    public PacketCommand getCommand() {
        return Command;
    }

    public String getMessage() {
        return Message;
    }

    public byte getError() {
        return Error;
    }

    public byte[] getData() {
        return Data;
    }
}
