package com.mikolka9144.worldcraft.socket.model.Packet;

import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Packet {
    private PacketProtocol ProtoId;
    private int PlayerId;
    private PacketCommand Command;
    private String Message;
    private byte Error;
    private byte[] Data;
}
