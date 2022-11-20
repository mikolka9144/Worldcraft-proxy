package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.BlockData;
import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketCommand;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Utills.PacketAlreadyInterceptedException;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;
import java.util.Random;

public class ChatCommandsInterceptor extends FullPacketInterceptor {
    private PacketInterceptor serverInterceptor;
    private Boolean isChatEnabled = true;
    public ChatCommandsInterceptor(WorldCraftPacketIO connectionIO, PacketInterceptor serverInterceptor) {
        super(connectionIO);
        this.serverInterceptor = serverInterceptor;
    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message) {
        if (!message.contains("/")) {
            if(isChatEnabled) return;
            else {
                println("CHAT IS DISABLED",packet.getProtoId());
                println("You can enable it with '/chat on'",packet.getProtoId());
                throw new PacketAlreadyInterceptedException();
            }
        }
        new Thread(() -> {
            String command = message.split("/", 2)[1];
            if (command.equals("moto")) {
                try {
                    println("In the notepad", packet.getProtoId());
                    Thread.sleep(500);
                    println("fates are written", packet.getProtoId());
                    Thread.sleep(500);
                    println("cus Pandora didn't listen", packet.getProtoId());
                    Thread.sleep(1000);
                    println( "Time will march", packet.getProtoId());
                    Thread.sleep(500);
                    println("and here with me", packet.getProtoId());
                    Thread.sleep(1800);
                    println("THIS SCREEN IS LAST YOU WILL EVER SEE", packet.getProtoId());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } else if (command.equals("chat off")) {
                isChatEnabled = false;
                println("Chat was disabled",packet.getProtoId());
            }
            else if (command.equals("chat on")) {
                isChatEnabled = true;
                println("Chat was enabled",packet.getProtoId());
            } else {
                println("Unknown command: "+command,packet.getProtoId());
            }
        }).start();
        throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data) {
        if (!isChatEnabled) throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data) {
        try {
            if (data.getBlockType() != 0) modifyBlock(packet, data, new Random().nextInt(100), 5);
            String message = String.format("Modify block:%n" +
                            "at %d %d %d (Chunk %d,%d)\n" +
                            "block %d:%d -> %d:%d",
                    data.getX(), data.getY(), data.getZ(), data.getChunkX(), data.getChunkZ(),
                    data.getPrevBlockType(), data.getPrevBlockData(), data.getBlockType(), data.getBlockData());
            for (String line : message.split("\n")) {
                println(line, packet.getProtoId());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void modifyBlock(Packet packet, BlockData data, int type, int blockData) throws IOException {
        data.setBlockType((byte) type);
        data.setBlockData((byte) blockData);
        Packet modifyBlock = new Packet(PacketProtocol.SERVER, packet.getPlayerId(),
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(data));
        try {
            connectionIO.send(modifyBlock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        packet.setData(PacketContentSerializer.encodePlaceBlockReq(data));
    }
//        @Override
//    public void interceptPlayerPositionReq(Packet packet, MovementPacket data) {
//        String message = String.format("You are at: X:%d Y:%d Z:%d",
//                Math.round(data.getPosition().getX()),
//                Math.round(data.getPosition().getY()),
//                        Math.round(data.getPosition().getZ()));
//        try {
//            println(message,packet.getProtoId()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private void println(String text, PacketProtocol proto) {
        try {
            connectionIO.send(new Packet(PacketProtocol.SERVER, 0, PacketCommand.SB_CHAT_MSG, "", (byte) 0,
                    PacketContentSerializer.encodeChatMessage(new ChatMessage(text, text, ChatMessage.MsgType.PLAYER_JOINED), proto)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {

    }
}
