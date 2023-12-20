package com.mikolka9144.worldcraft.simba;

import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.backend.packets.codecs.MovementPacket;
import com.mikolka9144.worldcraft.backend.packets.codecs.PopupMessage;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.backend.server.socket.SocketPacketSender;
import com.mikolka9144.worldcraft.simba.backend.Monika.MonikasConsole;
import com.mikolka9144.worldcraft.simba.backend.models.StepPacket;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
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
    private List<Block> suspendedBlocks;
    private final MonikasConsole monika; // <3

    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
        if (data.getBlockName() == BlockType.REDSTONE_ORE_ID){
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
                data.setBlockName(BlockType.AIR);
                formula.addWriteback(packager.serverPacket(PacketCommand.S_SET_BLOCK_TYPE, PacketDataEncoder.serverPlaceBlock(data)));
            }
        } else if (BlockType.REDSTONE_ORE_ID.getId() == data.getPrevBlockType()) {
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
