package com.mikolka9144.worldcraft.modules.hackoring.fakers;

import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.common.api.packet.codecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component("device-spoofer")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceSpoofer extends CommandPacketInterceptor {
    private static final Random random = new Random();
    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        data.setDeviceId(String.valueOf(random.nextInt()));
        data.setAndroidVer("Android 1.0");
        data.setDeviceName("Helo_moto");
        data.setClientVer("4.0.0");
        packet.setData(PacketDataEncoder.login(data));
    }
}
