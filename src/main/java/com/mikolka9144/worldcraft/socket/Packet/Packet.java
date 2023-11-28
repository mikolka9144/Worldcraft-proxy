package com.mikolka9144.worldcraft.socket.Packet;

import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packet {
    private PacketProtocol ProtoId;
    private int PlayerId;
    private PacketCommand Command;
    private String Message;
    private byte Error;
    private byte[] Data;
}
