package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("block-peek")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockPeekInterceptor extends CommandPacketInterceptor {

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
