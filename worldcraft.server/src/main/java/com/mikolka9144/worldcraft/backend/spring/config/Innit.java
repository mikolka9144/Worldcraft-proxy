package com.mikolka9144.worldcraft.backend.spring.config;

import com.mikolka9144.worldcraft.backend.spring.socket.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Innit {
    public static void start(ApplicationContext context){
        try {
            var config = context.getBean(ServerConfig.class);
            var server = context.getBean(SocketServer.class);

            log.info(String.format("Creating Server:  socketHost %d", config.getHostingSocketPort()));
            server.start();
        } catch (Exception e) {
            log.error("Server crashed!!!", e);
        }
    }
    @Bean
    public ServerConfig configure(ConfigurationBuilder builder, ServerConfigManifest manifest) {
        return builder.configure(manifest);
    }
}
