package com.mikolka9144.worldcraft.interceptors.unify.convert;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.packets.codecs.ClientBuildManifest;
import com.mikolka9144.worldcraft.backend.packets.codecs.LoginInfo;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.FullPacketInterceptor;
import com.mikolka9144.worldcraft.interceptors.unify.backend.PacketConverter;
import com.mikolka9144.worldcraft.interceptors.unify.backend.VersionFlags;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("packetConvEarly")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EarlyInterceptor extends FullPacketInterceptor {
    PacketProtocol newProto = null;

    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if (packet.getCommand() == PacketCommand.CLIENT_LOGIN_REQ) {
            LoginInfo ret = PacketConverter.prepareLegacyLoginInfo(packet);

            if (packet.getProtoId() == PacketProtocol.LEGACY_VERSION) {
                newProto = ret.getClientVer().equals("2.7.4")
                        ? PacketProtocol.WORLDCRAFT_2_7_4
                        : PacketProtocol.WORLDCRAFT_2_8_7;
                packet.setProtoId(newProto);
            }

            VersionFlags flags = VersionFlags.getFlags(packet.getProtoId());
            if (!flags.simplifyLogin()) return super.interceptRawPacket(packet);

            packet.setData(PacketDataEncoder.login(ret));
        }
        if (newProto != null && packet.getProtoId() != PacketProtocol.SERVER) packet.setProtoId(newProto);
        return super.interceptRawPacket(packet);
    }

    @Override
    public void interceptVersionCheckReq(Packet packet, ClientBuildManifest clientBuildManifest, PacketsFormula formula) {
        if (clientBuildManifest.getClientVersion() == 2) {
            newProto = PacketProtocol.WORLD_OF_CRAFT_1_2;
            packet.setProtoId(newProto);
        }
    }

    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        if (data.getType() != ChatMessage.MsgType.STANDARD) return;
        String[] parts = data.getMessage().split(">", 2);
        data.setPlayerNicknameArg(parts[0]);
        data.setMessage(parts[1]);
        packet.setData(PacketDataEncoder.chatMessage(data));
    }
}
