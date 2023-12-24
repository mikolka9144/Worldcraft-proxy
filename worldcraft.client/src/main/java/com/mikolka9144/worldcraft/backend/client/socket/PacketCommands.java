package com.mikolka9144.worldcraft.backend.client.socket;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.*;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketHook;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;

import java.util.List;

/**
 * Interface for use with classes implementing type-based packet interpretation.
 * Use with {@link com.mikolka9144.worldcraft.backend.client.api.PacketCommandResolver}
 */
public interface PacketCommands {
    @PacketHook(PacketCommand.CLIENT_LOGIN_REQ)
    default void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ROOM_CREATE_RESP)
    default void interceptCreateRoomResp(Packet packet, String worldUploadToken, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_ROOM_CREATE_REQ)
    default void interceptCreateRoomReq(Packet packet, RoomCreateReq roomCreateReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_ROOM_SEARCH)
    default void interceptRoomsSearchReq(Packet packet, RoomSearchReq roomSearchReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PURCHASES_SAVE_RESP)
    default void interceptPurchaseSavingResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PURCHASES_SAVE_REQ)
    default void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PURCHASES_LOAD_RESP)
    default void interceptPurchaseLoadingResp(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PURCHASES_LOAD_REQ)
    default void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PONG)
    default void interceptPingResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PING)
    default void interceptPingReq(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_LIKE_WORLD)
    default void interceptLikeWorld(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_DISLIKE_WORLD)
    default void interceptDislikeWorld(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PLAYER_UPDATE_REQ)
    default void interceptUserProfileUpdateReq(Packet packet, Player player, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLAYER_UPDATE_RES)
    default void interceptUserProfileUpdateResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ENEMY_UPDATED)
    default void interceptPlayerUpdateInfo(Packet packet, PlayerInfo playerInfo, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLAYER_JOINED)
    default void interceptPlayerJoinedInfo(Packet packet, Player player, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLAYERS_LIST)
    default void interceptPlayerList(Packet packet, List<Player> players, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ROOM_JOIN_RESP)
    default void interceptJoinRoomResp(Packet packet, JoinRoomResponse joinRoomResponse, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_DIALOG_DISPLAY)
    default void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_ROOM_JOIN_REQ)
    default void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLAYER_LEFT)
    default void interceptPlayerDisconnect(Packet packet, int playerId, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_READY_REQ)
    default void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_READY_RESP)
    default void interceptGraphicsInitializationResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PLAYER_TAP_RESP)
    default void interceptPlayerActionResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PLAYER_TAP_REQ)
    default void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLACE_BLOCK_RESP)
    default void interceptPlaceBlockResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PLAYER_MOVE_RESP)
    default void interceptPlayerPositionResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_LOGIN_RESP)
    default void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_CHECK_VERSION_RESP)
    default void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_CHECK_VERSION_REQ)
    default void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_UPDATE_BLOCK)
    default void interceptServerPlaceBlock(Packet packet, Block data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PLACE_BLOCK_REQ)
    default void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ROOM_BLOCKS)
    default void interceptServerBlocks(Packet packet, ServerBlockData data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PLAYER_MOVE_REQ)
    default void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ENEMY_MOVED)
    default void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ENEMY_TAPPED)
    default void interceptEnemyAction(Packet packet, PlayerAction data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_MESSAGE)
    default void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_PURCHASES_VALIDATE_RESP)
    default void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_SPEAK)
    default void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_PURCHASES_VALIDATE_REQ)
    default void interceptPurchaseValidationRequest(Packet packet, PurchaseValidationReq data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SERVER_ROOM_LIST_RESP)
    default void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.CLIENT_ROOM_LIST_REQ)
    default void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {

    }
    @PacketHook(PacketCommand.CLIENT_REPORT_REQ)
    default void interceptPlayerReportReq(Packet packet, PlayerReport data,PacketsFormula formula){

    }
    @PacketHook(PacketCommand.SERVER_REPORT_RESP)
    default void interceptPlayerReportResp(Packet packet,PacketsFormula formula){

    }

    default void interceptUnknownPacket(Packet packet, PacketsFormula formula) {

    }
}
