import com.mikolka9144.worldcraft.backend.client.api.PacketBuilder;
import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.codecs.ChatMessage;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;

public class FullPacketInterceptorTests {
    public static class TestFullInterceptor extends CommandPacketInterceptor {
        @Override
        public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
            Assertions.assertThat("test").isEqualTo(message);
        }

        @Override
        public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
            super.interceptChatMessage(packet, data, formula);
        }
    }
    @Test
    public void onNormalPacket_testDataExtraction_ShouldExecuteMethod() throws IOException {
        //given
        CommandPacketInterceptor module = new TestFullInterceptor();
        PacketBuilder builder = new PacketBuilder(PacketProtocol.UNKNOWN);
        Packet packet = builder.serverPacket(PacketCommand.C_CHAT_MSG, PacketDataEncoder.playerMessage("test"));
        // act
            module.interceptRawPacket(packet);
        // assert

    }
    @Test
    public void onReduntantPacket_testPacketIgnoring_ShouldIgnorePacket() throws IOException {
        //given
        CommandPacketInterceptor module = new TestFullInterceptor();
        PacketBuilder builder = new PacketBuilder(PacketProtocol.UNKNOWN);
        // That's invalid packet
        Packet packet = builder.serverPacket(PacketCommand.S_ROOM_LIST_RESP, PacketDataEncoder.playerMessage("test"));

        // act
        module.interceptRawPacket(packet);
        // assert

    }
}
