package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.model.Vector3;

public class Utills {
    public static Vector3 addVectors(Vector3 base,Vector3 diff){
        return new Vector3(base.getX()+ diff.getX(), base.getY()+ diff.getY(), base.getZ()+ diff.getZ());
    }
}
