/*
 * Point3D.java
 *
 * Created on 17. Februar 2007
 *
 */
package org.neugen.simpletriangulation;

/**
 * A very simple implementation of a point in 3D space.
 * 
 * @author Jens P Eberhard
 */
public class Point3D {

    int x, y, z;

    /** Creates a new instance of Point3D */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Checks if Point3D p1 equals this
     */
    public boolean equals(Point3D p1) {
        return (x == p1.x && y == p1.y && z == p1.z);
    }

    /**
     * Checks if a Point3D p1 is a neighbour of <i>this</i>. Neighbouring
     * points are at most 1 step away in each direction.
     */
    public boolean isNeighbour(Point3D p1) {

        if (this.equals(p1)) {
            return false;
        }

        int dx = (x > p1.x) ? (x - p1.x) : (p1.x - x);
        int dy = (y > p1.y) ? (y - p1.y) : (p1.y - y);
        int dz = (z > p1.z) ? (z - p1.z) : (p1.z - z);
        return (dx <= 1 && dy <= 1 && dz <= 1);
    }

    public final void printData() {
        System.out.print("Pt3D(" + x + " " + y + " " + z + ") ");
    }
}
