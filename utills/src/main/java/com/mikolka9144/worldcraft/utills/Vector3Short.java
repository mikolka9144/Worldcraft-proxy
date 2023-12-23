package com.mikolka9144.worldcraft.utills;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Setter
@Getter
@EqualsAndHashCode
public class Vector3Short {
    public Vector3Short(short x, short y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3Short(int x, int y, int z) {this((short) x,(short) y,(short) z);}
    public Vector3Short(Vector3 vector3){
        this(vector3, Float::shortValue);
    }
    public Vector3Short(Vector3 vector3, Function<Float,Short> conv){
        x = conv.apply(vector3.getX());
        y = conv.apply(vector3.getY());
        z = conv.apply(vector3.getZ());
    }
    private short x;

    private short y;
    private short z;
}
