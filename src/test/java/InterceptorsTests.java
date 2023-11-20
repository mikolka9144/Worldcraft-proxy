import com.mikolka9144.worldcraft.modules.interceptors.socket.ChatCommandsInterceptor;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketBuilder;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftThread;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
@Slf4j
public class InterceptorsTests {

    static class TestInterceptor extends PacketAlteringModule{
        private final Function<Packet,PacketsFormula>  test;
        public TestInterceptor(Function<Packet,PacketsFormula> command){this.test = command;}
        @Override
        public PacketsFormula InterceptRawPacket(Packet packet) {
            return test.apply(packet);
        }
    }
    @Test
    public void Interceptors_testPacketSend() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN,0,
                PacketCommand.C_LOGIN_REQ,"",(byte)0,new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new WorldcraftPacketIO(new ByteArrayInputStream(new byte[0]),tempOut).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();
        WorldcraftSocket socket = new WorldcraftSocket(input,output,"XD",output);

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setMessage("test");
            formula.addUpstream(s);
            return formula;
        }));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            //assert
            assertThat(s.getMessage()).isEqualTo("test");

            formula.addUpstream(s);
            return formula;
        }));

        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();


        // act
        socket.getChannel().send(packet);
        WorldcraftThread thread = new WorldcraftThread(socket,upstreamInterceptors,downstreamInterceptors);
        thread.sendPacket(packet);
    }
    @Test
    public void Interceptors_testPacketRemove() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN,0,
                PacketCommand.C_LOGIN_REQ,"",(byte)0,new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new WorldcraftPacketIO(new ByteArrayInputStream(new byte[0]),tempOut).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();
        WorldcraftSocket socket = new WorldcraftSocket(input,output,"XD",output);

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> new PacketsFormula()));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            throw new RuntimeException("This shouldn't be executed");
        }));

        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();


        // act
        socket.getChannel().send(packet);
        WorldcraftThread thread = new WorldcraftThread(socket,upstreamInterceptors,downstreamInterceptors);
        thread.sendPacket(packet);
    }
    @Test
    public void Interceptors_testPacketLoopback() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN,0,
                PacketCommand.C_LOGIN_REQ,"",(byte)0,new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new WorldcraftPacketIO(new ByteArrayInputStream(new byte[0]),tempOut).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();
        WorldcraftSocket socket = new WorldcraftSocket(input,output,"XD",output);

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setMessage("test");
            formula.addUpstream(s);
            return formula;
        }));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setPlayerId(15);
            formula.addWriteback(s);
            return formula;
        }));

        AtomicBoolean executed = new AtomicBoolean(false);
        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();
        downstreamInterceptors.add(new TestInterceptor((s) -> {
            assertThat(s.getMessage()).isEqualTo("test");
            assertThat(s.getPlayerId()).isEqualTo(15);
            executed.set(true);
            return new PacketsFormula();
        }));

        // act
        socket.getChannel().send(packet);
        WorldcraftThread thread = new WorldcraftThread(socket,upstreamInterceptors,downstreamInterceptors);
        thread.handleThread();
        // assert
        assertThat(executed).isTrue();
    }
    @Test
    public void Interceptors_testcommands() throws IOException {
        //given


        PacketBuilder builder = new PacketBuilder(PacketProtocol.SERVER);
        builder.setPlayerId(10);
        var block = new BlockData(
                        new Vector3Short((short) 100, (short) 50, (short) 12),
                        BlockData.BlockType.GRASS_ID,
                        (byte) 0,
                        (byte) 0,
                        (byte) 0
                );

        Packet commandSetBlock = builder.writeln("/setpointer 10 0");
        Packet setBlock = new Packet(PacketProtocol.SERVER, 10,
                PacketCommand.C_SET_BLOCK_TYPE_REQ, "", (byte) 0, PacketContentSerializer.encodeServerPlaceBlock(block));

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();

        ChatCommandsInterceptor commands = new ChatCommandsInterceptor();
        upstreamInterceptors.add(commands);
        downstreamInterceptors.add(commands);

        // tests
        upstreamInterceptors.add(new CommandPacketInterceptor() {
            @Override
            public void interceptPlaceBlockReq(Packet packet, BlockData data, PacketsFormula formula) {
                assertThat(data.getY()).isEqualTo(block.getY());
                assertThat(data.getX()).isEqualTo(block.getX());
                assertThat(data.getZ()).isEqualTo(block.getZ());
                assertThat(data.getBlockType()).isEqualTo(BlockData.BlockType.LAVA_ID);
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
            public void interceptServerPlaceBlock(Packet packet, BlockData data, PacketsFormula formula) {
                assertThat(data.getY()).isEqualTo(block.getY());
                assertThat(data.getX()).isEqualTo(block.getX());
                assertThat(data.getZ()).isEqualTo(block.getZ());
                assertThat(data.getChunkX()).isEqualTo(block.getChunkX());
                assertThat(data.getChunkZ()).isEqualTo(block.getChunkZ());
                assertThat(data.getBlockType()).isEqualTo(BlockData.BlockType.LAVA_ID);
            }
        });

        // act
        WorldcraftThread.sendPacket(commandSetBlock,upstreamInterceptors,downstreamInterceptors);
        WorldcraftThread.sendPacket(setBlock,upstreamInterceptors,downstreamInterceptors);
    }
}
