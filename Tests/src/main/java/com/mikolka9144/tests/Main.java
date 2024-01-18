package com.mikolka9144.tests;

import com.mikolka9144.worldcraft.backend.server.config.WorldcraftServerBootstrap;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Import(WorldcraftServerBootstrap.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.run(args);
    }

    @Bean
    public static ServerConfigManifest createSource(Environment env) {
        return new ServerConfigManifest(env);
    }
}
