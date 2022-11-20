package com.mikolka9144.Models.Interceptors;

import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Models.Packet.PacketServer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.util.List;
import java.util.function.BiFunction;
@FunctionalInterface
public interface ClientInterceptorFunc extends BiFunction<WorldCraftPacketIO, PacketServer, List<PacketInterceptor>> {
}
