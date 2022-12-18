package com.mikolka9144.WoCserver.modules.socket;

import com.mikolka9144.WoCserver.model.EventCodecs.LoginInfo;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

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
