package com.mikolka9144.worldcraft.interceptors.unify.convert;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.packets.codecs.LoginResponse;
import com.mikolka9144.worldcraft.backend.packets.codecs.PopupMessage;
import com.mikolka9144.worldcraft.backend.packets.codecs.RoomsPacket;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.FullPacketInterceptor;
import com.mikolka9144.worldcraft.interceptors.unify.backend.PacketConverter;
import com.mikolka9144.worldcraft.interceptors.unify.backend.VersionFlags;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("packetConvLate")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LateInterceptor extends FullPacketInterceptor {
    private VersionFlags flags;


    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if (packet.getProtoId() != PacketProtocol.SERVER && flags == null) {
            flags = VersionFlags.getFlags(packet.getProtoId());
        }
        return super.interceptRawPacket(packet);
    }

    @Override
    public void interceptLoginResp(Packet packet, LoginResponse loginResponse, PacketsFormula formula) {
        if (!flags.simplifyLogin()) return;
        packet.setData(
                new PacketDataBuilder()
                        .append(loginResponse.getPlayerId())
                        .append(loginResponse.getPlayerName())
                        .build()
        );
    }

    @Override
    public void interceptRoomsResp(Packet packet, RoomsPacket data, PacketsFormula formula) {
        if (flags.shoudIncludeReadOlnyRoomStatus()) return;
        packet.setData(PacketConverter.encodeLegacyRooms(data));
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        if (data.getType() != ChatMessage.MsgType.STANDARD) {
            if (flags.simplifyChat()) {
                packet.setData(data.getMessage().getBytes());
            }
            return;
        }
        if (flags.simplifyChat()) {
            String playerPrefix = data.getPlayerNicknameArg().isEmpty() ? "" : data.getPlayerNicknameArg() + ">";
            String colorlessMessage = data.getMessage().replaceAll("\\[[0-9A-F]{6}\\]", "");
            packet.setData((playerPrefix + colorlessMessage).getBytes());
            return;
        }
        String playerPrefix = data.getPlayerNicknameArg().isEmpty() ? ">" : data.getPlayerNicknameArg() + ">";
        data.setMessage(playerPrefix + data.getMessage());
        packet.setData(PacketDataEncoder.chatMessage(data));
    }

    @Override
    public void interceptPopupMessage(Packet packet, PopupMessage popupMessage, PacketsFormula formula) {
        if (!flags.simplifyAlerts()) return;
        packet.setData(popupMessage.getBaseMessage().getBytes());
    }

}
