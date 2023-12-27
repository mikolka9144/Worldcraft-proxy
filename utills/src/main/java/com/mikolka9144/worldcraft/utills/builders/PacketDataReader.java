package com.mikolka9144.worldcraft.utills.builders;


import com.mikolka9144.worldcraft.utills.ParsingException;
import com.mikolka9144.worldcraft.utills.Vector3;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PacketDataReader {
    private final ByteArrayInputStream data;

    public PacketDataReader(byte[] data) {
        this.data = new ByteArrayInputStream(data);
    }

    public String getString() {
        try {
            int strSize = getShort();
            byte[] strAsBytes = data.readNBytes(strSize);
            return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(strAsBytes)).toString();
        } catch (IOException e) {
            throw new ParsingException("Reading string from packet bytes failed.", e);
        }
    }

    public String readAsText() {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(data.readAllBytes())).toString();
    }

    @SneakyThrows
    public int getInt() {
        return ByteBuffer.wrap(this.data.readNBytes(Integer.BYTES)).getInt();
    }

    @SneakyThrows
    public int getLong() {
        return ByteBuffer.wrap(this.data.readNBytes(Long.BYTES)).getInt();
    }

    @SneakyThrows
    public short getShort() {
        return ByteBuffer.wrap(this.data.readNBytes(Short.BYTES)).getShort();
    }

    public byte getByte() {
        return (byte) data.read();
    }

    public byte[] getBytes() {
        return data.readAllBytes();
    }

    public boolean getBoolean() {
        return getByte() == 1;
    }

    public boolean hasNext(int bytes) {
        return data.available() >= bytes;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public Vector3 getVector3() {
        return new Vector3(getFloat(), getFloat(), getFloat());
    }
}
