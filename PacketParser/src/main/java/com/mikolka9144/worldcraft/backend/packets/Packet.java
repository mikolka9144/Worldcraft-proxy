package com.mikolka9144.worldcraft.backend.packets;

import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link Packet} is the smallest unit in socket communication.
 * It represents single packet between client-server.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Packet {
    /**
     * ID of the protocol version used by the sender.
     */
    private int protocolByteCode;
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

    public Packet(PacketProtocol packetProtocol, int playerId, PacketCommand command, String msg, byte error, byte[] data) {
        this(packetProtocol.getProto(), playerId,command,msg,error,data);
    }

    /**
     * Gets game version of the sender or {@code SERVER}
     */
    public PacketProtocol getProtoId(){
        return PacketProtocol.findPacketProtoById(protocolByteCode);
    }
    /**
     * Gets game version of the sender or {@code SERVER}
     */
    public void setProtoId(PacketProtocol protoId){
        protocolByteCode = protoId.getProto();
    }
}
