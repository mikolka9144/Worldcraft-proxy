package com.mikolka9144.worldcraft.socket.model;

public class PacketAlreadyInterceptedException extends RuntimeException{
    public PacketAlreadyInterceptedException(){
        super("Packet was intercepted to the fullest extent. " +
                "Further interception could lead to unexpected behaviour");
    }
}