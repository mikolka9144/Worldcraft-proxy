package com.mikolka9144.worldcraft.modules.simba;

import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.common.api.packet.Packet;
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
