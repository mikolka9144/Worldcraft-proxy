package com.mikolka9144.Utills.PacketParsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PacketDataReader {
    private final ByteArrayInputStream data;

    public PacketDataReader(byte[] data){
        this.data = new ByteArrayInputStream(data);
    }
    public String getString(){
        try {
            short strSize = getShort();
            byte[] strAsBytes =  data.readNBytes(strSize);
            String out = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(strAsBytes)).toString();
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int getInt() throws IOException {
            return ByteBuffer.wrap(this.data.readNBytes(Integer.BYTES)).getInt();
    }
    public short getShort() throws IOException {
            return ByteBuffer.wrap(this.data.readNBytes(Short.BYTES)).getShort();
    }
    public byte getByte(){
        return (byte) data.read();
    }
    public byte[] getBytes(){
        return data.readAllBytes();
    }
    public boolean getBoolean(){return getByte()==1;}
    public boolean hasNext(int bytes){
        return data.available()>=bytes;
    }

    public float getFloat() throws IOException {
        return Float.intBitsToFloat(getInt()); //TODO test this
    }
}
