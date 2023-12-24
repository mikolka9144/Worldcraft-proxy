package com.mikolka9144.worldcraft.backend.server.config;

import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.backend.server.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
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
            httpDownloadPreset.add(requestBean(bean,HttpDownloadInterceptor.class));
        }
        for (String bean : manifest.getHttpInterUpBeans()) {
            httpUploadPreset.add(context.getBean(bean,HttpUploadInterceptor.class));
        }

        return new ServerConfig(manifest.getSocketPort(), socketPreset,httpDownloadPreset,httpUploadPreset);
    }

    private <T> T requestBean(String bean, Class<T> target) {
        try {
            return context.getBean(bean, target);
        }
        catch (NoSuchBeanDefinitionException x){
            log.error("Bean "+x.getBeanName()+" couldn't be found.");
            log.error("Consider removing that bean from server configuration.");
            log.error("or create such bean (extending "+target.getName()+" ofc.)");
            throw new KillAppException();
        }
        catch (BeanNotOfRequiredTypeException x){
            log.error("Bean "+x.getBeanName()+" was found, but was inserted to incorrect list.");
            log.error("Bean was expected to be "+target.getName());
            log.error("But is "+x.getActualType().getName());
            log.error("Make sure to put "+bean+ " to correct list in configuration");
            throw new KillAppException();
        }
    }
}
