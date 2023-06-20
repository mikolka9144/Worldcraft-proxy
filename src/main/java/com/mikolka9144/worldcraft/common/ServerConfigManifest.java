package com.mikolka9144.worldcraft.common;

import lombok.Getter;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class ServerConfigManifest {
    private final String targetServer;
    private final int targetHttpPort;
    private final int targetSocketPort;
    private final int socketPort;
    private final List<String> socketInterBeans;
    private final List<String> httpInterDownBeans;
    private final List<String> httpInterUpBeans;
    private final String socketServer;

    public ServerConfigManifest(Environment env){
        targetServer = env.getRequiredProperty("server.target.address"); // 64.237.54.60
        targetHttpPort = Integer.parseInt(env.getRequiredProperty("server.target.port"));
        targetSocketPort = Integer.parseInt(env.getRequiredProperty("server.target.socketPort"));
        // socketPort = "server.port"
        socketPort = Integer.parseInt(env.getRequiredProperty("server.socketPort"));

        socketServer = env.getRequiredProperty("server.interceptors.server");

        socketInterBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.socket").split(";")).toList();
        httpInterDownBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.http.download").split(";")).toList();
        httpInterUpBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.http.upload").split(";")).toList();
    }
}
