package com.mikolka9144.WoCserver.Models.Interceptors;

import com.mikolka9144.WoCserver.Models.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.Models.Packet.PacketServer;
import com.mikolka9144.WoCserver.ServerComponents.socket.WorldCraftPacketIO;

import java.util.List;
import java.util.function.BiFunction;
@FunctionalInterface
public interface ClientInterceptorFunc extends BiFunction<WorldCraftPacketIO, PacketServer, List<PacketInterceptor>> {
}
