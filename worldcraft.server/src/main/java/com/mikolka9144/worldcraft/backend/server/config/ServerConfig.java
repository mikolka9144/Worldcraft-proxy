package com.mikolka9144.worldcraft.backend.server.config;

import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ServerConfig {
    private int hostingSocketPort;
    private Supplier<List<PacketAlteringModule>> reqInterceptors; //int port,  interceptors, Supplier<PacketServer> socketServersProvider
    private List<HttpDownloadInterceptor> httpDownloadInterceptors;
    private List<HttpUploadInterceptor> httpUploadInterceptors;
    private boolean isConfigured;
}
