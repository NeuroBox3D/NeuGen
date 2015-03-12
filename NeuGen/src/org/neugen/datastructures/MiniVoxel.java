/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
 * 
 * This file is part of NeuGen.
 *
 * NeuGen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *      file://path/to/NeuGen/LICENSE
 *
 * NeuGen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of NeuGen includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of NeuGen. The copyright statement/attribution may not be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do the following regarding copyright
 * notice and author attribution.
 *
 * Add an additional notice, stating that you modified NeuGen. In addition
 * you must cite the publications listed below. A suitable notice might read
 * "NeuGen source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 * Publications:
 *
 * S. Wolf, S. Grein, G. Queisser. NeuGen 2.0 -
 * Employing NeuGen 2.0 to automatically generate realistic
 * morphologies of hippocapal neurons and neural networks in 3D.
 * Neuroinformatics, 2013, 11(2), pp. 137-148, doi: 10.1007/s12021-012-9170-1
 *
 *
 * J. P. Eberhard, A. Wanner, G. Wittum. NeuGen -
 * A tool for the generation of realistic morphology 
 * of cortical neurons and neural networks in 3D.
 * Neurocomputing, 70(1-3), pp. 327-343, doi: 10.1016/j.neucom.2006.01.028
 *
 */
/*
 * MiniVoxel.java
 *
 * Created on April 17, 2007
 *
 * Class for a data structure containing a voxels value and coordinates (and slice number)
 */
package org.neugen.datastructures;

// miniVoxelArrayList contains the MiniVoxels to be visualized
import org.apache.log4j.Logger;

// ArrayList<MiniVoxel> miniVoxelArrayList = new ArrayList<MiniVoxel>();
// Test for convex hull algorithm (dice with additional point)
// float a = 0.0f;
// float b = 2.0f;
// float c = 1.0f;
// float d = 4.0f;
// dice
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, a, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, a, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, b, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, a, b));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, b, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, b, b));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, a, b));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, b, b));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, c, c, c));
// cube
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, a, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, a, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, c, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, c, a));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, a, c));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, a, c));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, b, c, c));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, a, c, c));
// miniVoxelArrayList.add(new MiniVoxel(1.0f, d, d, d));
// private void computeConvexHull(ArrayList<MiniVoxel>
// miniVoxelArrayList, Triangle3dCreator triangle3dCreator,
// Line3dCreator line3dCreator, Cube3dCreator cubeCreator)
/**
 * Class MiniVoxel
 * 
 * @author Simone Eberhard
 */
public class MiniVoxel implements Comparable {

    /** Use to log messages. */
    private static Logger logger = Logger.getLogger(MiniVoxel.class.getName());
    /**
     * the value of the MiniVoxel can be a (percentage of a) length or
     * volume of a net part (dendrite or axon) or a (percentage of a) number
     * of a net part (synapses)
     */
    private float value;
    /** The x coordinate of the MiniVoxel. */
    private double xCoord;
    /** The y coordinate of the MiniVoxel. */
    private double yCoord;
    /** The z coordinate of the MiniVoxel. */
    private double zCoord;
    /** The number of the slice of a volume in which a MiniVoxel lies usually the int value of the z coordinate. */
    private int sliceNumber;

    /**
     * Creates a new instance of MiniVoxel
     *
     * @param value
     *                the value of the MiniVoxel can be a (percentage of a)
     *                length or volume of a net part (dendrite or axon) or a
     *                (percentage of a) number of a net part (synapses)
     * @param xCoord
     *                the x coordinate of the MiniVoxel
     * @param yCoord
     *                the y coordinate of the MiniVoxel
     * @param zCoord
     *                the z coordinate of the MiniVoxel
     */
    public MiniVoxel(float value, double xCoord, double yCoord, double zCoord) {
        this.value = value;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
    }

    /**
     * Creates a new instance of MiniVoxel
     *
     * @param value
     *                the value of the MiniVoxel can be a (percentage of a)
     *                length or volume of a net part (dendrite or axon) or a
     *                (percentage of a) number of a net part (synapses)
     * @param xCoord
     *                the x coordinate of the MiniVoxel
     * @param yCoord
     *                the y coordinate of the MiniVoxel
     * @param zCoord
     *                the z coordinate of the MiniVoxel
     * @param sliceNumber
     *                the number of the slice of a volume in which a
     *                MiniVoxel lies usually the z coordinate
     */
    public MiniVoxel(float value, double xCoord, double yCoord, double zCoord, int sliceNumber) {
        this.value = value;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.sliceNumber = sliceNumber;
    }

    /**
     * Prints data of the MiniVoxel: coordinates, (sliceNumber) and value.
     */
    public void printData() {
        logger.info("x: " + this.xCoord + " y: " + this.yCoord + " z: " + this.zCoord);
        try {
            logger.info("slice number:" + this.sliceNumber);
        } catch (Exception e) {
            logger.error(e, e);
        }
        logger.info("value: " + this.value);
    }

    /**
     * Sets the value of the MiniVoxel.
     *
     * @param value
     *                the value of the MiniVoxel can be a (percentage of a)
     *                length or volume of a net part (dendrite or axon) or a
     *                (percentage of a) number of a net part (synapses)
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Sets the x coordinate of the MiniVoxel.
     *
     * @param xCoord
     *                the x coordinate of the MiniVoxel
     */
    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * Sets the y coordinate of the MiniVoxel.
     *
     * @param yCoord
     *                the y coordinate of the MiniVoxel
     */
    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
    }

    /**
     * Sets the z coordinate of the MiniVoxel.
     *
     * @param zCoord
     *                the z coordinate of the MiniVoxel
     */
    public void setZCoord(double zCoord) {
        this.zCoord = zCoord;
    }

    /**
     * Gets the value coordinate of the MiniVoxel.
     *
     * @return the value of the MiniVoxel can be a (percentage of a) length
     *         or volume of a net part (dendrite or axon) or a (percentage
     *         of a) number of a net part (synapses)
     */
    public float getValue() {
        return value;
    }

    /**
     * Gets the x coordinate of the MiniVoxel.
     *
     * @return the x coordinate of the MiniVoxel
     */
    public double getXCoord() {
        return xCoord;
    }

    /**
     * Gets the y coordinate of the MiniVoxel.
     *
     * @return the y coordinate of the MiniVoxel
     */
    public double getYCoord() {
        return yCoord;
    }

    /**
     * Sets the z coordinate of the MiniVoxel.
     *
     * @return the z coordinate of the MiniVoxel
     */
    public double getZCoord() {
        return zCoord;
    }

    /**
     * Sets the slice number of the MiniVoxel.
     *
     * @return the slice number of the MiniVoxel usually the int value of
     *         the z coordinate of the MiniVoxel
     */
    public int getSliceNumber() {
        return sliceNumber;
    }

    /**
     * compare the value of this MiniVoxel with the value of another
     * MiniVoxel
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param otherMiniVoxel
     *                the MiniVoxel to compare to
     * @return -1, if this this.value < otherMiniVoxel.value, 0, if
     *         this.value == otherMiniVoxel.value, 1, if this.value >
     *         otherMiniVoxel.value
     */
    @Override
    public int compareTo(Object otherMiniVoxel) {
        if (this.value < ((MiniVoxel) otherMiniVoxel).value) {
            return -1;
        }
        if (this.value == ((MiniVoxel) otherMiniVoxel).value) {
            return 0;
        }
        if (this.value > ((MiniVoxel) otherMiniVoxel).value) {
            return 1;
        }
        return 42;
    }
}
