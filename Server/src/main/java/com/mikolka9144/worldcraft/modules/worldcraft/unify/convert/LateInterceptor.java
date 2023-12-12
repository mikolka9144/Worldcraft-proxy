package com.mikolka9144.worldcraft.modules.worldcraft.unify.convert;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.build.PacketDataBuilder;
import com.mikolka9144.packet.packet.codecs.ChatMessage;
import com.mikolka9144.packet.packet.codecs.LoginResponse;
import com.mikolka9144.packet.packet.codecs.PopupMessage;
import com.mikolka9144.packet.packet.codecs.RoomsPacket;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.packet.packet.enums.PacketProtocol;
import com.mikolka9144.worldcraft.modules.worldcraft.unify.backend.VersionFlags;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("packet-conv-late")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LateInterceptor extends CommandPacketInterceptor {
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
