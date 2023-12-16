package com.mikolka9144.worldcraft.backend.spring.config;

import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.spring.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.base.interceptor.PacketAlteringModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@AllArgsConstructor
public class ConfigurationBuilder {
    private ApplicationContext context;

    public ServerConfig configure(ServerConfigManifest manifest){
        Supplier<List<PacketAlteringModule>> socketPreset;

        List<HttpDownloadInterceptor> httpDownloadPreset = new ArrayList<>();

        List<HttpUploadInterceptor> httpUploadPreset = new ArrayList<>();

        socketPreset = () -> {
            List<PacketAlteringModule> interceptors = new ArrayList<>();
            for (String bean: manifest.getSocketInterBeans()) {
                interceptors.add(context.getBean(bean, PacketAlteringModule.class));
            }
            return interceptors;
        };
        for (String bean : manifest.getHttpInterDownBeans()) {
            httpDownloadPreset.add(context.getBean(bean,HttpDownloadInterceptor.class));
        }
        for (String bean : manifest.getHttpInterUpBeans()) {
            httpUploadPreset.add(context.getBean(bean,HttpUploadInterceptor.class));
        }

        return new ServerConfig(manifest.getSocketPort(), socketPreset,httpDownloadPreset,httpUploadPreset);
    }
}
