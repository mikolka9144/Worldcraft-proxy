package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.common.ServerConfigManifest;
import com.mikolka9144.worldcraft.socket.SocketServer;
import com.mikolka9144.worldcraft.socket.model.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        var context = app.run(args);
        try {
            var server = context.getBean(SocketServer.class);
            var config = context.getBean(ServerConfig.class);

            log.info(String.format("Creating Server:  socketHost %d", config.getHostingSocketPort()));
            server.start();
        } catch (Exception e) {
            log.error("Server crashed!!!");
            log.error("Server crashed!!!", e);
        }
    }

    @Bean
    public ServerConfig configure(ConfigurationBuilder builder, ServerConfigManifest manifest) {
        return builder.configure(manifest);
    }
}
