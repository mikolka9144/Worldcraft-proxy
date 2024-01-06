package com.mikolka9144.worldcraft.backend.server.config;

import com.mikolka9144.worldcraft.backend.server.socket.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Configuration
public class Innit {
    public static void start(ApplicationContext context) {
        try {
            bootloader(context);
        } catch (NoSuchBeanDefinitionException b) {
            String className = Objects.requireNonNull(b.getBeanType()).getName();
            log.error("Bean of type " + className + " couldn't be found");
            log.error("Have you added 'com.mikolka9144.worldcraft' to 'ComponentScan' annotation?");
        } catch (Exception e) {
            log.error("Server crashed!!!", e);
        }
        log.error("Forcefully killing app...");
        System.exit(-1);
    }

    private static void bootloader(ApplicationContext context) throws IOException {
        var config = context.getBean(ServerConfig.class);
        if(!config.isConfigured()){
            log.error("Configuration failed to initialise.");
            return;
        }
        var server = context.getBean(SocketServer.class);
        log.info(String.format("Creating Server:  socketHost %d", config.getHostingSocketPort()));
        server.start();
    }

    @Bean
    public ServerConfig configure(ConfigurationBuilder builder, ServerConfigManifest manifest, ConfigurableApplicationContext context) {
        return builder.configure(manifest);
    }
}
