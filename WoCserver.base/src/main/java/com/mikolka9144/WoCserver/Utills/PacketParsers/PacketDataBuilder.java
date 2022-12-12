package com.mikolka9144.WoCserver.Utills.PacketParsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketDataBuilder  {
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    public PacketDataBuilder append(String value){
        try {
            byte[] valueAsBytes = value.getBytes();
            append((short) valueAsBytes.length);
            out.write(valueAsBytes);
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
    public PacketDataBuilder append(float value){
        append(Float.floatToIntBits(value));
        return this;
    }
    public PacketDataBuilder append(Vector3 value){
        append(value.getX());
        append(value.getY());
        append(value.getZ());
        return this;
    }
    public byte[] build(){
        return out.toByteArray();
    }
}
