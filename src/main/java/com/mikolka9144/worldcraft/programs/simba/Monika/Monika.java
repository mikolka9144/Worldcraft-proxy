package com.mikolka9144.worldcraft.programs.simba.Monika;

import com.mikolka9144.worldcraft.programs.simba.Utills;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.Player;
import com.mikolka9144.worldcraft.socket.model.Vector3;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Monika {
    private final float moveMultiplier = 0.1f;
    @Getter
    private final int PLAYER_ID = 2;
    private final Player herPlayer;
    private int playerAngle = 0;
    private int playerUpwardAngle = 0;



    public Monika(){
        this.herPlayer = new Player();
        herPlayer.setId(PLAYER_ID);
        herPlayer.setSkinId((short) 4);
        herPlayer.setNickname("Monika.chr");
        herPlayer.setAt(new Vector3(0,0,0));
        herPlayer.setUp(new Vector3(0,0,0));
    }
    public Player summonMonika(Vector3Short bedrockPos){

        herPlayer.setPosition(new Vector3(bedrockPos.getX()+0.5f, bedrockPos.getY()+2f, bedrockPos.getZ()+0.5f));
        return herPlayer;
    }
    public void moveMonika(float distance){
        Vector3 upward = calculateMovement(playerUpwardAngle,-distance*moveMultiplier);
        Vector3 newPosition = calculateMovement(playerAngle,upward.getZ());
        newPosition.setY(upward.getX());

        Vector3 newVector = Utills.addVectors(herPlayer.getPosition(),newPosition);
        herPlayer.setPosition(newVector);
    }

    public void rotateMonika(int reqAngle){
        playerAngle = (playerAngle+reqAngle)%360;
        herPlayer.setAt(calculateAngle(playerAngle));
    }
    public void rotateUpMonika(int reqAngle){
        playerUpwardAngle = (playerUpwardAngle+reqAngle)%360;
        herPlayer.getAt().setY((float) Math.sin(playerUpwardAngle));
    }

    public Vector3 calculateMovement(float roation,float lenght){

        double radians = Math.toRadians(-roation);
        double Xpos = lenght * Math.sin(radians);
        double Zpos = lenght * Math.cos(radians);
        return new Vector3((float) Xpos,0f, (float) Zpos);
    }
    public Vector3 calculateAngle(int angle){
        double radians = Math.toRadians(angle);
        double x = Math.sin(radians);
        double z = -Math.cos(radians);
        if(angle == 180) x = 0; // This fixes invalid value from Math.sin

        return new Vector3((float) x, 0, (float) z);
    }
    public Vector3 getCurrentPosition(){
        return herPlayer.getPosition();
    }
    public MovementPacket getPositionData(){
        return new MovementPacket(
                herPlayer.getId(),
                herPlayer.getPosition(),
                herPlayer.getAt(),
                herPlayer.getUp()
        );
    }
}
