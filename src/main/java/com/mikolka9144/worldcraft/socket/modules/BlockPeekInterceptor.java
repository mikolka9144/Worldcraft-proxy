package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.Packeter;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

public class BlockPeekInterceptor extends FullPacketInterceptor {
    private Packeter packager;

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        if (packager == null) packager = new Packeter(packet.getProtoId());
        return super.InterceptRawPacket(packet);
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {


        if (data.getBlockType() == BlockData.BlockType.BEDROCK_ID) {
            formula.getUpstreamPackets().remove(packet);
            formula.addWriteback(packager.sendBlockClientPacket(data.getX() * data.getChunkX(), data.getY(), data.getZ() * data.getChunkZ(), BlockData.BlockType.AIR, 0, data.getPrevBlockData(), data.getPrevBlockType()));

        }
        String message = getBlockLog(data);
        for (String line : message.split("\n")) {
            formula.addWriteback(packager.println(line));
        }
    }

    private static String getBlockLog(BlockData data) {
        return String.format("Modify block:%n" + "at %d %d %d (Chunk %d,%d)\n" + "block %s:%d -> %s:%d",
                data.getX(),
                data.getY(),
                data.getZ(),
                data.getChunkX(),
                data.getChunkZ(),
                BlockData.BlockType.findBlockById(data.getPrevBlockType()).name(),
                data.getPrevBlockData(),
                data.getBlockType().name(),
                data.getBlockData());
    }
}
