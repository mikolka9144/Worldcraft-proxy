package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component("simba-3d")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimbaInterceptor extends CommandPacketInterceptor {
    private Thread runner = null;
    private SocketPacketSender pipe;

    @Autowired
    public SimbaInterceptor(MonikasConsole monika) {
        this.monika = monika;
    }

    @Override
    public void setupSockets(SocketPacketSender io) {
        pipe = io;
    }


    Vector3Short bedrockPos;
    MonikasConsole monika; // <3

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        if (data.getBlockType() == BlockData.BlockType.REDSTONE_ORE_ID){
            if (bedrockPos == null){
                bedrockPos = data.getPosition();
                formula.addWriteback(packager.serverPacket(
                        PacketCommand.SB_PLAYER_JOINED_ROOM
                        ,PacketContentSerializer.encodePlayerInfo(
                                monika.getMonika().summonMonika(bedrockPos)
                        )));
                formula.addWriteback(packager.println("Your Monika is here for you"));
            }
            else {
                formula.getUpstreamPackets().remove(packet);
                data.setBlockType(BlockData.BlockType.AIR);
                formula.addWriteback(packager.serverPacket(PacketCommand.S_SET_BLOCK_TYPE, PacketContentSerializer.encodeServerPlaceBlock(data)));
            }
        } else if (BlockData.BlockType.REDSTONE_ORE_ID.getId() == data.getPrevBlockType()) {
            formula.addWriteback(packager.serverPacket(PacketCommand.S_PLAYER_DISCONNECTED,
                    PacketContentSerializer.encodePlayerDisconnect(monika.getMonika().getPLAYER_ID())));
            bedrockPos = null;
        }
    }

    @Override
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {
//        log.info("Pos: "+data.getPosition());
//        log.info("Orientation: "+data.getOrientation());
//        log.info("Up: "+data.getUp());
//        log.info("----------------------------");

    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        if (bedrockPos != null && runner == null){
            List<StepPacket> steps = monika.processInput(message);
            runner = new Thread(() -> {
                for (StepPacket executionStep : steps) {
                    if (executionStep.getNewBlocks() != null){
                        // packager.createBlockComp(executionStep.getNewBlocks(),20)
                        executionStep.getNewBlocks().stream().map(s ->
                                        packager.serverPacket(
                                                PacketCommand.S_SET_BLOCK_TYPE,
                                                PacketContentSerializer.encodeServerPlaceBlock(s)))
                                .forEach(s -> pipe.sendToClient(s));
                    }
                    if (executionStep.getNewPosition() != null){
                        Packet movementPacket = packager.serverPacket(
                                PacketCommand.S_ENEMY_MOVE,
                                PacketContentSerializer.encodeEnemyMovementPacket(executionStep.getNewPosition())
                        );
                        pipe.sendToClient(movementPacket);
                    }
                    if (executionStep.getTextOutput() != null){
                        pipe.sendToClient(packager.println(executionStep.getTextOutput()));
                    }
                    try {
                        Thread.sleep(120);
                    } catch (InterruptedException e) {
                        log.error("Executing steps Interrupted!?");
                        runner.interrupt();
                    }
                }
                runner = null;
            });
            runner.start();
        }
    }
}
