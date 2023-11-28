package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
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
