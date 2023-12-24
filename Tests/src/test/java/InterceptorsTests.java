import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftSocket;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.server.socket.SocketPacketSender;
import com.mikolka9144.worldcraft.backend.server.socket.WorldcraftThread;
import com.mikolka9144.worldcraft.backend.server.socket.interceptor.PacketAlteringModule;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Slf4j
public class InterceptorsTests {

    static class TestInterceptor extends PacketAlteringModule{
        private final Function<Packet,PacketsFormula>  test;
        public TestInterceptor(Function<Packet,PacketsFormula> command){this.test = command;}
        @Override
        public PacketsFormula interceptRawPacket(Packet packet) {
            return test.apply(packet);
        }
    }
    @Test
    public void Interceptors_testPacketSend() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN,0,
                PacketCommand.CLIENT_LOGIN_REQ,"",(byte)0,new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new WorldcraftPacketIO(new ByteArrayInputStream(new byte[0]),tempOut).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();
        WorldcraftSocket socket = new WorldcraftSocket(input,output,"XD",output);

        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setMsg("test");
            formula.addUpstream(s);
            return formula;
        }));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            //assert
            assertThat(s.getMsg()).isEqualTo("test");

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
                PacketCommand.CLIENT_LOGIN_REQ,"",(byte)0,new byte[0]);
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
        assertThatCode(() -> thread.sendPacket(packet)).doesNotThrowAnyException();
    }
    @Test
    public void Interceptors_testPacketLoopback() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN,0,
                PacketCommand.CLIENT_LOGIN_REQ,"",(byte)0,new byte[0]);


        List<PacketAlteringModule> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setMsg("test");
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
            assertThat(s.getMsg()).isEqualTo("test");
            assertThat(s.getPlayerId()).isEqualTo(15);
            executed.set(true);
            return new PacketsFormula();
        }));

        // act
        SocketPacketSender thread = new SocketPacketSender(upstreamInterceptors,downstreamInterceptors,null);
        thread.sendToServer(packet);
        // assert
        assertThat(executed).isTrue();
    }

}
