package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;

/**
 * This class contains methods for building packets.
 */
public class PacketBuilder {

    private final PacketProtocol clientProto;
    private final VersionFlags flags;
    private final int playerId;

    public PacketBuilder(PacketProtocol clientProto,int playerId) {
        flags = new VersionFlags(clientProto);
        this.clientProto = clientProto;
        this.playerId = playerId;
    }

    public Packet println(String text) {
        if (!flags.simplifyChat) {
            text = "[D7EF00]>" + text;
        }
        var chatMsg = new ChatMessage(text, text, ChatMessage.MsgType.STANDARD);

        return new Packet(PacketProtocol.SERVER, 0, PacketCommand.SB_CHAT_MSG, "", (byte) 0,
                PacketContentSerializer.encodeChatMessage(chatMsg));

    }

    public Packet
    setBlockServerPacket(int x, int y, int z, BlockData.BlockType blockType, int blockData){
        return new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(new BlockData(
                        new Vector3Short((short) x, (short) y, (short) z),
                         blockType,
                        (byte) blockData
                )));


    }

    public Packet sendBlockClientPacket(int x, int y, int z, BlockData.BlockType blockType, int blockData, int blockTypePrev, int blockDataPrev) {
        return new Packet(clientProto, playerId,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(new BlockData(
                        new Vector3Short((short) x, (short) y, (short) z),
                         blockType,
                        (byte) blockData,
                        (byte) blockTypePrev,
                        (byte) blockDataPrev
                )));


    }

    public Packet writeln(String line) {
        return new Packet(clientProto,playerId,
                PacketCommand.C_CHAT_MSG,"", (byte) 0, PacketContentSerializer.encodePlayerMessage(line));
    }
}
