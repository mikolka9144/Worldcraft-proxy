import com.mikolka9144.packet.Vector3Short;
import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.codecs.Block;
import com.mikolka9144.packet.packet.codecs.ChatMessage;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.packet.packet.enums.BlockType;
import com.mikolka9144.packet.packet.enums.PacketCommand;
import com.mikolka9144.packet.packet.enums.PacketProtocol;
import com.mikolka9144.worldcraft.modules.hackoring.ChatCommandsInterceptor;
import com.mikolka9144.worldcraft.socket.api.PacketBuilder;
import com.mikolka9144.worldcraft.socket.api.PacketsFormula;
import com.mikolka9144.worldcraft.socket.interceptor.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.interceptor.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.server.WorldcraftThread;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
public class CmdTests {
    @Test
    public void Interceptors_testCommands() throws IOException {
        //given


        PacketBuilder builder = new PacketBuilder(PacketProtocol.SERVER);
        builder.setPlayerId(10);
        var block = new Block(
                new Vector3Short(100, 50, 12),
                BlockType.GRASS_ID,
                (byte) 0,
                (byte) 0,
                (byte) 0
        );

        Packet commandSetBlock = builder.writeln("/setpointer 10 0");
        Packet setBlock = new Packet(PacketProtocol.SERVER, 10,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0, PacketDataEncoder.serverPlaceBlock(block));

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();

        ChatCommandsInterceptor commands = new ChatCommandsInterceptor();
        upstreamInterceptors.add(commands);
        downstreamInterceptors.add(commands);

        // tests
        upstreamInterceptors.add(new CommandPacketInterceptor() {
            @Override
            public void interceptPlaceBlockReq(Packet packet, Block data, PacketsFormula formula) {
                assertThat(data.getY()).isEqualTo(block.getY());
                assertThat(data.getX()).isEqualTo(block.getX());
                assertThat(data.getZ()).isEqualTo(block.getZ());
                assertThat(data.getBlockName()).isEqualTo(BlockType.LAVA_ID);
                assertThat(data.getChunkX()).isEqualTo(block.getChunkX());
                assertThat(data.getChunkZ()).isEqualTo(block.getChunkZ());
            }
        });
        downstreamInterceptors.add(new CommandPacketInterceptor() {
            @Override
            public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
                log.info(data.getMessage());
            }

            @Override
            public void interceptServerPlaceBlock(Packet packet, Block data, PacketsFormula formula) {
                assertThat(data.getY()).isEqualTo(block.getY());
                assertThat(data.getX()).isEqualTo(block.getX());
                assertThat(data.getZ()).isEqualTo(block.getZ());
                assertThat(data.getChunkX()).isEqualTo(block.getChunkX());
                assertThat(data.getChunkZ()).isEqualTo(block.getChunkZ());
                assertThat(data.getBlockName()).isEqualTo(BlockType.LAVA_ID);
            }
        });

        // act
        WorldcraftThread.sendPacket(commandSetBlock,upstreamInterceptors,downstreamInterceptors);
        WorldcraftThread.sendPacket(setBlock,upstreamInterceptors,downstreamInterceptors);
    }
}
