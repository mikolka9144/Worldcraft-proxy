package com.mikolka9144.worldcraft.common.api.packet;

import com.mikolka9144.worldcraft.common.api.packet.enums.PacketCommand;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packet {
    private PacketProtocol protoId;
    private int playerId;
    private PacketCommand command;
    private String msg;
    private byte errorCode;
    private byte[] data;
}
