/*
 * To change this template; choose Tools | Templates
 * and open the template in the editor.
 */
package org.neugen.vrl;

import eu.mihosoft.vrl.annotation.ObjectInfo;
import javax.vecmath.Point3i;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
@ObjectInfo(serializeParam=false)
public class VoxelParams {

    private int x;
    private int y;
    private int z;
    private int threshold;
    private int weight;
    private float width;
    private float height;
    private float depth;

    public VoxelParams(int x, int y, int z, int threashold, int weight, float width, float height, float depth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.threshold = threashold;
        this.weight = weight;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
    
    public Point3i getVoxelLength() {
        return new Point3i(x, y, z);
    }
    

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }

    /**
     * @return the threashold
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * @return the depth
     */
    public float getDepth() {
        return depth;
    }
}
