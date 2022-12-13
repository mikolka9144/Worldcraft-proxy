package com.mikolka9144.WoCserver.model.Interceptors;

import com.mikolka9144.WoCserver.model.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

import java.util.List;
import java.util.function.BiFunction;
@FunctionalInterface
public interface ClientInterceptorFunc extends BiFunction<WorldCraftPacketIO, PacketServer, List<PacketInterceptor>> {
}
