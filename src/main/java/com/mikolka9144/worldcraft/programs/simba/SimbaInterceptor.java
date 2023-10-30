package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("simba-3d")
public class SimbaInterceptor extends CommandPacketInterceptor {

    Vector3Short bedrockPos;
    Monika monika; // <3

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        if (data.getBlockType() == BlockData.BlockType.BEDROCK_ID){
            if (bedrockPos == null){
                bedrockPos = data.getPosition();
                monika = new Monika(bedrockPos,packager);
                formula.addWriteback(packager.serverPacket(PacketCommand.SB_PLAYER_JOINED_ROOM,PacketContentSerializer.encodePlayerInfo(monika.getHerPlayer())));
                formula.addWriteback(packager.println("Your Monika is here for you"));
            }
            else {
                formula.getUpstreamPackets().remove(packet);
                data.setBlockType(BlockData.BlockType.AIR);
                formula.addWriteback(packager.serverPacket(PacketCommand.S_SET_BLOCK_TYPE, PacketContentSerializer.encodeServerPlaceBlock(data)));
            }
        } else if (BlockData.BlockType.BEDROCK_ID.getId() == data.getPrevBlockType()) {
            formula.addWriteback(packager.serverPacket(PacketCommand.S_PLAYER_DISCONNECTED,PacketContentSerializer.encodePlayerDisconnect(monika.getHerPlayer().getId())));
            bedrockPos = null;
            monika = null;
        }
    }

    @Override
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {
        log.info("Pos: "+data.getPosition());
        log.info("Orientation: "+data.getOrientation());
        log.info("Up: "+data.getUp());
        log.info("----------------------------");

    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        monika.processInput(message,formula);
    }
}
