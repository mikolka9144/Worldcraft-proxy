package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.backend.spring.config.Innit;
import com.mikolka9144.worldcraft.backend.spring.config.ServerConfigManifest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {
        if(System.getenv("SPRING_PROFILES_ACTIVE") == null) {
            log.error("No profile selected. select profile with SPRING_PROFILES_ACTIVE=profile");
            log.error("Available profiles:");
            log.error("dev,hack,mod");
            return;
        }
        SpringApplication app = new SpringApplication(Main.class);
        Innit.start(app.run(args));
    }
    @Bean
    public static ServerConfigManifest createSource(Environment env){
        return new ServerConfigManifest(env);
    }
}