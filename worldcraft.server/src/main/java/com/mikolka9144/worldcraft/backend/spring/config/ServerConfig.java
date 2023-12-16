package com.mikolka9144.worldcraft.backend.spring.config;

import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.base.interceptor.PacketAlteringModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Supplier;

@Getter
@AllArgsConstructor
@Setter
public class ServerConfig {
    private int hostingSocketPort;
    private Supplier<List<PacketAlteringModule>> reqInterceptors; //int port,  interceptors, Supplier<PacketServer> socketServersProvider
    private List<HttpDownloadInterceptor> httpDownloadInterceptors;
    private List<HttpUploadInterceptor> httpUploadInterceptors;
}
