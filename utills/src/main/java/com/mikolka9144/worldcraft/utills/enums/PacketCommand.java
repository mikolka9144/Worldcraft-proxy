package com.mikolka9144.worldcraft.utills.enums;

import com.mikolka9144.worldcraft.utills.exception.ParsingException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum PacketCommand {
    CLIENT_LOGIN_REQ(1),
    SERVER_LOGIN_RESP(2),
    CLIENT_SPEAK(10),
    SERVER_MESSAGE(11),
    CLIENT_ROOM_CREATE_REQ(14),
    SERVER_ROOM_CREATE_RESP(15),
    CLIENT_ROOM_JOIN_REQ(18),
    SERVER_ROOM_JOIN_RESP(19),
    CLIENT_CHECK_VERSION_REQ(21),
    SERVER_CHECK_VERSION_RESP(22),
    CLIENT_ROOM_LIST_REQ(24),
    SERVER_ROOM_LIST_RESP(25),
    CLIENT_PLACE_BLOCK_REQ(27),
    SERVER_PLACE_BLOCK_RESP(28),
    SERVER_UPDATE_BLOCK(29),
    CLIENT_PLAYER_MOVE_REQ(30),
    SERVER_PLAYER_MOVE_RESP(31),
    SERVER_ENEMY_MOVED(32),
    SERVER_PLAYER_LEFT(33),
    CLIENT_PLAYER_TAP_REQ(34),
    CLIENT_PLAYER_TAP_RESP(35),
    SERVER_ENEMY_TAPPED(36),
    CLIENT_READY_REQ(37),
    SERVER_READY_RESP(38),
    SERVER_ROOM_BLOCKS(40),
    SERVER_PLAYERS_LIST(45),// tag
    SERVER_PLAYER_JOINED(46),
    CLIENT_PING(47),
    SERVER_PONG(48),
    CLIENT_LIKE_WORLD(49),
    CLIENT_DISLIKE_WORLD(50),
    CLIENT_ROOM_SEARCH(51),
    CLIENT_REPORT_REQ(52),
    SERVER_REPORT_RESP(53),
    SERVER_DIALOG_DISPLAY(54),
    CLIENT_PLAYER_UPDATE_REQ(55),
    SERVER_PLAYER_UPDATE_RES(56),
    SERVER_ENEMY_UPDATED(57),
    CLIENT_PURCHASES_SAVE_REQ(58),
    SERVER_PURCHASES_SAVE_RESP(59),
    CLIENT_PURCHASES_LOAD_REQ(60),
    SERVER_PURCHASES_LOAD_RESP(61),
    CLIENT_PURCHASES_VALIDATE_REQ(62),
    SERVER_PURCHASES_VALIDATE_RESP(63);

    private final byte command;


    public static PacketCommand findPacketCommandById(byte id) {
        Optional<PacketCommand> command = Arrays.stream(PacketCommand.values()).filter(s -> s.getCommand() == id).findFirst();
        if (command.isPresent()) return command.get();
        throw new ParsingException("Command " + id + " is not declared");
    }

    PacketCommand(int command) {
        this.command = (byte) command;
    }
}
