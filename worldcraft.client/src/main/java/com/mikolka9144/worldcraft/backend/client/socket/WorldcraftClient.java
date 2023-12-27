package com.mikolka9144.worldcraft.backend.client.socket;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.nio.BufferUnderflowException;
import java.util.function.Consumer;

/**
 * Implementation of basic worldcraft client allowing to communicate with target server.
 */
@Slf4j
public class WorldcraftClient implements Closeable {
    @Getter
    private final WorldcraftSocket socket;
    private final Consumer<Packet> packetCallback;
    private boolean isRunning = true;
    private final Thread receiver;

    /**
     * @param hostname        Name (or IP) of target server
     * @param port            A port to hit on target {@code hostname}
     * @param onPacketReceive Callback to run for received packets from server
     * @throws IOException Thrown if client fails to open connection to target server
     */
    public WorldcraftClient(String hostname, int port, Consumer<Packet> onPacketReceive) throws IOException {
        this(new WorldcraftSocket(new Socket(hostname, port)), onPacketReceive);

    }

    /**
     * @param io              Connection (real or fake) to operate on
     * @param onPacketReceive Callback to run for received packets from server
     */
    public WorldcraftClient(WorldcraftSocket io, Consumer<Packet> onPacketReceive) {
        socket = io;
        packetCallback = onPacketReceive;
        receiver = new Thread(this::handleThread, io.getConnectedIp());
    }

    private void handleThread() {
        try {
            while (isRunning) {
                Packet initialPacket = socket.getChannel().receive();
                packetCallback.accept(initialPacket);
            }
            log.info(String.format("Connection for %s was HALTED!", socket.getConnectedIp()));
        } catch (IOException x) {
            log.warn(socket.getConnectedIp() + " had IO Exception");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) { // Duplicate exception
            log.warn(socket.getConnectedIp() + " had Buffer underflow");
        } finally {
            // if onClose throws an Exception, that will be his problem
            close();
            log.warn(socket.getConnectedIp() + " closed!");
        }
    }

    /**
     * Starts receiving packets from server. As simple as that.
     */
    public void start() {
        receiver.start();
    }

    /**
     * Sends packet to Server
     *
     * @param packet A packet to send
     * @throws IOException Sending packet to server failed
     */
    public void send(Packet packet) throws IOException {
        socket.getChannel().send(packet);
    }

    @Override
    @SneakyThrows
    public void close() {
        isRunning = false;
        socket.close();
    }
}
