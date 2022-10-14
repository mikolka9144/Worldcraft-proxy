package com.mikolka9144.Worldcraft.PacketParsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketDataBuilder  {
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    public PacketDataBuilder append(String value){
        try {
            append((short) value.length());
            out.write(value.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    public PacketDataBuilder append(short value) {
        try {
            out.write(ByteBuffer.allocate(2).putShort(value).array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    public PacketDataBuilder append(int value) {
        try {
            out.write(ByteBuffer.allocate(4).putInt(value).array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    public PacketDataBuilder append(byte value) {
        out.write(value);
        return this;
    }
    public PacketDataBuilder append(byte[] value){
        try {
            out.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
    public PacketDataBuilder append(boolean value){
        if (value){
            append((byte)1);
        }
        else{
            append((byte)0);
        }
        return this;
    }
    public byte[] build(){
        return out.toByteArray();
    }
}
