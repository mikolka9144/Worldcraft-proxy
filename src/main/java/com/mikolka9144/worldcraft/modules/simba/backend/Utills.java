package com.mikolka9144.worldcraft.modules.simba.backend;

import com.mikolka9144.worldcraft.common.models.Vector3;

public class Utills {
    private Utills(){}
    public static Vector3 addVectors(Vector3 base,Vector3 diff){
        return new Vector3(base.getX()+ diff.getX(), base.getY()+ diff.getY(), base.getZ()+ diff.getZ());
    }
}
