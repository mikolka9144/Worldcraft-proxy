package com.mikolka9144.worldcraft.backend.spring.unify.convert;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.codecs.Block;
import com.mikolka9144.packet.packet.codecs.LoginInfo;
import com.mikolka9144.packet.packet.codecs.RoomsPacket;
import com.mikolka9144.packet.packet.codecs.ServerBlockData;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.spring.unify.backend.VersionFlags;
import com.mikolka9144.worldcraft.backend.base.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.base.interceptor.CommandPacketInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component("block-convert")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockConvertInterceptor extends CommandPacketInterceptor {
    private VersionFlags flags;

    @Override
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
        flags = VersionFlags.getFlags(packet.getProtoId());
    }

    @Override
    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {
        if (flags.simplifyBlocks()){
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
