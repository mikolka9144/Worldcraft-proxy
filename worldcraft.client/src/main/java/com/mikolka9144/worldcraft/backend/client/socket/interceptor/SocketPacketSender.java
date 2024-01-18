package com.mikolka9144.worldcraft.backend.client.socket.interceptor;

import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftThread;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

/**
 * Class used to communicate with client and server alike.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SocketPacketSender {
    private final WorldcraftThread thread;
    private final Closeable dispose;

    public static void configureWoCThread(WorldcraftThread thread, Closeable dispose) {
        SocketPacketSender sender = new SocketPacketSender(thread, dispose);
        thread.getInterceptors().forEach(s ->  s.setupSockets(sender));
    }

    /**
     * Sends packet to connected client
     *
     * @param packet packet to send
     */
    public void sendToClient(Packet packet) {
        try {
            thread.sendServerPacket(packet);
        } catch (Exception x) {
            log.error("Exception was thrown when attempting to send packet by interceptor to client:", x);
        }
    }

    /**
     * Sends packet to server as connected client
     *
     * @param packet packet to send
     */
    public void sendToServer(Packet packet) {
        try {
            thread.sendClientPacket(packet);
        } catch (Exception x) {
            log.error("Exception was thrown when attempting to send packet by interceptor to server:", x);
        }
    }

    /**
     * Terminates connection with client
     */
    @SneakyThrows
    public void closeConnection() {
        dispose.close();
    }
}
