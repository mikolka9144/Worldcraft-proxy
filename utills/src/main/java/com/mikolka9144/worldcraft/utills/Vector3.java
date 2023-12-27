package com.mikolka9144.worldcraft.utills;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Vector3 {
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3Short vector) {
        x = vector.getX();
        y = vector.getY();
        z = vector.getZ();
    }

    private float x;

    private float y;
    private float z;

}
