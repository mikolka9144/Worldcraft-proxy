package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

import java.util.Random;

public class DeviceSpoofer extends FullPacketInterceptor {
    private static Random random = new Random();
    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        data.setDeviceId(String.valueOf(random.nextInt()));
        data.setAndroidVer("Android 1.0");
        data.setDeviceName("Helo_moto");
        data.setClientVer("4.0.0");
        packet.setData(PacketContentSerializer.encodeLogin(data));
    }
}
