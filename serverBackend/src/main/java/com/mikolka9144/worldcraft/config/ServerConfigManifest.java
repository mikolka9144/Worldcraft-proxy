package com.mikolka9144.worldcraft.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class ServerConfigManifest {
    private final String targetServer;
    private final int targetHttpPort;
    private final int targetSocketPort;
    private final int socketPort;
    private final List<String> socketInterBeans;
    private final List<String> httpInterDownBeans;
    private final List<String> httpInterUpBeans;
    @Autowired
    public ServerConfigManifest(Environment env){
        targetServer = env.getRequiredProperty("server.target.address"); // 64.237.54.60
        targetHttpPort = Integer.parseInt(env.getRequiredProperty("server.target.port"));
        targetSocketPort = Integer.parseInt(env.getRequiredProperty("server.target.socketPort"));
        // socketPort = "server.port"
        socketPort = Integer.parseInt(env.getRequiredProperty("server.socketPort"));

        socketInterBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.socket").split(";")).toList();
        httpInterDownBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.http.download").split(";")).toList();
        httpInterUpBeans = Arrays.stream(env.getRequiredProperty("server.interceptors.http.upload").split(";")).toList();
    }
}
