package com.mikolka9144.WoCserver.modules.socket;

import com.mikolka9144.WoCserver.model.EventCodecs.BlockData;
import com.mikolka9144.WoCserver.model.EventCodecs.ChatMessage;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Packet;
import com.mikolka9144.WoCserver.model.Packet.PacketCommand;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.WoCserver.model.PacketProtocol;
import com.mikolka9144.WoCserver.utills.PacketParsers.ContentParsers.PacketContentSerializer;
import com.mikolka9144.WoCserver.utills.PacketAlreadyInterceptedException;
import com.mikolka9144.WoCserver.utills.Packeter;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;

import java.io.IOException;
import java.util.Arrays;

public class ChatCommandsInterceptor extends FullPacketInterceptor {
    private PacketInterceptor serverInterceptor;
    private Boolean isChatEnabled = false;
    private Boolean isFeedEnabled = true;
    private Integer blockId = null;
    private Integer blockData = null;
    private Packeter packager;

    public ChatCommandsInterceptor(WorldCraftPacketIO connectionIO, PacketInterceptor serverInterceptor) {
        super(connectionIO);
        this.serverInterceptor = serverInterceptor;

    }

    @Override
    public void InterceptRawPacket(Packet packet) {
        if(packager == null) packager = new Packeter(connectionIO ,serverInterceptor,packet.getProtoId());
        super.InterceptRawPacket(packet);
    }

    @Override
    public void interceptPlayerMessage(Packet packet, String message) {
        if (!message.contains("/")) {
            if(isChatEnabled) return;
            else {
                packager.println("CHAT IS DISABLED");
                packager.println("You can enable it with '/chat on'");
                throw new PacketAlreadyInterceptedException();
            }
        }
        new Thread(() -> {
            String[] command = message.split("/", 2)[1].split(" ");
            switch (command[0]) {
                case "moto" -> {
                    try {
                        packager.println("In the notepad");
                        Thread.sleep(500);
                        packager.println("fates are written");
                        Thread.sleep(500);
                        packager.println("cus Pandora didn't listen");
                        Thread.sleep(1000);
                        packager.println("Time will march");
                        Thread.sleep(500);
                        packager.println("and here with me");
                        Thread.sleep(1800);
                        packager.println("THIS SCREEN IS LAST YOU WILL EVER SEE");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
        throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data) {
        if (!isFeedEnabled) throw new PacketAlreadyInterceptedException();
    }

    @Override
    public void interceptPlaceBlockReq(Packet packet, BlockData data) {

        try {
            if(data.getBlockType() == 7) {
                setBlock(data,0,0);

                nuke(data);
                throw new PacketAlreadyInterceptedException();
            }
            if(blockId != null && blockData != null){
                setBlock(packet, data, blockId, blockData);
            }
            String message = getBlockLog(data);
            for (String line : message.split("\n")) {
                packager.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void nuke(BlockData data) throws IOException {
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                int airX = data.getX()+x;
                if(airX == -1){
                    airX = 15;
                    data.setChunkX((short) (data.getChunkX()-1));
                }
                int airZ = data.getZ()+z;
                if(airZ == -1){
                    airZ = 15;
                    data.setChunkZ((short) (data.getChunkZ()-1));
                }
                var airToPlace = new BlockData((short) airX, (short) (data.getY()-1), (short) airZ, data.getChunkX(), data.getChunkZ(), (byte) 0,(byte) 2,(byte) 0,(byte) 0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                setBlock(airToPlace,0,0);
            }
        }
    }

    private static String getBlockLog(BlockData data) {
        String message = String.format("Modify block:%n" +
                        "at %d %d %d (Chunk %d,%d)\n" +
                        "block %d:%d -> %d:%d",
                data.getX(), data.getY(), data.getZ(), data.getChunkX(), data.getChunkZ(),
                data.getPrevBlockType(), data.getPrevBlockData(), data.getBlockType(), data.getBlockData());
        return message;
    }

    private void setBlock(Packet packet, BlockData data, int type, int blockData) throws IOException {
        data.setBlockType((byte) type);
        data.setBlockData((byte) blockData);
        Packet modifyBlock = new Packet(PacketProtocol.SERVER, packet.getPlayerId(),
                PacketCommand.S_SET_BLOCK_TYPE, "", (byte) 0,
                PacketContentSerializer.encodeServerPlaceBlock(data));
        try {
            connectionIO.send(modifyBlock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        packet.setData(PacketContentSerializer.encodePlaceBlockReq(data));
    }
    private void setBlock(BlockData data, int type, int blockData) throws IOException {
        var feedbackPacket = new Packet(PacketProtocol.SERVER,0,PacketCommand.S_MODIFIED_BLOCKS,"",(byte)0,new byte[1]);
        setBlock(feedbackPacket,data,type,blockData);
        serverInterceptor.InterceptRawPacket(feedbackPacket);
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



    @Override
    public void close() throws IOException {

    }
}
