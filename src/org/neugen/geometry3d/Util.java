package org.neugen.geometry3d;

import javax.vecmath.Vector3f;

public class Util {

    public Util() {
    }

    public static float getDeterminant(Vector3f v1, Vector3f v2, Vector3f v3) {
        float determinant = v1.x * v2.y * v3.z - v1.x * v3.y * v2.y - v2.x * v1.y * v3.z + v2.x * v3.y * v1.z + v3.x * v1.y * v2.z - v3.x * v2.y * v1.z;
        return determinant;
    }
}
