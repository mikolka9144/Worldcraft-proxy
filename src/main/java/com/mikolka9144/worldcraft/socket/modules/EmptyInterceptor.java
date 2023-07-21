package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketAlteringModule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("none")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmptyInterceptor extends PacketAlteringModule {
}
