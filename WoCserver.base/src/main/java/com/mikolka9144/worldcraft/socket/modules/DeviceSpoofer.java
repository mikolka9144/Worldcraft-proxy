package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.LoginInfo;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.ContentParsers.PacketContentSerializer;

import java.util.Random;

public class DeviceSpoofer extends FullPacketInterceptor {
    public DeviceSpoofer(WorldCraftPacketIO connectionIO) {
        super(connectionIO);
    }

    @Override
    public void interceptLogin(Packet packet, LoginInfo data) {
        data.setDeviceId(String.valueOf(new Random().nextInt()));
        data.setAndroidVer("Android 1.0");
        data.setDeviceName("Helo_moto");
        data.setClientVer("4.0.0");
        packet.setData(PacketContentSerializer.encodeLogin(data));
    }
}
