package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.ChatMessage;
import com.mikolka9144.Models.EventCodecs.MovementPacket;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Models.Packet.PacketCommand;
import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.PacketProtocol;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Utills.PacketAlreadyInterceptedException;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;

public class ChatCommandsInterceptor extends FullPacketInterceptor {
    private PacketInterceptor serverInterceptor;
    private boolean isChatEnabled = true;

    public ChatCommandsInterceptor(WorldCraftPacketIO connectionIO, PacketInterceptor serverInterceptor) {
        super(connectionIO);
        this.serverInterceptor = serverInterceptor;
    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message) {
            if(message.contains(":moto")){
                new Thread(() -> {
                    try {
                        connectionIO.send(println("In the notepad",packet.getProtoId()));
                        Thread.sleep(500);
                        connectionIO.send(println("fates are written",packet.getProtoId()));
                        Thread.sleep(500);
                        connectionIO.send(println("cus Pandora didn't listen",packet.getProtoId()));
                        Thread.sleep(1000);
                        connectionIO.send(println("Time will march",packet.getProtoId()));
                        Thread.sleep(500);
                        connectionIO.send(println("and here with me",packet.getProtoId()));
                        Thread.sleep(1800);
                        connectionIO.send(println("THIS SCREEN IS LAST YOU WILL EVER SEE",packet.getProtoId()));
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                throw new PacketAlreadyInterceptedException();
            } else if (message.contains(":chat off")) {
                isChatEnabled = false;
            }
            else if (message.contains(":chat on")) {
                isChatEnabled = true;
            }
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data) {
        if(!isChatEnabled) throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data) {
        String message = String.format("You are at: X:%d Y:%d Z:%d",
                Math.round(data.getPosition().getX()),
                Math.round(data.getPosition().getY()),
                        Math.round(data.getPosition().getZ()));
        try {
            connectionIO.send(println(message,packet.getProtoId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Packet println(String text, PacketProtocol proto){
        return new Packet(PacketProtocol.SERVER,0, PacketCommand.SB_CHAT_MSG,"", (byte) 0,
                PacketContentSerializer.encodeChatMessage(new ChatMessage("",text, ChatMessage.MsgType.STANDARD),proto));
    }
    @Override
    public void close() throws IOException {

    }
}
