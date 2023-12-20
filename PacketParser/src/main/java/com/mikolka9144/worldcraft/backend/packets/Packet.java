package com.mikolka9144.worldcraft.backend.packets;

import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *{@link Packet} is the smallest unit in socket communication.
 * It represents single packet between client-server.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packet {
    /**
     * Game version of the sender or {@code SERVER}
     */
    private PacketProtocol protoId;
    /**
     * ID of the sender.
     * <h4>Not present in:<h4/>
     * <ul>
     *   <li>Server packets</li>
     *   <li>General packets (not affecting player)</li>
     * </ul>
     */
    private int playerId;
    /**
     * Action (req. or resp.) of the packet
     */
    private PacketCommand command;
    /**
     * Additional tag for packet (because yes)
     */
    private String msg;
    /**
     * Error code of the packet or 0 for "No error".
     */
    private byte errorCode;
    /**
     * Raw content of the packet.
     */
    private byte[] data;
}
