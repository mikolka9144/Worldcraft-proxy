package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.packetParsers.Vector3;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

import java.util.ArrayList;
import java.util.List;

public class HologramProjectionInterceptor extends FullPacketInterceptor {
    private List<Vector3> holographicBlocks = new ArrayList<>();
    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {

    }
}
