package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.backend.server.config.Innit;
import com.mikolka9144.worldcraft.backend.server.config.ServerConfigManifest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@Slf4j
public class Main {
    public static void main(String[] args) {
        if(System.getenv("SPRING_PROFILES_ACTIVE") == null) {
            log.error("No profile selected. select profile with SPRING_PROFILES_ACTIVE=profile");
            log.error("Available profiles:");
            log.error("legacy,official,proxy,proxyLegacy");
            return;
        }
        SpringApplication app = new SpringApplication(Main.class);
        AtomicReference<String> addr = new AtomicReference<>("64.237.54.60");
        Arrays.stream(args).findFirst().ifPresent(s ->{
            log.info("LOADING CUSTOM SERVER: "+s);
            addr.set(s);
        });
        app.setDefaultProperties(Collections
                .singletonMap("server.target.address", addr.get().toLowerCase()));
        Innit.start(app.run(args));
    }
    @Bean
    public static ServerConfigManifest createSource(Environment env){
        return new ServerConfigManifest(env);
    }
}