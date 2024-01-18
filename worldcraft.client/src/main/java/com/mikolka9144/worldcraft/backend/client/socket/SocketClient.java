package com.mikolka9144.worldcraft.backend.client.socket;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.util.function.Consumer;

/**
 * Implementation of basic worldcraft client allowing to communicate with target server.
 * If you want to connect to server, use {@link WorldcraftClient}
 */
@Slf4j
public class SocketClient implements Closeable {
    @Getter
    private final SocketPacketIO socket;
    private final String clientName;
    private final Consumer<Packet> packetCallback;
    private boolean isRunning = true;
    private final Thread receiver;

    /**
     * @param io              Connection (real or fake) to operate on
     * @param connectionName  Name (or IP) of connected target
     * @param onPacketReceive Callback to run for received packets from server
     */
    public SocketClient(SocketPacketIO io,String connectionName, Consumer<Packet> onPacketReceive) {
        socket = io;
        this.clientName = connectionName;
        packetCallback = onPacketReceive;
        receiver = new Thread(this::handleThread, connectionName);
    }
    public static SocketPacketIO getIoStream(String hostname, int port) throws WorldcraftCommunicationException, IOException {
        try {
            return new SocketPacketIO(new Socket(hostname, port));
        }
        catch (UnknownHostException x){
            throw new WorldcraftCommunicationException("Hostname "+hostname+" couldn't be resolved!",x);
        }
        catch (IllegalArgumentException x){
            throw new WorldcraftCommunicationException("Port "+port+" is not in socket port ranges",x);
        }
    }
    private void handleThread() {
        try {
            while (isRunning) {
                Packet initialPacket = socket.receive();
                packetCallback.accept(initialPacket);
            }
            log.info(String.format("Connection for %s was HALTED!", clientName));
        } catch (IOException x) {
            log.warn(clientName + " had IO Exception");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) { // Duplicate exception
            log.warn(clientName + " had Buffer underflow");
        } finally {
            // if onClose throws an Exception, that will be his problem
            close();
            log.warn(clientName + " closed!");
        }
    }


    public void start() {
        receiver.start();
    }

    public void send(Packet packet) throws IOException {
        socket.send(packet);
    }

    @Override
    @SneakyThrows
    public void close() {
        isRunning = false;
        socket.close();
    }
}
