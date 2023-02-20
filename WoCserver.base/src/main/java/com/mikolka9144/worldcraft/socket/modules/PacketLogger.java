package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;

import java.util.Arrays;

public class PacketLogger extends PacketInterceptor {
    public PacketLogger(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        System.out.println(Arrays.toString(packet.getData()));
        System.out.println("Proto: "+packet.getProtoId());
        System.out.println("Error code: "+packet.getError());
        System.out.println("Command: "+packet.getCommand());
        System.out.println("PlayerId: "+packet.getPlayerId());
        System.out.println("Msg: "+packet.getMessage());
        System.out.println("Data: "+ Arrays.toString(packet.getData()));
        System.out.println();
    }

}
