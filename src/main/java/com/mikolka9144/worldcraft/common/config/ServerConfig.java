package com.mikolka9144.worldcraft.common.config;

import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketServer;
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
    private Supplier<List<PacketAlteringModule>> reqInterceptors; //int port,  interceptors, Supplier<PacketServer> socketServersProvider
    private List<HttpDownloadInterceptor> httpDownloadInterceptors;
    private List<HttpUploadInterceptor> httpUploadInterceptors;
    private Function<WorldcraftPacketIO, PacketServer> packetServer;
}
