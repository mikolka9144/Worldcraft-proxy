import com.mikolka9144.worldcraft.socket.logic.WorldcraftPacketIO;
import com.mikolka9144.worldcraft.socket.logic.WorldcraftThreadHandler;
import com.mikolka9144.worldcraft.socket.model.Interceptors.PacketAlteringModule;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.Packet.WorldcraftSocket;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class InterceptorsTests extends WorldcraftThreadHandler {

    class TestInterceptor extends PacketAlteringModule{
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
        handleClientSocket(socket,upstreamInterceptors,downstreamInterceptors);
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
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            return formula;
        }));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            throw new RuntimeException("This shouldn't be executed");
        }));

        List<PacketAlteringModule> downstreamInterceptors = new ArrayList<>();


        // act
        socket.getChannel().send(packet);
        handleClientSocket(socket,upstreamInterceptors,downstreamInterceptors);
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
        handleClientSocket(socket,upstreamInterceptors,downstreamInterceptors);
        // assert
        assertThat(executed).isTrue();
    }
}
