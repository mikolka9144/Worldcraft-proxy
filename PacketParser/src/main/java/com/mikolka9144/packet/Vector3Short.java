package com.mikolka9144.packet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

    private short x;

    private short y;
    private short z;
}
