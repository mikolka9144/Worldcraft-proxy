package tests;

import com.mikolka9144.worldcraft.backend.client.api.PacketsFormula;
import com.mikolka9144.worldcraft.backend.client.socket.SocketPacketIO;
import com.mikolka9144.worldcraft.backend.client.socket.WorldcraftThread;
import com.mikolka9144.worldcraft.backend.client.socket.interceptor.PacketInterceptor;
import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import com.mikolka9144.worldcraft.utills.enums.PacketProtocol;
import com.mikolka9144.worldcraft.utills.exception.WorldcraftCommunicationException;
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

    static class TestInterceptor extends PacketInterceptor {
        private final Function<Packet, PacketsFormula> test;

        public TestInterceptor(Function<Packet, PacketsFormula> command) {
            this.test = command;
        }

        @Override
        public PacketsFormula interceptRawPacket(Packet packet) {
            return test.apply(packet);
        }
    }

    @Test
    public void Interceptors_testPacketSend() throws IOException, WorldcraftCommunicationException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN, 0,
                PacketCommand.CLIENT_LOGIN_REQ, "", (byte) 0, new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new SocketPacketIO(new ByteArrayInputStream(new byte[0]), tempOut,() -> {}).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();
        SocketPacketIO socket = new SocketPacketIO(input,output,() -> {});

        List<PacketInterceptor> upstreamInterceptors = new ArrayList<>();
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
        // act
        socket.send(packet);
        WorldcraftThread thread = new WorldcraftThread( upstreamInterceptors,s ->{});
        thread.sendClientPacket(packet);
    }

    @Test
    public void Interceptors_testPacketRemove() throws IOException, WorldcraftCommunicationException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN, 0,
                PacketCommand.CLIENT_LOGIN_REQ, "", (byte) 0, new byte[0]);
        var tempOut = new ByteArrayOutputStream();
        new SocketPacketIO(new ByteArrayInputStream(new byte[0]), tempOut,() -> {}).send(packet);
        byte[] rawInput = tempOut.toByteArray();

        InputStream input = new ByteArrayInputStream(rawInput);
        OutputStream output = new ByteArrayOutputStream();

        List<PacketInterceptor> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> new PacketsFormula()));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            throw new RuntimeException("This shouldn't be executed");
        }));
        var socket = new SocketPacketIO(input,output,() -> {});

        // act
        socket.send(packet);
        WorldcraftThread thread = new WorldcraftThread( upstreamInterceptors, s -> {});
        assertThatCode(() -> thread.sendClientPacket(packet)).doesNotThrowAnyException();
    }

    @Test
    public void Interceptors_testPacketLoopback() throws IOException {
        //given
        //TODO I bet Monika wouldn't be happy with this goofy ahh code (or anyone)
        var packet = new Packet(PacketProtocol.UNKNOWN, 0,
                PacketCommand.CLIENT_LOGIN_REQ, "", (byte) 0, new byte[0]);


        List<PacketInterceptor> upstreamInterceptors = new ArrayList<>();
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            s.setMsg("test");
            formula.addUpstream(s);
            return formula;
        }));
        upstreamInterceptors.add(new TestInterceptor((s) -> {
            PacketsFormula formula = new PacketsFormula();
            if((s.getPlayerId() == 15)) formula.addUpstream(s);
            else formula.addWriteback(s);
            s.setPlayerId(15);
            return formula;
        }));

        AtomicBoolean executed = new AtomicBoolean(false);

        // act
        WorldcraftThread thread = new WorldcraftThread(upstreamInterceptors, s ->{
            assertThat(s.getMsg()).isEqualTo("test");
            assertThat(s.getPlayerId()).isEqualTo(15);
            executed.set(true);
        });
        thread.sendClientPacket(packet);
        // assert
        assertThat(executed).isTrue();
    }

}
