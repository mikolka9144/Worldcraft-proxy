package com.mikolka9144.Impl;

import com.mikolka9144.Models.EventCodecs.LoginInfo;
import com.mikolka9144.Models.Packet.FullPacketInterceptor;
import com.mikolka9144.Models.Packet.Packet;
import com.mikolka9144.Utills.ContentParsers.PacketContentSerializer;
import com.mikolka9144.Worldcraft.ServerComponents.socket.WorldCraftPacketIO;

import java.io.IOException;
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

    @Override
    public void close() throws IOException {

    }
}
