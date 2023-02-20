package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.logic.PacketParsers.ContentParsers.PacketContentSerializer;

import java.io.IOException;

public class Packeter {
    private final WorldCraftPacketIO client;
    private final PacketInterceptor server;
    private final PacketProtocol clientProto;

    public Packeter(WorldCraftPacketIO client, PacketInterceptor server,PacketProtocol clientProto) {
        this.client = client;
        this.server = server;
        this.clientProto = clientProto;
    }

    public void println(String text) {
        if(clientProto.isWorldOfCraft()){
            text = "[D7EF00]>"+text;
        }
        var chatMsg = new ChatMessage(text, text, ChatMessage.MsgType.STANDARD);
        try {
            client.send(new Packet(PacketProtocol.SERVER, 0, PacketCommand.SB_CHAT_MSG, "", (byte) 0,
                    PacketContentSerializer.encodeChatMessage(chatMsg)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setBlockServerPacket(int x,int y,int z,int blockType,int blockData) throws IOException {
        client.send( new Packet(PacketProtocol.SERVER, 0,
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(new BlockData(
                        (short) (x%16),
                        (short) y,
                        (short) (z%16),
                        (short) (x/16),
                        (short) (z/16),
                        (byte) blockType,
                        (byte) blockData
                ))));


    }
    public void sendBlockClientPacket(int x, int y, int z, int blockType, int blockData, int blockTypePrev, int blockDataPrev) throws IOException {
        client.send( new Packet(clientProto, 0,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(new BlockData(
                        (short) (x%16),
                        (short) y,
                        (short) (z%16),
                        (short) (x/16),
                        (short) (z/16),
                        (byte) blockType,
                        (byte) blockData,
                        (byte) blockTypePrev,
                        (byte) blockDataPrev
                ))));


    }
    public void setBlock(int x,int y,int z,int blockData,int blockType,int prevBlockType,int prevBlockData) throws IOException {
        sendBlockClientPacket(x,y,z,blockType,blockData,prevBlockType,prevBlockData);
        setBlockServerPacket(x,y,z,blockType,blockData);
    }
}
