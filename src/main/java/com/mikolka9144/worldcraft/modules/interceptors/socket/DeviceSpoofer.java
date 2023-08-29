package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component("device-spoofer")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeviceSpoofer extends FullPacketInterceptor {
    private static final Random random = new Random();
    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        data.setDeviceId(String.valueOf(random.nextInt()));
        data.setAndroidVer("Android 1.0");
        data.setDeviceName("Helo_moto");
        data.setClientVer("4.0.0");
        packet.setData(PacketContentSerializer.encodeLogin(data));
    }
}