package com.mikolka9144.worldcraft.backend.spring.unify.convert;

import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.codecs.ChatMessage;
import com.mikolka9144.packet.packet.codecs.ClientBuildManifest;
import com.mikolka9144.packet.packet.codecs.LoginInfo;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import com.mikolka9144.worldcraft.backend.spring.unify.backend.VersionFlags;
import com.mikolka9144.worldcraft.backend.base.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.spring.socket.interceptor.CommandPacketInterceptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("packet-conv-early")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EarlyInterceptor extends CommandPacketInterceptor {
    PacketProtocol newProto = null;

    @Override
    public PacketsFormula interceptRawPacket(Packet packet) {
        if (packet.getCommand() == PacketCommand.C_LOGIN_REQ) {
            LoginInfo ret = PacketConverter.pareLegacyLoginInfo(packet);

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
        if(clientBuildManifest.getClientVersion() == 2) {
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
