package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.Packeter;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

import java.util.Arrays;

public class ChatCommandsInterceptor extends FullPacketInterceptor {

    private Boolean isChatEnabled = false;
    private Boolean isFeedEnabled = true;
    private Integer blockId = null;
    private Integer blockData = null;
    private Packeter packager;

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        if (packager == null) packager = new Packeter(packet.getProtoId());
        return super.InterceptRawPacket(packet);
    }

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
                        formula.addWriteback(packager.println("Command synax:"));
                        formula.addWriteback(packager.println("/chat <on/off>"));
                    }
                }
            }
            case "setpointer" -> {
                try {
                    if (command[1].equals("clear")) {
                        blockData = null;
                        blockId = null;
                        formula.addWriteback(packager.println("block pointer resetted"));
                    }
                    blockId = Integer.parseInt(command[1]);
                    blockData = Integer.parseInt(command[2]);
                    formula.addWriteback(packager.println("Block pointer changed to " + blockId + ":" + blockData));
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    formula.addWriteback(packager.println("Command synax:"));
                    formula.addWriteback(packager.println("/setpointer <blockId> <blockData>"));
                }
            }
            default -> formula.addWriteback(packager.println("Unknown command: " + Arrays.toString(command)));
        }
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
        if (blockId != null && blockData != null) {
            int x = data.getX() * data.getChunkX();
            int y = data.getY();
            int z = data.getZ() * data.getChunkZ();
            formula.addUpstream( packager.setBlockServerPacket(x,y,z,blockId,blockData));
            formula.addWriteback(packager.sendBlockClientPacket(x,y,z,blockId,blockData,data.getPrevBlockType(), data.getPrevBlockData()));
        }
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
         if (!isFeedEnabled) formula.getUpstreamPackets().remove(packet);
    }


}
