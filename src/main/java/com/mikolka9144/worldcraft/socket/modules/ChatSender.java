package com.mikolka9144.worldcraft.socket.modules;

import com.mikolka9144.worldcraft.socket.logic.SocketPacketSender;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.FullPacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Component("chat-sender")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChatSender extends FullPacketInterceptor {
    private static Optional<Thread> consoleChatThread = Optional.empty();
    private static SocketPacketSender clientPipeline;
    @Override
    public void setupSockets(SocketPacketSender io) {
        this.clientPipeline = io;
        log.info("Refreshed interceptors for the console");
        if(consoleChatThread.isEmpty()){
            consoleChatThread = Optional.of(new Thread(() -> {
                Scanner input = new Scanner(System.in);
                while (true){
                    String line = input.nextLine();
                    Packet packetWithMessage = packager.writeln(line);
                    clientPipeline.sendToServer(packetWithMessage);

                }
            }));
            consoleChatThread.get().start();
            log.info("Opening up new console");
        }

    }
}
