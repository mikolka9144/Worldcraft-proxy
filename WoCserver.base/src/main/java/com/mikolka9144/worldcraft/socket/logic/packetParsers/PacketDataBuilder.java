package com.mikolka9144.worldcraft.socket.logic.packetParsers;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketDataBuilder {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @SneakyThrows
    public PacketDataBuilder append(String value) {
        byte[] valueAsBytes = value.getBytes();
        append((short) valueAsBytes.length);
        out.write(valueAsBytes);
        return this;
    }

    @SneakyThrows
    public PacketDataBuilder append(short value) {
        out.write(ByteBuffer.allocate(2).putShort(value).array());
        return this;
    }

    @SneakyThrows
    public PacketDataBuilder append(int value) {

        out.write(ByteBuffer.allocate(4).putInt(value).array());
        return this;
    }

    public PacketDataBuilder append(byte value) {
        out.write(value);
        return this;
    }

    @SneakyThrows
    public PacketDataBuilder append(byte[] value) {
        out.write(value);
        return this;
    }

    public PacketDataBuilder append(boolean value) {
        if (value) {
            append((byte) 1);
        } else {
            append((byte) 0);
        }
        return this;
    }

    public PacketDataBuilder append(float value) {
        append(Float.floatToIntBits(value));
        return this;
    }

    public PacketDataBuilder append(Vector3 value) {
        append(value.getX());
        append(value.getY());
        append(value.getZ());
        return this;
    }

    public byte[] build() {
        return out.toByteArray();
    }
}
