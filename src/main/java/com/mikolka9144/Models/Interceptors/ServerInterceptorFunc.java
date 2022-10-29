package com.mikolka9144.Models.Interceptors;

import com.mikolka9144.Models.Packet.PacketInterceptor;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.util.List;
import java.util.function.Function;
@FunctionalInterface
public interface ServerInterceptorFunc extends Function<WorldCraftPacketIO, List<PacketInterceptor>> {
}
