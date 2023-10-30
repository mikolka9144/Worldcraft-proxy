package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketBuilder;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketsFormula;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.Player;
import com.mikolka9144.worldcraft.socket.model.Packet.Packet;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketCommand;
import com.mikolka9144.worldcraft.socket.model.Vector3;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Monika {
    private int moveMultiplier = 1;
    @Getter
    private final Player herPlayer;
    private int playerAngle = 0;
    private final PacketBuilder packetBuilder;

    public Monika(Vector3Short bedrockPos, PacketBuilder builder){
        this.packetBuilder = builder;

        this.herPlayer = new Player();
        herPlayer.setId(2);
        herPlayer.setSkinId((short) 4);
        herPlayer.setNickname("Monika.chr");
        herPlayer.setPosition(new Vector3(bedrockPos.getX()+0.5f, bedrockPos.getY()+2f, bedrockPos.getZ()+0.5f));
        herPlayer.setAt(new Vector3(0,0,0));
        herPlayer.setUp(new Vector3(0,0,0));
    }
    public void processInput(String input, PacketsFormula formula){
        MonikaCommandReader reader = new MonikaCommandReader(splitInputCommands(input).iterator());
        try{

            while (reader.hasNext()){
                processCommand(reader,formula);
            }
        }
        catch (Exception x){
            formula.addWriteback(packetBuilder.println("Error occured: "+x.getClass().getName()));
        }
    }
    public void processCommand(MonikaCommandReader reader,PacketsFormula formula){
        switch (reader.readNext()){
            case "np":
                int steps = Integer.parseInt(reader.readNext());
                formula.addWriteback(moveMonika(calculateMovement(playerAngle-90,steps*moveMultiplier)));
                break;
            case "ws":
                int stepsBack = Integer.parseInt(reader.readNext());
                Vector3 movement = calculateMovement(playerAngle-90,stepsBack*moveMultiplier*-1f);
                formula.addWriteback(moveMonika(movement));
                break;
            case "pr":
                int rightAngle = Integer.parseInt(reader.readNext());
                formula.addWriteback(rotateMonika(rightAngle));
                break;
            case "lw":
                int leftAngle = Integer.parseInt(reader.readNext());
                formula.addWriteback(rotateMonika(-leftAngle));
                break;
        }
    }
    public Packet moveMonika(Vector3 movement){
        Vector3 newVector = Utills.addVectors(herPlayer.getPosition(),movement);
        herPlayer.setPosition(newVector);
        return packetBuilder.serverPacket(
                PacketCommand.S_ENEMY_MOVE,
                PacketContentSerializer.encodeEnemyMovementPacket
                        (new MovementPacket(herPlayer.getId(),herPlayer.getPosition(),herPlayer.getAt(),herPlayer.getUp()))
        );
    }
    public Packet rotateMonika(int reqAngle){
        playerAngle = (playerAngle+reqAngle)%360;
        herPlayer.setAt(calculateAngle(playerAngle));
        return packetBuilder.serverPacket(
                PacketCommand.S_ENEMY_MOVE,
                PacketContentSerializer.encodeEnemyMovementPacket
                        (new MovementPacket(herPlayer.getId(),herPlayer.getPosition(),herPlayer.getAt(),herPlayer.getUp()))
        );
    }
    public Vector3 calculateMovement(float roation,float lenght){
        double num = 0.017453292519944;
        double num2 = -roation + 90.0;
        double num3 = lenght * Math.sin(num * num2);
        double num4 = lenght * Math.cos(num * num2);
        return new Vector3((float) num3,0f, (float) num4);
    }
    public Vector3 calculateAngle(int angle){
        double radians = Math.toRadians(angle);
        double x = Math.sin(radians);
        double z = -Math.cos(radians);
        if(angle == 180) x = 0; // This fixes invalid value from Math.sin

        return new Vector3((float) x, 0, (float) z);
    }
    public List<String> splitInputCommands(String input){
        return Arrays.stream(input.split(" "))
                .flatMap(s -> s.startsWith("[")? Stream.of("[",s.substring(1)) : Stream.of(s))
                .flatMap(s -> s.endsWith("]")? Stream.of(s.substring(0,s.length()-1),"]") : Stream.of(s))
                .filter(s -> !s.equals(""))
                .toList();
    }
}
