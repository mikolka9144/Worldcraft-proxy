package com.mikolka9144.worldcraft.backend.server.config;

import com.mikolka9144.worldcraft.backend.server.socket.WorldcraftServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Configuration
@ComponentScan("com.mikolka9144.worldcraft")
public class WorldcraftServerBootstrap implements ApplicationRunner {
    private final ApplicationContext context;

    public WorldcraftServerBootstrap(ApplicationContext context){
        this.context = context;
    }

    private static void bootloader(ApplicationContext context){
        var config = context.getBean(ServerConfig.class);
        if(!config.isConfigured()){
            log.error("Configuration failed to initialise.");
            return;
        }
        var server = context.getBean(WorldcraftServer.class);
        log.info(String.format("Creating Server:  socketHost %d", config.getHostingSocketPort()));
        server.start();
    }

    @Bean
    public static ServerConfig configure(ConfigurationBuilder builder, ServerConfigManifest manifest, ConfigurableApplicationContext context) {
        return builder.configure(manifest);
    }

    @Override
    public void run(ApplicationArguments args)  {
         if(context.getBeansOfType(ServerConfigManifest.class).isEmpty()){
             log.error("Bean of type ServerConfigManifest couldn't be found");
             log.error("Have you added configuration to this app?");
             System.exit(-1);
         }

        try {
            bootloader(context);
        } catch (Exception e) {
            log.error("Server crashed!!!", e);
        }
        log.error("Forcefully killing app...");
        System.exit(-1);
    }
}
