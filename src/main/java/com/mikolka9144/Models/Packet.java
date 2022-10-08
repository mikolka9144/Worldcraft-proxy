package com.mikolka9144.Models;

public class Packet {
    private final PacketProtocol ProtoId;
    private final int PlayerId;
    private final PacketCommand Command;
    private final String Message;
    private final byte Error;
    private final byte[] Data;

    public Packet(PacketProtocol protoId, int playerId, PacketCommand command, String message, byte error, byte[] data) {
        ProtoId = protoId;
        PlayerId = playerId;
        Command = command;
        Message = message;
        Error = error;
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
