package com.mikolka9144.worldcraft.interceptors.unify.convert;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.backend.packets.codecs.LoginInfo;
import com.mikolka9144.worldcraft.backend.packets.codecs.RoomsPacket;
import com.mikolka9144.worldcraft.backend.packets.codecs.ServerBlockData;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.FullPacketInterceptor;
import com.mikolka9144.worldcraft.interceptors.unify.backend.PacketConverter;
import com.mikolka9144.worldcraft.interceptors.unify.backend.VersionFlags;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("blockConverter")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockConvertInterceptor extends FullPacketInterceptor {
    private VersionFlags flags;

    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        flags = VersionFlags.getFlags(packet.getProtoId());
    }

    @Override
    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {
        if (flags.simplifyBlocks()) {
            data.getRooms().forEach(s -> s.setId(-s.getId()));
            packet.setData(PacketDataEncoder.roomsQueryResponse(data));
        }
    }

    @Override
    public void interceptServerBlocks(Packet packet, ServerBlockData data, PacketsFormula formula) {
        if (!flags.simplifyBlocks()) return;
        data.getBlocks().forEach(s -> s.setBlockType(PacketConverter.replaceForWorldcraft(s.getBlockType())));
        packet.setData(PacketDataEncoder.serverBlocks(data));
    }

    @Override
    public void interceptServerPlaceBlock(Packet packet, Block data, PacketsFormula formula) {
        data.setBlockType(PacketConverter.replaceForWorldcraft(data.getBlockType()));
        packet.setData(PacketDataEncoder.serverPlaceBlock(data));
    }
}
