package com.mikolka9144.worldcraft.backend.client.api;


import com.mikolka9144.worldcraft.backend.packets.Packet;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataDecoder;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketHook;
import com.mikolka9144.worldcraft.backend.client.socket.PacketCommands;
import com.mikolka9144.worldcraft.utills.enums.PacketCommand;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Component identifying packet type and redirecting
 * it to a suitable callback in {@link PacketCommands} implementation
 */
@Slf4j
public class PacketCommandResolver {
    private final PacketCommands commands;

    public PacketCommandResolver(PacketCommands targetObject){

        this.commands = targetObject;
    }

    /**
     * Analyses given packet and executes suitable callback in {@code targetObject}
     * {@link PacketsFormula} should have given packet set as upstream packet
     * @param packet A packet to analyse
     * @param formula Formula for default callback
     */
    public void executeCommand(Packet packet, PacketsFormula formula) {
        Method[] declaredCalls = commands.getClass().getDeclaredMethods();
        Method[] allCalls = PacketCommands.class.getDeclaredMethods();
        Optional<Method> baseMethod = findTargetInMethods(packet.getCommand(), allCalls);
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
                        log.error(String.format("Method execution failed for %s with argument %s",packet.getCommand(),argument), exp);
                    }
                }
        );
    }

    /**
     * Analyses given packet and executes suitable callback in {@code targetObject}.
     * Also takes care of preparing {@link PacketsFormula}
     * @param packet A packet to analyse
     * @return Result of callback execution
     */
    public PacketsFormula executeCommand(Packet packet){
        PacketsFormula formula = new PacketsFormula();
        formula.addUpstream(packet);
        executeCommand(packet,formula);
        return formula;
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
