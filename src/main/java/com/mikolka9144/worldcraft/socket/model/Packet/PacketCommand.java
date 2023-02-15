package com.mikolka9144.worldcraft.socket.model.Packet;

import java.util.Arrays;
import java.util.Optional;

public enum PacketCommand {
    C_LOGIN_REQ(1),
    S_LOGIN_RESP(2),
    SB_LOGIN(4),
    C_LOGOUT(5),
    SB_LOGOUT(6),
    S_DISCONNECT(7),
    C_QUIT_GAME(8),
    C_CHAT_MSG(10),
    SB_CHAT_MSG(11),
    C_MOVE_REQ(12),
    S_MOVE_RESP(13),
    C_CREATE_ROOM_REQ(14),
    S_CREATE_ROOM_RESP(15),
    SB_CREATE_ROOM(16),
    SBNIR_CREATE_ROOM(17),
    C_JOIN_ROOM_REQ(18),
    S_JOIN_ROOM_RESP(19),
    SB_JOIN_ROOM(20),
    C_CHECK_VERSION_REQ(21),
    S_CHECK_VERSION_RESP(22),
    C_ROOM_LIST_REQ(24),
    S_ROOM_LIST_RESP(25),
    S_ROOM_LIST(26),
    C_SET_BLOCK_TYPE_REQ(27),
    S_SET_BLOCK_TYPE_RESP(28),
    S_SET_BLOCK_TYPE(29),
    C_PLAYER_MOVE_REQ(30),
    S_PLAYER_MOVE_RESP(31),
    S_ENEMY_MOVE(32),
    S_PLAYER_DISCONNECTED(33),
    C_PLAYER_ACTION_REQ(34),
    S_PLAYER_ACTION_RESP(35),
    S_ENEMY_ACTION(36),
    C_PLAYER_GRAPHICS_INITED_REQ(37),
    S_PLAYER_GRAPHICS_INITED_RESP(38),
    S_PLAYER_GRAPHICS_INITED(39),
    S_MODIFIED_BLOCKS(40),
    SUBCLASS_FIRST_CMD_ID(41),
    S_PLAYERS_INFO(45),
    SB_PLAYER_JOINED_ROOM(46),
    C_PING_REQ(47),
    S_PING_RESP(48),
    C_LIKE_WORLD_REQ(49),
    C_DISLIKE_WORLD_REQ(50),
    C_ROOM_SEARCH_REQ(51),
    C_REPORT_ABUSE_REQ(52),
    S_REPORT_ABUSE_RES(53),
    S_POPUP_MESSAGE(54),
    C_UPDATE_PROFILE_REQ(55),
    S_UPDATE_PROFILE_RES(56),
    SB_PLAYER_UPDATED(57),
    C_SAVE_PURCHASES_REQ(58),
    S_SAVE_PURCHASES_RES(59),
    C_LOAD_PURCHASES_REQ(60),
    S_LOAD_PURCHASES_RES(61),
    C_VALIDATE_PURCHASE_REQ(62),
    S_VALIDATE_PURCHASE_RES(63);

    private byte command;


    public byte getCommand() {
        return command;
    }

    public static PacketCommand findPacketCommandById(byte Id){
        Optional<PacketCommand> command = Arrays.stream(PacketCommand.values()).filter(s -> s.getCommand() == Id).findFirst();
        if(command.isPresent()) return command.get();
        throw new RuntimeException("Command "+Id+" is not declared");
    }

    PacketCommand(int command) {
        this.command = (byte)command;
    }
}
