package com.mikolka9144.WoCserver.model;

import com.mikolka9144.WoCserver.model.Interceptors.ClientInterceptorFunc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class ServerConfig {
    private String targetServer;
    private int targetServerSocketPort;
    private int targetServerHttpPort;
    private int HostingSocketPort;
    private int HostingHttpPort;
    private ClientInterceptorFunc reqInterceptors;
    private HttpInterceptor httpDownloadInterceptors;

}
