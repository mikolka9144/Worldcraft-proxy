package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.model.Vector3;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component("holo-projector")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HologramProjectionInterceptor extends FullPacketInterceptor {
    private List<Vector3> holographicBlocks = new ArrayList<>();
    private String template = "";

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        int origin_x = data.getX()+(16*data.getChunkX());
        int origin_z = data.getZ()+(16*data.getChunkZ());

        formula.getUpstreamPackets().remove(packet);
        if(data.getBlockType() == BlockData.BlockType.BEDROCK_ID){
            switch (template){
                case "flower":
                    registerPacket(formula,origin_x, (short)(data.getY()-1), origin_z, BlockData.BlockType.GLASS_ID);
                    registerPacket(formula,origin_x+1, (short)(data.getY()-1), origin_z, BlockData.BlockType.GLASS_ID);
                    registerPacket(formula,origin_x-1, (short)(data.getY()-1), origin_z, BlockData.BlockType.GLASS_ID);
                    registerPacket(formula,origin_x, (short)(data.getY()-1), origin_z+1, BlockData.BlockType.GLASS_ID);
                    registerPacket(formula,origin_x, (short)(data.getY()-1), origin_z-1, BlockData.BlockType.GLASS_ID);
                    registerPacket(formula,origin_x, data.getY(), origin_z, BlockData.BlockType.WOOD_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+1), origin_z, BlockData.BlockType.WOOD_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+2), origin_z, BlockData.BlockType.WOOD_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+3), origin_z, BlockData.BlockType.WOOD_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+4), origin_z, BlockData.BlockType.GLOW_STONE_ID);
                    registerPacket(formula,origin_x+1, (short) (data.getY()+4), origin_z, BlockData.BlockType.LEAVES_ID);
                    registerPacket(formula,origin_x-1,(short) (data.getY()+4), origin_z, BlockData.BlockType.LEAVES_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+4), origin_z+1, BlockData.BlockType.LEAVES_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+4), origin_z-1, BlockData.BlockType.LEAVES_ID);
                    registerPacket(formula,origin_x, (short) (data.getY()+5), origin_z, BlockData.BlockType.SLAB_ID);
                    return;
                default:
                    formula.addWriteback(packager.println("Template is invalid"));
                    formula.addWriteback(packager.setBlockServerPacket(origin_x, data.getY(), origin_z, BlockData.BlockType.AIR,0));
            }

        } else if (data.getBlockType() == BlockData.BlockType.AIR) {
            if(holographicBlocks.stream().anyMatch(s -> s.getX()==origin_x&&s.getY()== data.getY()&&s.getZ()==origin_z)){
                cleanAllHolograms(formula);
            }
            else {
                formula.addUpstream(packet);
            }
        }
        else {
            formula.addUpstream(packet);
        }
    }

    private void registerPacket(PacketsFormula formula, int originX, short y, int originZ, BlockData.BlockType dirtId) {
        holographicBlocks.add(new Vector3(originX,y,originZ));
        formula.addWriteback(packager.setBlockServerPacket(originX,y,originZ,dirtId,0));
    }

    private void cleanAllHolograms(PacketsFormula formula) {
        for (Vector3 data : holographicBlocks) {
            formula.addWriteback(packager.setBlockServerPacket((int) data.getX(),(int)data.getY(),(int)data.getZ(), BlockData.BlockType.AIR,0));
        }
    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        if (!message.contains("/")) {
            return;
        }
        String[] command = message.split("/", 2)[1].split(" ");
        if(command[0].equals("holo")){
            formula.getUpstreamPackets().remove(packet); // this removes packet from sending queue

            template = command[1];
            formula.addWriteback(packager.println("Template set"));
        }
    }
}
