package com.mikolka9144.worldcraft.socket.api;

import com.mikolka9144.worldcraft.common.api.packet.enums.BlockType;
import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.common.api.packet.codecs.Block;
import com.mikolka9144.worldcraft.common.api.packet.codecs.ChatMessage;
import com.mikolka9144.worldcraft.common.api.packet.codecs.ServerBlockData;
import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketCommand;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketProtocol;
import com.mikolka9144.worldcraft.common.models.Vector3Short;
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
@Getter
public class PacketBuilder {
    private final PacketProtocol clientProto;
    @Setter
    private int playerId;

    public PacketBuilder(PacketProtocol clientProto) {
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
        var chatMsg = new ChatMessage("", text, ChatMessage.MsgType.STANDARD);

        return new Packet(PacketProtocol.SERVER, 0, PacketCommand.SB_CHAT_MSG, "", (byte) 0,
                PacketDataEncoder.chatMessage(chatMsg));

    }

    public Packet
    setBlockServerPacket(int x, int y, int z, BlockType blockType, int blockData){
        return new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketDataEncoder.serverPlaceBlock(new Block(
                        new Vector3Short((short) x, (short) y, (short) z),
                         blockType,
                        (byte) blockData
                )));


    }
    public Packet
    setBlockServerPacket(Block data){
        return new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketDataEncoder.serverPlaceBlock(data));


    }

    public Packet sendBlockClientPacket(int x, int y, int z, BlockType blockType, int blockData, int blockTypePrev, int blockDataPrev) {
        return sendBlockClientPacket(new Block(
                        new Vector3Short((short) x, (short) y, (short) z),
                         blockType,
                        (byte) blockData,
                        (byte) blockTypePrev,
                        (byte) blockDataPrev
                ));


    }

    public Packet writeln(String line) {
        return new Packet(clientProto,playerId,
                PacketCommand.C_CHAT_MSG,"", (byte) 0, PacketDataEncoder.playerMessage(line));
    }

    public Packet sendBlockClientPacket(Block data) {
        return new Packet(clientProto, playerId,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0,
                PacketDataEncoder.placeBlockReq(data));
    }

    public List<Packet> createBlockComp(List<Block> blocks, int blocksPerPacket) {
        List<ServerBlockData> blockData = new ArrayList<>();
        var chunks = prepareChunks(blocks,blocksPerPacket).stream().toList();

        for (int i = 0; i < chunks.size(); i++) {
            blockData.add(new ServerBlockData(i, chunks.size(), chunks.get(i)));
        }

        return blockData.stream()
                .map(PacketDataEncoder::serverBlocks)
                .map(s -> serverPacket(PacketCommand.S_MODIFIED_BLOCKS,s))
                .toList();

    }
    public static <T> Collection<List<T>> prepareChunks(List<T> inputList, int chunkSize) {
        AtomicInteger counter = new AtomicInteger();
        return inputList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
    }
}
