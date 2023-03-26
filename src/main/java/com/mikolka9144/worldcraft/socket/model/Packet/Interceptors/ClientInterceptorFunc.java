package com.mikolka9144.worldcraft.socket.model.Packet.Interceptors;

import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;

import java.util.List;
import java.util.function.BiFunction;
@FunctionalInterface
public interface ClientInterceptorFunc extends BiFunction<WorldCraftPacketIO, PacketServer, List<PacketInterceptor>> {
}
