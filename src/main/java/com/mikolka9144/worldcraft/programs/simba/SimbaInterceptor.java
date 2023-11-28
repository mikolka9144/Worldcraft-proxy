package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.programs.simba.Monika.MonikasConsole;
import com.mikolka9144.worldcraft.programs.simba.models.StepPacket;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PopupMessage;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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


    private Vector3Short bedrockPos;
    private boolean isSuspended;
    private List<BlockData> suspendedBlocks;
    private MonikasConsole monika; // <3

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        if (data.getBlockType() == BlockData.BlockType.REDSTONE_ORE_ID){
            if (bedrockPos == null){
                bedrockPos = data.getPosition();
                formula.addWriteback(packager.serverPacket(
                        PacketCommand.SB_PLAYER_JOINED_ROOM
                        , PacketDataEncoder.playerInfo(
                                monika.getMonika().summonMonika(bedrockPos)
                        )));
                formula.addWriteback(packager.println("Your Monika is here for you"));
            }
            else {
                formula.getUpstreamPackets().remove(packet);
                data.setBlockType(BlockData.BlockType.AIR);
                formula.addWriteback(packager.serverPacket(PacketCommand.S_SET_BLOCK_TYPE, PacketDataEncoder.serverPlaceBlock(data)));
            }
        } else if (BlockData.BlockType.REDSTONE_ORE_ID.getId() == data.getPrevBlockType()) {
            formula.addWriteback(packager.serverPacket(PacketCommand.S_PLAYER_DISCONNECTED,
                    PacketDataEncoder.playerDisconnect(monika.getMonika().getPLAYER_ID())));
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
            switch (message){
                case "suspend" ->{
                    isSuspended = true;
                    suspendedBlocks = new ArrayList<>();
                    formula.addWriteback(packager.serverPacket(
                            PacketCommand.S_POPUP_MESSAGE,
                            PacketDataEncoder.popupMessage(
                                    new PopupMessage("Packets suspended!")
                            )
                    ));
                    return;
                }
                case "run" ->{
                    isSuspended = false;
                    pipe.sendToClient(packager.serverPacket(
                            PacketCommand.S_POPUP_MESSAGE,
                            PacketDataEncoder.popupMessage(
                                    new PopupMessage("Writing Packets!\nYour game might freeze!")
                            )
                    ));
                    formula.getWritebackPackets().addAll(suspendedBlocks.stream().map(s ->
                            packager.serverPacket(
                                    PacketCommand.S_SET_BLOCK_TYPE,
                                    PacketDataEncoder.serverPlaceBlock(s))).toList());
                    return;
                }
            }
            List<StepPacket> steps = monika.processInput(message);
            if (isSuspended){
                suspendedBlocks.addAll(steps.stream().flatMap(s -> s.getNewBlocks().stream()).toList());
            }
            else {
                runner = new Thread(() -> {
                    for (StepPacket executionStep : steps) {
                        if (executionStep.getNewBlocks() != null){
                            // packager.createBlockComp(executionStep.getNewBlocks(),20)
                            executionStep.getNewBlocks().stream().map(s ->
                                            packager.serverPacket(
                                                    PacketCommand.S_SET_BLOCK_TYPE,
                                                    PacketDataEncoder.serverPlaceBlock(s)))
                                    .forEach(s -> pipe.sendToClient(s));
                        }
                        if (executionStep.getNewPosition() != null){
                            Packet movementPacket = packager.serverPacket(
                                    PacketCommand.S_ENEMY_MOVE,
                                    PacketDataEncoder.enemyMovementPacket(executionStep.getNewPosition())
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
}
