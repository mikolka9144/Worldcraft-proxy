package com.mikolka9144.worldcraft.socket.interceptor;

import com.mikolka9144.worldcraft.common.api.packet.Packet;
import com.mikolka9144.worldcraft.common.api.packet.codecs.*;
import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketDataDecoder;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketCommand;
import com.mikolka9144.worldcraft.common.api.packet.enums.PacketProtocol;
import com.mikolka9144.worldcraft.socket.api.PacketBuilder;
import com.mikolka9144.worldcraft.common.api.packet.encodings.PacketHook;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Abstract class containing basic packet recognition for use as a base for {@code interceptors}.
 * In addition, it also initialises {@link PacketBuilder packager}, but only after it receives playerId from one of packets.
 */
@Slf4j
public abstract class CommandPacketInterceptor extends PacketAlteringModule {

    protected PacketBuilder packager = null;

    // first packet is added by default
    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        configurePacketer(packet);
        PacketsFormula formula = super.interceptRawPacket(packet);
        runPacketHook(packet.getCommand(),packet,formula);
        return formula;
    }



    private void runPacketHook(PacketCommand target, Packet packet, PacketsFormula formula) {
        Method[] declaredCalls = getClass().getDeclaredMethods();
        Method[] allCalls = CommandPacketInterceptor.class.getDeclaredMethods();
        Optional<Method> baseMethod = findTargetInMethods(target, allCalls);
        String callName = baseMethod.map(Method::getName).orElse("interceptUnknownPacket");
        Optional<Method> call = findTargetInMethods(callName,declaredCalls);

        call.ifPresent(
                x -> {
                    Object argument = null;
                    try {
                         argument = getPacketData(packet.getCommand(), packet.getData());
                        if (argument != null) x.invoke(this, packet, argument, formula);
                        else x.invoke(this, packet, formula);
                    } catch (Exception exp) {
                        log.error(String.format("Method execution failed for %s with argument %s",target,argument), exp);
                    }
                }
        );
    }

    private Object getPacketData(PacketCommand target, byte[] data) {
        Method[] encoders = PacketDataDecoder.class.getMethods();
        Optional<Method> call = findTargetInMethods(target, encoders);
        if (call.isPresent()) {
            try {
                return call.get().invoke(null, (Object) data);
            } catch (Exception exp) {
                log.error("Method execution failed for following method: " + target, exp);
            }
        }
        return null;
    }

    private static Optional<Method> findTargetInMethods(PacketCommand target, Method[] pool) {
        return Arrays.stream(pool)
                .filter(s -> s.getAnnotation(PacketHook.class) != null)
                .filter(s -> s.getAnnotation(PacketHook.class).value().equals(target)).findFirst();
    }
    private void configurePacketer(Packet packet) {
        if (packager == null) {
            if (packet.getProtoId() != PacketProtocol.SERVER) {
                this.packager = new PacketBuilder(packet.getProtoId());
            }
            return;
        }
        if (packet.getPlayerId() != 0 && packager.getPlayerId() == 0) {
            packager.setPlayerId(packet.getPlayerId());
        }
    }

    private static Optional<Method> findTargetInMethods(String target, Method[] pool) {
        return Arrays.stream(pool)
                .filter(s -> s.getName().equals(target)).findFirst();
    }

    @PacketHook(PacketCommand.S_CREATE_ROOM_RESP)
    public void interceptCreateRoomResp(Packet packet, String worldUploadToken, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_CREATE_ROOM_REQ)
    public void interceptCreateRoomReq(Packet packet, RoomCreateReq roomCreateReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_ROOM_SEARCH_REQ)
    public void interceptRoomsSearchReq(Packet packet, RoomSearchReq roomSearchReq, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_SAVE_PURCHASES_RES)
    public void interceptPurchaseSavingResp(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_SAVE_PURCHASES_REQ)
    public void interceptPurchaseSavingReq(Packet packet, PurchasesList purchasesList, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_LOAD_PURCHASES_RES)
    public void interceptPurchaseLoadingResp(Packet packet, @Nullable PurchasesList purchasesList, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_LOAD_PURCHASES_REQ)
    public void interceptPurchaseLoadingReq(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_PING_RESP)
    public void interceptPingResp(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_PING_REQ)
    public void interceptPingReq(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_LIKE_WORLD_REQ)
    public void interceptLikeWorld(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_DISLIKE_WORLD_REQ)
    public void interceptDislikeWorld(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_UPDATE_PROFILE_REQ)
    public void interceptUserProfileUpdateReq(Packet packet, Player player, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_UPDATE_PROFILE_RES)
    public void interceptUserProfileUpdateResp(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.SB_PLAYER_UPDATED)
    public void interceptPlayerUpdateInfo(Packet packet, PlayerInfo playerInfo, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.SB_PLAYER_JOINED_ROOM)
    public void interceptPlayerJoinedInfo(Packet packet, Player player, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_PLAYERS_INFO)
    public void interceptPlayerList(Packet packet, List<Player> players, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_JOIN_ROOM_RESP)
    public void interceptJoinRoomResp(Packet packet, JoinRoomResponse joinRoomResponse, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_POPUP_MESSAGE)
    public void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_JOIN_ROOM_REQ)
    public void interceptJoinRoomReq(Packet packet, JoinRoomRequest joinRoomRequest, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_DISCONNECTED)
    public void interceptPlayerDisconnect(Packet packet, int playerId, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PLAYER_GRAPHICS_INITED_REQ)
    public void interceptGraphicsInitializationReq(Packet packet, MovementPacket initPos, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_GRAPHICS_INITED_RESP)
    public void interceptGraphicsInitializationResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_PLAYER_ACTION_RESP)
    public void interceptPlayerActionResp(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_PLAYER_ACTION_REQ)
    public void interceptPlayerActionReq(Packet packet, PlayerAction playerAction, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_SET_BLOCK_TYPE_RESP)
    public void interceptPlaceBlockResp(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_PLAYER_MOVE_RESP)
    public void interceptPlayerPositionResp(Packet packet, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_LOGIN_RESP)
    public void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_CHECK_VERSION_RESP)
    public void interceptVersionCheckResponse(Packet packet, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_CHECK_VERSION_REQ)
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_LOGIN_REQ)
    public void interceptLogin(Packet packet, LoginInfo data, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_SET_BLOCK_TYPE)
    public void interceptServerPlaceBlock(Packet packet, Block data, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.C_SET_BLOCK_TYPE_REQ)
    public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_MODIFIED_BLOCKS)
    public void interceptServerBlocks(Packet packet, ServerBlockData data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_PLAYER_MOVE_REQ)
    public void interceptPlayerPositionReq(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ENEMY_MOVE)
    public void interceptEnemyPosition(Packet packet, MovementPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ENEMY_ACTION)
    public void interceptEnemyAction(Packet packet, PlayerAction data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.SB_CHAT_MSG)
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
    }

    @PacketHook(PacketCommand.S_VALIDATE_PURCHASE_RES)
    public void interceptPurchaseValidationResponse(Packet packet, PurchaseValidationResp data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_CHAT_MSG)
    public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_VALIDATE_PURCHASE_REQ)
    public void interceptPurchaseValidationRequest(Packet packet, PurchaseValidationReq data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.S_ROOM_LIST_RESP)
    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {

    }

    @PacketHook(PacketCommand.C_ROOM_LIST_REQ)
    public void interceptRoomsReq(Packet packet, RoomListRequest data, PacketsFormula formula) {

    }

    public void interceptUnknownPacket(Packet packet, PacketsFormula formula) {

    }
}
