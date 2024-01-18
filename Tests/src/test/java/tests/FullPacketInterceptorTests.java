package tests;

import com.mikolka9144.worldcraft.backend.client.api.PacketBuilder;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.FullPacketInterceptor;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.Getter;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FullPacketInterceptorTests {
    @Getter
    public static class TestFullInterceptor extends FullPacketInterceptor {
        private String msg;

        @Override
        public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
            msg = message;
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
            super.interceptChatMessage(packet, data, formula);
        }
    }

    @Test
    public void onNormalPacket_testDataExtraction_ShouldExecuteMethod() throws IOException {
        //given
        TestFullInterceptor module = new TestFullInterceptor();
        PacketBuilder builder = new PacketBuilder(PacketProtocol.UNKNOWN);
        Packet packet = builder.serverPacket(PacketCommand.CLIENT_SPEAK, PacketDataEncoder.playerMessage("test"));
        // act
        module.interceptRawPacket(packet);
        // assert
        assertThat(module.msg).isEqualTo("test");
    }

    @Test
    public void onReduntantPacket_testPacketIgnoring_ShouldIgnorePacket() throws IOException {
        //given
        TestFullInterceptor module = new TestFullInterceptor();
        PacketBuilder builder = new PacketBuilder(PacketProtocol.UNKNOWN);
        // That's invalid packet
        Packet packet = builder.serverPacket(PacketCommand.SERVER_ROOM_LIST_RESP, PacketDataEncoder.playerMessage("test"));

        // act
        module.interceptRawPacket(packet);
        // assert
        assertThat(module.msg).isNotEqualTo("test");
    }
}
