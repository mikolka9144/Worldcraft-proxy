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
    @PacketHook(PacketCommand.C_LOGIN_REQ)
    default void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_CREATE_ROOM_RESP)
    default void interceptCreateRoomResp(Packet packet, String worldUploadToken, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_CREATE_ROOM_REQ)
    default void interceptCreateRoomReq(Packet packet, RoomCreateReq roomCreateReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_ROOM_SEARCH_REQ)
    default void interceptRoomsSearchReq(Packet packet, RoomSearchReq roomSearchReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_SAVE_PURCHASES_RES)
    default void interceptPurchaseSavingResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_SAVE_PURCHASES_REQ)
    default void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_LOAD_PURCHASES_RES)
    default void interceptPurchaseLoadingResp(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_LOAD_PURCHASES_REQ)
    default void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PING_RESP)
    default void interceptPingResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PING_REQ)
    default void interceptPingReq(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_LIKE_WORLD_REQ)
    default void interceptLikeWorld(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_DISLIKE_WORLD_REQ)
    default void interceptDislikeWorld(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_UPDATE_PROFILE_REQ)
    default void interceptUserProfileUpdateReq(Packet packet, Player player, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_UPDATE_PROFILE_RES)
    default void interceptUserProfileUpdateResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SB_PLAYER_UPDATED)
    default void interceptPlayerUpdateInfo(Packet packet, PlayerInfo playerInfo, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SB_PLAYER_JOINED_ROOM)
    default void interceptPlayerJoinedInfo(Packet packet, Player player, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYERS_INFO)
    default void interceptPlayerList(Packet packet, List<Player> players, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_JOIN_ROOM_RESP)
    default void interceptJoinRoomResp(Packet packet, JoinRoomResponse joinRoomResponse, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_POPUP_MESSAGE)
    default void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_JOIN_ROOM_REQ)
    default void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_DISCONNECTED)
    default void interceptPlayerDisconnect(Packet packet, int playerId, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PLAYER_GRAPHICS_INITED_REQ)
    default void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_GRAPHICS_INITED_RESP)
    default void interceptGraphicsInitializationResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_ACTION_RESP)
    default void interceptPlayerActionResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PLAYER_ACTION_REQ)
    default void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_SET_BLOCK_TYPE_RESP)
    default void interceptPlaceBlockResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_MOVE_RESP)
    default void interceptPlayerPositionResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_LOGIN_RESP)
    default void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_CHECK_VERSION_RESP)
    default void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_CHECK_VERSION_REQ)
    default void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_SET_BLOCK_TYPE)
    default void interceptServerPlaceBlock(Packet packet, Block data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_SET_BLOCK_TYPE_REQ)
    default void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_MODIFIED_BLOCKS)
    default void interceptServerBlocks(Packet packet, ServerBlockData data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PLAYER_MOVE_REQ)
    default void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ENEMY_MOVE)
    default void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ENEMY_ACTION)
    default void interceptEnemyAction(Packet packet, PlayerAction data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SB_CHAT_MSG)
    default void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_VALIDATE_PURCHASE_RES)
    default void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_CHAT_MSG)
    default void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_VALIDATE_PURCHASE_REQ)
    default void interceptPurchaseValidationRequest(Packet packet, PurchaseValidationReq data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ROOM_LIST_RESP)
    default void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_ROOM_LIST_REQ)
    default void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {

    }
    @PacketHook(PacketCommand.C_REPORT_ABUSE_REQ)
    default void interceptPlayerReportReq(Packet packet, PlayerReport data,PacketsFormula formula){

    }
    @PacketHook(PacketCommand.S_REPORT_ABUSE_RES)
    default void interceptPlayerReportResp(Packet packet,PacketsFormula formula){

    }

    default void interceptUnknownPacket(Packet packet, PacketsFormula formula) {

    }
}
