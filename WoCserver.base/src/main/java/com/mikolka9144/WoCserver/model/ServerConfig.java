package com.mikolka9144.WoCserver.model;

import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
@Setter
public class ServerConfig {
    private int HostingSocketPort;
    private int HostingHttpPort;
    private ClientInterceptorFunc reqInterceptors;
    private List<HttpInterceptor> httpDownloadInterceptors;
    private Function<WorldCraftPacketIO,PacketServer> packetServer;
}
