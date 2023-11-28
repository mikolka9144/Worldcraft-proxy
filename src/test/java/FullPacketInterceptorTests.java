import com.mikolka9144.worldcraft.socket.Packet.Packet;
import com.mikolka9144.worldcraft.socket.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.Packet.packetParsers.PacketDataEncoder;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketBuilder;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullPacketInterceptorTests {
    public class TestFullInterceptor extends CommandPacketInterceptor{
        @Override
        public void interceptPlayerMessage(Packet packet, String message, PacketsFormula formula) {
            assertEquals("test",message);
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
            module.InterceptRawPacket(packet);
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
        module.InterceptRawPacket(packet);
        // assert

    }
}
