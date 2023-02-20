package com.mikolka9144.worldcraft.socket.model;

import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
@Setter
public class ServerConfig {
    private int HostingSocketPort;
    private int HostingHttpPort;
    private ClientInterceptorFunc reqInterceptors;
    private List<HttpDownloadInterceptor> httpDownloadInterceptors;
    private List<HttpUploadInterceptor> httpUploadInterceptors;
    private Function<WorldCraftPacketIO, PacketServer> packetServer;
}
