package com.mikolka9144.worldcraft.socket.server;


import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.build.PacketDataBuilder;
import com.mikolka9144.packet.packet.build.PacketDataReader;
import com.mikolka9144.packet.packet.enums.PacketCommand;
import com.mikolka9144.packet.packet.enums.PacketProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Low-level class used to receiving and sending Packets.
 */
public class WorldcraftPacketIO {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public WorldcraftPacketIO(InputStream in, OutputStream out){

        this.inputStream = in;
        this.outputStream = out;

    }
    public Packet receive() throws IOException {
        // Loading Early metadata
        PacketDataReader headerReader = new PacketDataReader(inputStream.readNBytes(8));

        // Reading early header
        PacketProtocol protocol = PacketProtocol.findPacketProtoById(headerReader.getInt());
        int packetSize = headerReader.getInt();

        // Loading packet
        PacketDataReader packetRaw = new PacketDataReader(inputStream.readNBytes(packetSize));

        // Reading packet
        PacketCommand command = PacketCommand.findPacketCommandById(packetRaw.getByte());
        byte errorCode = packetRaw.getByte();
        int playerId = packetRaw.getInt();
        String packetMessage = packetRaw.getString();
        /*int dataSize =*/ packetRaw.getInt();
        byte[] data = packetRaw.getBytes();
        // Craft serialized packet
        return new Packet(protocol,playerId,command,packetMessage,errorCode,data);
    }
    public void send(Packet packet) throws IOException {
        //Calculate data size
        int dataSize = packet.getData().length;

        //Create buffer for writing actual packet
        PacketDataBuilder out = new PacketDataBuilder();

        // Write packet
        out.append(packet.getCommand().getCommand())
            .append(packet.getErrorCode())
            .append(packet.getPlayerId())
            .append(packet.getMsg())
            .append(dataSize)
            .append(packet.getData());

        // Build packet
        PacketDataBuilder tcpPacket = new PacketDataBuilder();
        byte[] rawPacket = out.build();
        byte[] tcpBlob = tcpPacket
                .append(packet.getProtoId().getProto())
                .append(rawPacket.length)
                .append(rawPacket)
                .build();

        // Send packet
        outputStream.write(tcpBlob);
        outputStream.flush();
    }

}
