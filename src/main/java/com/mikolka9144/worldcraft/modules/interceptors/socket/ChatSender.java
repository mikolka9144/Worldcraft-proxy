package com.mikolka9144.worldcraft.modules.interceptors.socket;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.ChatMessage;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PopupMessage;
import com.mikolka9144.worldcraft.socket.model.Interceptors.CommandPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component("chat-sender")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChatSender extends CommandPacketInterceptor {
    private static Optional<Thread> consoleChatThread = Optional.empty();
    private static SocketPacketSender clientPipeline;
    private static OutputStream output;
    private boolean showedWarning = true;
    @Override
    public void setupSockets(SocketPacketSender io) {
        clientPipeline = io;
        log.info("Refreshed interceptors for the console");
        if(consoleChatThread.isEmpty()){
            consoleChatThread = Optional.of(new Thread(() -> {

                try(ServerSocket server = new ServerSocket(10000)) {
                    Socket nc = server.accept();
                    var input = nc.getInputStream();
                    output = nc.getOutputStream();
                    while (true){
                        var header = input.readNBytes(1);
                        var rest = input.readNBytes(input.available());
                        var builder = new ByteArrayBuilder();
                        builder.write(header);
                        builder.write(rest);
                        var byteInput = builder.toByteArray();
                        String line = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(byteInput)).toString();
                        line = line.replace("\n","");
                        log.info(line);
                        Packet packetWithMessage = packager.writeln(line);
                        clientPipeline.sendToServer(packetWithMessage);
                    }
                } catch (IOException e) {
                    log.error("", new RuntimeException(e));
                }
            }));
            consoleChatThread.get().start();
            log.info("Opening up new console");
        }

    }

    @SneakyThrows
    @Override
    public void interceptChatMessage(Packet packet, ChatMessage data, PacketsFormula formula) {
        try {
            output.write((data.getMessage()+"\n").getBytes());
        }
        catch (NullPointerException ignore){}
        //formula.getUpstreamPackets().remove(packet);
    }

    @Override
    public void interceptGraphicsInitializationResp(Packet packet, PacketsFormula formula) {
        if (!showedWarning){
            Packet message = new Packet(PacketProtocol.SERVER,0, PacketCommand.S_POPUP_MESSAGE,"", (byte) 0,
                    PacketContentSerializer.encodePopupMessage(
                            new PopupMessage(
                                PopupMessage.PopupMessageType.NONE,0,"",
                                    "You have a terminal connection connected to your device. Chat is located at port 10000 of your server." +
                                            " CHAT WILL NOT BE VISIBLE HERE!!!"    )
                            ));
            formula.addUpstream(message);
            showedWarning = true;
        }
    }
}
