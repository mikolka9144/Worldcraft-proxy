package com.mikolka9144.worldcraft.backend.base.api;


import com.mikolka9144.packet.packet.Packet;
import com.mikolka9144.packet.packet.encodings.PacketDataDecoder;
import com.mikolka9144.packet.packet.encodings.PacketHook;
import com.mikolka9144.worldcraft.backend.base.socket.PacketCommands;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
@Slf4j
public class PacketCommandResolver {
    private final PacketCommands commands;

    public PacketCommandResolver(PacketCommands targetObject){

        this.commands = targetObject;
    }

    public void executeCommand(PacketCommand target, Packet packet, PacketsFormula formula) {
        Method[] declaredCalls = commands.getClass().getDeclaredMethods();
        Method[] allCalls = PacketCommands.class.getDeclaredMethods();
        Optional<Method> baseMethod = findTargetInMethods(target, allCalls);
        String callName = baseMethod.map(Method::getName).orElse("interceptUnknownPacket");
        Optional<Method> call = findTargetInMethods(callName,declaredCalls);

        call.ifPresent(
                x -> {
                    Object argument = null;
                    try {
                        argument = getPacketData(packet.getCommand(), packet.getData());
                        if (argument != null) x.invoke(commands, packet, argument, formula);
                        else x.invoke(commands, packet, formula);
                    } catch (Exception exp) {
                        log.error(String.format("Method execution failed for %s with argument %s",target,argument), exp);
                    }
                }
        );
    }

    private Object getPacketData(PacketCommand target, byte[] data) {
        Method[] encoders = PacketDataDecoder.class.getMethods();
        Optional<Method> call = findTargetInMethods(target, encoders);
        if (call.isPresent()) {
            try {
                @SuppressWarnings("UnnecessaryLocalVariable")
                Object payload = data;
                return call.get().invoke(null, payload);
            } catch (Exception exp) {
                log.error("Method execution failed for following method: " + target, exp);
            }
        }
        return null;
    }

    private static Optional<Method> findTargetInMethods(PacketCommand target, Method[] pool) {
        return Arrays.stream(pool)
                .filter(s -> s.getAnnotation(PacketHook.class) != null)
                .filter(s -> s.getAnnotation(PacketHook.class).value().equals(target)).findFirst();
    }
    private static Optional<Method> findTargetInMethods(String target, Method[] pool) {
        return Arrays.stream(pool)
                .filter(s -> s.getName().equals(target)).findFirst();
    }
}
