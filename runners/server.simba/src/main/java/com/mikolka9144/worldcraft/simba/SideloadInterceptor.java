package com.mikolka9144.worldcraft.simba;

import com.mikolka9144.worldcraft.backend.base.api.PacketsFormula;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.spring.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.packet.packet.Packet;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
@Component("code-inject")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SideloadInterceptor extends CommandPacketInterceptor {
    @SneakyThrows
    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        if (message.equals("go")){
            String code = Files.readString(Path.of("./simba.sgo"));
            packet.setData(PacketDataEncoder.playerMessage(code));
        }
    }
}
