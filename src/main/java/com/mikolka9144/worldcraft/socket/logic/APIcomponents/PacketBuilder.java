package com.mikolka9144.worldcraft.socket.logic.APIcomponents;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ServerBlockData;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import com.mikolka9144.worldcraft.socket.model.VersionFlags;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class contains methods for building packets.
 */
public class PacketBuilder {
    @Getter
    private final PacketProtocol clientProto;
    @Getter
    private final VersionFlags flags;
    @Getter
    @Setter
    private int playerId;

    public PacketBuilder(PacketProtocol clientProto) {
        flags = new VersionFlags(clientProto);
        this.clientProto = clientProto;
    }
    public Packet serverPacket(PacketCommand command,byte[] data,byte error){
        return new Packet(
                PacketProtocol.SERVER,
                0,
                command,
                "",
                error,
                data
        );
    }
    public Packet serverPacket(PacketCommand command,byte[] data){
        return serverPacket(command,data,(byte)0);
    }
    public Packet clientPacket(PacketCommand command,byte[] data){
        return new Packet(
                clientProto,
                playerId,
                command,
                "",
                (byte)0,
                data
        );
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
    public Packet
    setBlockServerPacket(BlockData data){
        return new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(data));


    }

    public Packet sendBlockClientPacket(int x, int y, int z, BlockData.BlockType blockType, int blockData, int blockTypePrev, int blockDataPrev) {
        return sendBlockClientPacket(new BlockData(
                        new Vector3Short((short) x, (short) y, (short) z),
                         blockType,
                        (byte) blockData,
                        (byte) blockTypePrev,
                        (byte) blockDataPrev
                ));


    }

    public Packet writeln(String line) {
        return new Packet(clientProto,playerId,
                PacketCommand.C_CHAT_MSG,"", (byte) 0, PacketContentSerializer.encodePlayerMessage(line));
    }

    public Packet sendBlockClientPacket(BlockData data) {
        return new Packet(clientProto, playerId,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0,
                PacketContentSerializer.encodePlaceBlockReq(data));
    }

    public List<Packet> createBlockComp(List<BlockData> blocks,int blocksPerPacket) {
        List<ServerBlockData> blockData = new ArrayList<>();
        var chunks = prepareChunks(blocks,blocksPerPacket).stream().toList();

        for (int i = 0; i < chunks.size(); i++) {
            blockData.add(new ServerBlockData(i, chunks.size(), chunks.get(i)));
        }

        return blockData.stream()
                .map(PacketContentSerializer::encodeServerBlocks)
                .map(s -> serverPacket(PacketCommand.S_MODIFIED_BLOCKS,s))
                .toList();

    }
    public static <T> Collection<List<T>> prepareChunks(List<T> inputList, int chunkSize) {
        AtomicInteger counter = new AtomicInteger();
        return inputList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
    }
}
