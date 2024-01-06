package com.mikolka9144.worldcraft.backend.server.config;

import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@AllArgsConstructor
public class ConfigurationBuilder {
    private ApplicationContext context;

    public ServerConfig configure(ServerConfigManifest manifest) {
        if(!validateBeans(manifest)) return new ServerConfig();
        List<HttpDownloadInterceptor> httpDownloadPreset = new ArrayList<>(requestBeans(manifest.getSocketInterBeans(), HttpDownloadInterceptor.class));
        List<HttpUploadInterceptor> httpUploadPreset = new ArrayList<>(requestBeans(manifest.getHttpInterUpBeans(), HttpUploadInterceptor.class));
        Supplier<List<PacketAlteringModule>> socketPreset = () -> generatePacketInterceptors(manifest);
        return new ServerConfig(manifest.getSocketPort(), socketPreset, httpDownloadPreset, httpUploadPreset,true);
    }

    private <T> boolean validateBeans(List<String> beanNames, Class<T> type) {
        List<String> names = Arrays.stream(context.getBeanNamesForType(type)).toList();
        return beanNames.stream().allMatch(s -> {
            boolean isValid = names.contains(s);
            if(!isValid){
                log.error(String.format("Bean '%s' of type \"%s\" couldn't been found.",s,type.getSimpleName()));
                log.error("Consider removing this bean from your server configuration.");
                log.error("or create such bean extending that type.");
            }
            return isValid;
        });
    }
    private boolean validateBeans(ServerConfigManifest manifest) {
        return
                validateBeans(manifest.getSocketInterBeans(),PacketAlteringModule.class) &&
                validateBeans(manifest.getHttpInterUpBeans(),HttpUploadInterceptor.class) &&
                validateBeans(manifest.getHttpInterDownBeans(),HttpDownloadInterceptor.class);
    }

    private List<PacketAlteringModule> generatePacketInterceptors(ServerConfigManifest manifest) {
        return new ArrayList<>(requestBeans(manifest.getSocketInterBeans(), PacketAlteringModule.class));
    }

    private <T> List<T> requestBeans(List<String> beans, Class<T> target) {
        return beans.stream().map(x -> context.getBean(x,target)).toList();
    }
}
