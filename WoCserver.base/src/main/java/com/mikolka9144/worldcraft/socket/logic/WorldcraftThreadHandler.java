package com.mikolka9144.worldcraft.socket.logic;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.model.PacketAlreadyInterceptedException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.of;

@Slf4j
public abstract class WorldcraftThreadHandler {
    protected void attachToThread(WorldcraftSocket client, List<PacketInterceptor> socketInter, Closeable server) {
        new Thread(() -> worldcraftClientHandler(client, socketInter, server)).start();
    }

    @SneakyThrows
    private void worldcraftClientHandler(WorldcraftSocket socket, List<PacketInterceptor> socketInter, Closeable server) {
        try {
            while (true) { //TODO
                Packet packet = socket.getChannel().recive();
                PacketsFormula baseFormula = new PacketsFormula();
                baseFormula.getServerPackets().add(packet);

                for (PacketInterceptor interceptor : socketInter) {
                    PacketsFormula previousFormulas = baseFormula;
                    baseFormula = new PacketsFormula();

                    for (Packet item : previousFormulas.getServerPackets()){
                        Optional<PacketsFormula> resultingFormula = Optional.of((interceptor.InterceptRawPacket(item)));
                        resultingFormula.ifPresentOrElse(s -> baseFormula.add(s),() -> baseFormula.getServerPackets().c.add(packet));
                    }
                }
            }
        } catch (SocketException x) {
            log.warn(socket.getConnectedIp() + " closed connection");
            log.debug(x.getMessage());
        } catch (BufferUnderflowException ignore) {
        } catch (IOException x) {
            log.error(x.getMessage());
        } finally {
            // if onClose throws an Exception, that will be his problem
            server.close();
        }
    }
}
