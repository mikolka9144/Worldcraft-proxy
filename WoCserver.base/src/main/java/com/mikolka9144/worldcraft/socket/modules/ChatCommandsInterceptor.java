package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.Packeter;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketsFormula;

import java.io.IOException;
import java.util.Arrays;
//FIXME rewrite this to use formula
public class ChatCommandsInterceptor extends FullPacketInterceptor {

    private Boolean isChatEnabled = false;
    private Boolean isFeedEnabled = true;
    private Integer blockId = null;
    private Integer blockData = null;
    private Packeter packager;

    @Override
    public PacketsFormula InterceptRawPacket(Packet packet) {
        if(packager == null) packager = new Packeter(packet.getProtoId());
        return super.InterceptRawPacket(packet);
    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
        if (!message.contains("/")) {
            if(isChatEnabled) return;
            else {
                packager.println("CHAT IS DISABLED");
                packager.println("You can enable it with '/chat on'");
                //formula.getUpstreamPackets()
            }
        }
        new Thread(() -> {
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
                            packager.println("Chat was disabled");
                        }
                        case "on" -> {
                            isChatEnabled = true;
                            packager.println("Chat was enabled");
                        }
                    }
                }
                case "setpointer" -> {
                    try {
                        if (command[1].equals("clear")) {
                            blockData = null;
                            blockId = null;
                            packager.println("block pointer resetted");
                        }
                        blockId = Integer.parseInt(command[1]);
                        blockData = Integer.parseInt(command[2]);
                        packager.println("Block pointer changed to " + blockId + ":" + blockData);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        packager.println("Command synax:");
                        packager.println("/setpointer <blockId> <blockData>");
                    }
                }
                default -> packager.println("Unknown command: " + Arrays.toString(command));
            }
        }).start();
        //throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
       // if (!isFeedEnabled) throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {

        try {
            if(data.getBlockType() == 7) {
                packager.sendBlockClientPacket(
                        data.getX()*data.getChunkX(),
                        data.getY(),
                        data.getZ()*data.getChunkZ(),
                        0,0,
                        data.getPrevBlockType(),
                        data.getPrevBlockData()
                );

                //nuke(data);
               // throw new PacketAlreadyInterceptedException();
            }
            if(blockId != null && blockData != null){
                packager.setBlock(
                        data.getX()*data.getChunkX(),
                        data.getY(),
                        data.getZ()*data.getChunkZ(),
                        blockId, blockData,
                        data.getPrevBlockType(),data.getPrevBlockData());
            }
            String message = getBlockLog(data);
            for (String line : message.split("\n")) {
                packager.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    private void nuke(BlockData data) throws IOException {
//        for (int x = -1; x < 2; x++) {
//            for (int z = -1; z < 2; z++) {
//                int airX = data.getX()+x;
//                if(airX == -1){
//                    airX = 15;
//                    data.setChunkX((short) (data.getChunkX()-1));
//                }
//                int airZ = data.getZ()+z;
//                if(airZ == -1){
//                    airZ = 15;
//                    data.setChunkZ((short) (data.getChunkZ()-1));
//                }
//                //var airToPlace = new BlockData((short) airX, (short) (data.getY()-1), (short) airZ, data.getChunkX(), data.getChunkZ(), (byte) 0,(byte) 2,(byte) 0,(byte) 0);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                packager.setBlock(airX,);
//                setBlock(airToPlace,0,0);
//            }
//        }
//    }

    private static String getBlockLog(BlockData data) {
        String message = String.format("Modify block:%n" +
                        "at %d %d %d (Chunk %d,%d)\n" +
                        "block %d:%d -> %d:%d",
                data.getX(), data.getY(), data.getZ(), data.getChunkX(), data.getChunkZ(),
                data.getPrevBlockType(), data.getPrevBlockData(), data.getBlockType(), data.getBlockData());
        return message;
    }


//    private void nukeWorld(){
//            for (int x = 0; x < 128; x++) {
//                for (int z = 0; z < 128; z++) {
//                    int chunkBlockX = x%16;
//                    int chunkBlockZ = z%16;
//                    int chunkX = x/16;
//                    int chunkZ = z/16;
//                    try {
//
//                        println(getBlockLog(data),PacketProtocol.WORLDCRAFT_V_2_8_7);
//                        Thread.sleep(10);
//                    } catch (IOException|InterruptedException e) {
//                        System.out.println(e);
//                }
//            }
//        }
//    }
//        @Override
//    public void interceptPlayerPositionReq(Packet packet, MovementPacket data) {
//        String message = String.format("You are at: X:%d Y:%d Z:%d",
//                Math.round(data.getPosition().getX()),
//                Math.round(data.getPosition().getY()),
//                        Math.round(data.getPosition().getZ()));
//        try {
//            println(message,packet.getProtoId()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
