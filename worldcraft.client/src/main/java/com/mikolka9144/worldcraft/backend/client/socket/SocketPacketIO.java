package com.mikolka9144.worldcraft.backend.client.socket;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;
import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Low-level class used to receiving and sending Packets.
 */
@RequiredArgsConstructor
public class SocketPacketIO implements Closeable{
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Closeable closeable;
    public SocketPacketIO(Socket connection) throws WorldcraftCommunicationException {
        try {
            this.inputStream = connection.getInputStream();
            this.outputStream = connection.getOutputStream();
            this.closeable = connection;
        }
        catch (IOException x){
            throw new WorldcraftCommunicationException("General communication error occurred while connecting!",x);
        }

    }

    public Packet receive() throws IOException {
        // Loading Early metadata
        PacketDataReader headerReader = new PacketDataReader(inputStream.readNBytes(8));

        // Reading early header
        int protocol = headerReader.getInt();
        int packetSize = headerReader.getInt();

        // Loading packet
        PacketDataReader packetRaw = new PacketDataReader(inputStream.readNBytes(packetSize));

        // Reading packet
        PacketCommand command = PacketCommand.findPacketCommandById(packetRaw.getByte());
        byte errorCode = packetRaw.getByte();
        int playerId = packetRaw.getInt();
        String packetMessage = packetRaw.getString();
        /*int dataSize =*/
        packetRaw.getInt();
        byte[] data = packetRaw.getBytes();
        // Craft serialized packet
        return new Packet(protocol, playerId, command, packetMessage, errorCode, data);
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
                .append(packet.getProtocolByteCode())
                .append(rawPacket.length)
                .append(rawPacket)
                .build();

        // Send packet
        outputStream.write(tcpBlob);
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        closeable.close();
    }
}
