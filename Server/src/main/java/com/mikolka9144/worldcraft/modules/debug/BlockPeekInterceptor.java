package com.mikolka9144.worldcraft.modules.debug;

import com.mikolka9144.packet.packet.codecs.Block;
import com.mikolka9144.packet.packet.enums.BlockType;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("block-peek")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BlockPeekInterceptor extends CommandPacketInterceptor {

    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {


        if (data.getBlockName() == BlockType.BEDROCK_ID) {
            formula.getUpstreamPackets().remove(packet);
            formula.addWriteback(packager.sendBlockClientPacket(data.getX() * data.getChunkX(), data.getY(), data.getZ() * data.getChunkZ(), BlockType.AIR, 0, data.getPrevBlockData(), data.getPrevBlockType()));

        }
        String message = getBlockLog(data);
        for (String line : message.split("\n")) {
            formula.addWriteback(packager.println(line));
        }
    }

    private static String getBlockLog(Block data) {
        return String.format("Modify block:%n" + "at %d %d %d (Chunk %d,%d)\n" + "block %s:%d -> %s:%d",
                data.getX(),
                data.getY(),
                data.getZ(),
                data.getChunkX(),
                data.getChunkZ(),
                BlockType.findBlockById(data.getPrevBlockType()).name(),
                data.getPrevBlockData(),
                data.getBlockName().name(),
                data.getBlockData());
    }
}
