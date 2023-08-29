package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component("chat-commands")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ChatCommandsInterceptor extends FullPacketInterceptor {

    private Boolean isChatEnabled = false;
    private Boolean isFeedEnabled = true;
    private BlockData.BlockType blockId = null;
    private Integer blockData = null;


    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        formula.getUpstreamPackets().remove(packet); // this removes packet from sending queue
        if (!message.contains("/")) {
            if (isChatEnabled) {
                formula.addUpstream(packet); // this restores message
            }
            else {
                formula.addWriteback(packager.println("CHAT IS DISABLED"));
                formula.addWriteback(packager.println("You can enable it with '/chat on'"));
            }
            return;
        }
        String[] command = message.split("/", 2)[1].split(" ");
        switch (command[0]) {
            case "moto" -> {
                formula.addWriteback(packager.println("In the notepad"));
                formula.addWriteback(packager.println("fates are written"));
                formula.addWriteback(packager.println("cus Pandora didn't listen"));
                formula.addWriteback(packager.println("Time will march"));
                formula.addWriteback(packager.println("and here with me"));
                formula.addWriteback(packager.println("THIS SCREEN IS LAST YOU WILL EVER SEE"));
            }
            case "chat" -> {
                switch (command[1]) {
                    case "off" -> {
                        isChatEnabled = false;
                        formula.addWriteback(packager.println("Chat was disabled"));
                    }
                    case "on" -> {
                        isChatEnabled = true;
                        formula.addWriteback(packager.println("Chat was enabled"));
                    }
                    default -> {
                        formula.addWriteback(packager.println("Command syntax:"));
                        formula.addWriteback(packager.println("/chat <on/off>"));
                    }
                }
            }
            case "feed" -> {
                switch (command[1]) {
                    case "off" -> {
                        isFeedEnabled = false;
                        formula.addWriteback(packager.println("Feed was disabled"));
                    }
                    case "on" -> {
                        isFeedEnabled = true;
                        formula.addWriteback(packager.println("Feed was enabled"));
                    }
                    default -> {
                        formula.addWriteback(packager.println("Command syntax:"));
                        formula.addWriteback(packager.println("/feed <on/off>"));
                    }
                }
            }
            case "setpointer" -> {
                try {
                    if (command[1].equals("clear")) {
                        blockData = null;
                        blockId = null;
                        formula.addWriteback(packager.println("block pointer was reset"));
                    }
                    blockId = BlockData.BlockType.findBlockById(Byte.parseByte(command[1]));
                    blockData = Integer.parseInt(command[2]);
                    formula.addWriteback(packager.println("Block pointer changed to " + blockId + ":" + blockData));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    formula.addWriteback(packager.println("Command synax:"));
                    formula.addWriteback(packager.println("/setpointer <blockId> <blockData>"));
                }
            }
            case "help" -> {
                formula.addWriteback(packager.println("Available commands:"));
                formula.addWriteback(packager.println("chat,feed,moto,help,setpointer"));
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
                    formula.addWriteback(packager.println("Command synax:"));
                    formula.addWriteback(packager.println("/send packet_num"));
                }
            }
            default -> formula.addWriteback(packager.println(String.format("Unknown command: %s. use /help for command list", Arrays.toString(command))));
        }
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        if (blockId != null && blockData != null && !packet.getMessage().equals("tained")) {
            formula.getUpstreamPackets().remove(packet);
            int x = data.getX() * data.getChunkX();
            int y = data.getY();
            int z = data.getZ() * data.getChunkZ();
            var serverBlockPlace = packager.setBlockServerPacket(x,y,z,blockId,blockData);
            formula.addUpstream(packager.sendBlockClientPacket(x,y,z,blockId,blockData,data.getPrevBlockData(), data.getPrevBlockType()));
            formula.addWriteback(serverBlockPlace);
            log.info(String.format("Replacing block %s with %s",data.getBlockType(),blockId));
        }
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
         if (!isFeedEnabled) formula.getUpstreamPackets().remove(packet);
    }


}
