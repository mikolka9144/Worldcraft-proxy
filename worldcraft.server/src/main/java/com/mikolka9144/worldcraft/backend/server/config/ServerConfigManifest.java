package com.mikolka9144.worldcraft.backend.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertyResolver;

import java.util.Arrays;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
public class ServerConfigManifest {
    private final int socketPort;
    private final List<String> socketInterBeans;
    private final List<String> httpInterDownBeans;
    private final List<String> httpInterUpBeans;
    @Getter(value = PRIVATE)
    private final PropertyResolver env;

    public String obtainOption(String key){
        if (env.containsProperty(key)){
            return env.getProperty(key);
        }
        else throw new IllegalArgumentException("Option "+key+" not found. Did you added it to configuration correctly?");
    }
    @Autowired
    public ServerConfigManifest(PropertyResolver env) {
        this.env = env;
        // socketPort = "server.port"
        socketPort = Integer.parseInt(obtainOption("server.socketPort"));

        socketInterBeans = Arrays.stream(obtainOption("server.interceptors.socket").split(";")).toList();
        httpInterDownBeans = Arrays.stream(obtainOption("server.interceptors.http.download").split(";")).toList();
        httpInterUpBeans = Arrays.stream(obtainOption("server.interceptors.http.upload").split(";")).toList();
    }
}
