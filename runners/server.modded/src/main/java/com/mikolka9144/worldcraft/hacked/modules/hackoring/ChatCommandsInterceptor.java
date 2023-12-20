package com.mikolka9144.worldcraft.hacked.modules.hackoring;

import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component("chat-commands")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ChatCommandsInterceptor extends CommandPacketInterceptor {

    private Boolean isChatEnabled = false;
    private Boolean isFeedEnabled = true;
    private BlockType blockId = null;
    private Integer blockData = null;


    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet); // this removes packet from sending queue
        if (!message.contains("/")) {
            if (isChatEnabled) {
                formula.addUpstream(packet); // this restores message
            }
            else {
                println(formula,"CHAT IS DISABLED");
                println(formula,"You can enable it with '/chat on'");
            }
            return;
        }
        String[] command = message.split("/", 2)[1].split(" ");
        switch (command[0]) {
            case "moto" -> {
                println(formula,"In the notepad");
                println(formula,"fates are written");
                println(formula,"cus Pandora didn't listen");
                println(formula,"Time will march");
                println(formula,"and here with me");
                println(formula,"THIS SCREEN IS LAST YOU WILL EVER SEE");
            }
            case "chat" -> {
                switch (command[1]) {
                    case "off" -> {
                        isChatEnabled = false;
                        println(formula,"Chat was disabled");
                    }
                    case "on" -> {
                        isChatEnabled = true;
                        println(formula,"Chat was enabled");
                    }
                    default -> {
                        println(formula,"Command syntax:");
                        println(formula,"/chat <on/off>");
                    }
                }
            }
            case "feed" -> {
                switch (command[1]) {
                    case "off" -> {
                        isFeedEnabled = false;
                        println(formula,"Feed was disabled");
                    }
                    case "on" -> {
                        isFeedEnabled = true;
                        println(formula,"Feed was enabled");
                    }
                    default -> {
                        println(formula,"Command syntax:");
                        println(formula,"/feed <on/off>");
                    }
                }
            }
            case "setpointer" -> {
                try {
                    if (command[1].equals("clear")) {
                        blockData = null;
                        blockId = null;
                        println(formula,"block pointer was reset");
                    }
                    blockId = BlockType.findBlockById(Byte.parseByte(command[1]));
                    blockData = Integer.parseInt(command[2]);

                    println(formula,"Block pointer changed to " + blockId + ":" + blockData);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    println(formula,"Command syntax:");
                    println(formula,"/setpointer <blockId> <blockData>");
                }
            }
            case "help" -> {
                println(formula,"Available commands:");
                println(formula,"chat,feed,moto,help,setpointer,send");
            }
            case "repeat" ->{
                if(command.length != 3){
                    println(formula,"Command synax:");
                    println(formula,"/repeat <sequence> <count>");
                }
                else {
                    try {
                        String seqence = command[1];
                        int count = Integer.parseInt(command[2]);
                        String line = new String(new char[count]).replace("\0", seqence);
                        formula.addUpstream(packager.writeln(line));
                    }
                    catch (NumberFormatException x){
                        println(formula,"Invalid count number");
                        println(formula,"Command syntax:");
                        println(formula,"/repeat <sequence> <count>");
                    }
                }
            }
            case "send" -> {
                try {
                    switch (command[1]){
                        case "1" ->
                            formula.addWriteback(new Packet(PacketProtocol.SERVER,0, PacketCommand.SUBCLASS_FIRST_CMD_ID,"test",(byte)0,
                                    new byte[]{} ));

                        case "0" ->
                            formula.addUpstream(new Packet(PacketProtocol.UNKNOWN, packet.getPlayerId(), PacketCommand.SUBCLASS_FIRST_CMD_ID,"test",(byte)0,
                                    new byte[]{} ));

                    }
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    println(formula,"Command synax:");
                    println(formula,"/send packet_num");
                }
            }
            default -> println(formula,String.format("Unknown command: %s. use /help for command list", Arrays.toString(command)));
        }
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
        if (blockId != null && blockData != null && data.getBlockName() != BlockType.AIR) {
            formula.getUpstreamPackets().remove(packet);
            data.setBlockName(blockId);
            data.setBlockData(blockData.byteValue());
            var serverBlockPlace = packager.setBlockServerPacket(data);
            formula.addUpstream(packager.sendBlockClientPacket(data));
            formula.addWriteback(serverBlockPlace);
            log.info(String.format("Replacing block %s with %s",data.getBlockType(),blockId));
        }
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
         if (!isFeedEnabled) formula.getUpstreamPackets().remove(packet);
    }
    private void println(PacketsFormula formula,String message){
        formula.addWriteback(packager.println(message));
    }

}
