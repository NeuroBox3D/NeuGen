/*
 * VolumeOfVoxels.java
 *
 * Created on December 26, 2005
 *
 * Class VolumeOfVoxels for storing a three-dimensional volume of voxels.
 *
 */
package org.neugen.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import org.apache.log4j.Logger;

import org.neugen.geometry3d.Cube3dCreator;

/**
 * Class VolumeOfVoxels
 * 
 * @author Jens P Eberhard
 * @author Simone Eberhard
 */
public final class VolumeOfVoxels {

    /** Use to log messages. */
    private final static Logger logger = Logger.getLogger(VolumeOfVoxels.class.getName());
    /** Number of Voxels in x direction of the VolumeOfVoxels. */
    private int numberOfVoxelsX;
    /** Number of Voxels in y direction of the VolumeOfVoxels. */
    private int numberOfVoxelsY;
    /** Number of Voxels in z direction of the VolumeOfVoxels. */
    private int numberOfVoxelsZ;
    /** Total number of Voxels in the VolumeOfVoxels. */
    private int totalNumberOfVoxels;
    /** x coordinate of start point of the VolumeOfVoxels. */
    private float volumeStartX;
    /** y coordinate of start point of the VolumeOfVoxels. */
    private float volumeStartY;
    /** z coordinate of start point of the VolumeOfVoxels. */
    private float volumeStartZ;
    /** x coordinate of mid point of the VolumeOfVoxels. */
    private float midpointX;
    /** y coordinate of mid point of the VolumeOfVoxels. */
    private float midpointY;
    /** z coordinate of mid point of the VolumeOfVoxels. */
    private float midpointZ;
    /** x coordinate of end point of VolumeOfVoxels. */
    private float volumeEndX;
    /** y coordinate of end point of the VolumeOfVoxels. */
    private float volumeEndY;
    /** z coordinate of end point of the VolumeOfVoxels. */
    private float volumeEndZ;
    /**
     * array containing all Voxels of the VolumeOfVoxels, the index is given
     * by (intZ * numberOfVoxelsX * numberOfVoxelsY + intY * numberOfVoxelsX +
     * intX)
     */
    private Voxel[] voxelArray;
    /** The Voxel with the biggest value of all Voxels in the VolumeOfVoxels. */
    private Voxel voxelWithBiggestValue;
    /** radius of a sphere that would fit in the VolumeOfVoxels */
    private int numberOfVoxelsRadius;
    /** calculateMin(calculateMin(midpointX, midpointY), midpointZ) */
    private float radius;

    /** Creates a new instance of VolumeOfVoxels by a void cube of dimensions zero. */
    public VolumeOfVoxels() {
        numberOfVoxelsX = numberOfVoxelsY = numberOfVoxelsZ = 0;
        totalNumberOfVoxels = 0;
        volumeStartX = volumeStartY = volumeStartZ = 0.0f;
        volumeEndX = volumeEndY = volumeEndZ = 0.0f;
        voxelArray = new Voxel[0];
        calculateMidpointAndRadius();
    }

    /**
     * Creates a new instance of VolumeOfVoxels using the unit cube
     *
     * @param numberOfVoxelsX
     *                number of Voxels in x direction
     * @param numberOfVoxelsY
     *                number of Voxels in y direction
     * @param numberOfVoxelsZ
     *                number of Voxels in z direction
     */
    public VolumeOfVoxels(int numberOfVoxelsX, int numberOfVoxelsY, int numberOfVoxelsZ) {
        setNumberOfVoxels(numberOfVoxelsX, numberOfVoxelsY, numberOfVoxelsZ);
        volumeStartX = volumeStartY = volumeStartZ = 0.0f;
        volumeEndX = volumeEndY = volumeEndZ = 1.0f;
        setVoxelArray();
        calculateMidpointAndRadius();
    }

    /**
     * Creates a new instance of VolumeOfVoxels using a box which starts at (0,0,0)
     *
     * @param voxelNumber
     *                number of Voxels in x,y,z direction
     * @param volumeEnd
     *                x,y,z coordinate of end point of VolumeOfVoxels
     */
    public VolumeOfVoxels(Point3i voxelNumber, Point3f volumeEnd) {
        setNumberOfVoxels(voxelNumber.x, voxelNumber.y, voxelNumber.z);
        volumeStartX = volumeStartY = volumeStartZ = 0.0f;
        volumeEndX = volumeEnd.x;
        volumeEndY = volumeEnd.y;
        volumeEndZ = volumeEnd.z;
        setVoxelArray();
        calculateMidpointAndRadius();
    }

    /**
     * Creates a new instance of VolumeOfVoxels using a box with given start
     * and end points
     *
     * @param numberOfVoxelsX
     *                number of Voxels in x direction
     * @param numberOfVoxelsY
     *                number of Voxels in y direction
     * @param numberOfVoxelsZ
     *                number of Voxels in z direction
     * @param volumeStartX
     *                x coordinate of start point of VolumeOfVoxels
     * @param volumeStartY
     *                y coordinate of start point of VolumeOfVoxels
     * @param volumeStartZ
     *                z coordinate of start point of VolumeOfVoxels
     * @param volumeEndX
     *                x coordinate of end point of VolumeOfVoxels
     * @param volumeEndY
     *                y coordinate of end point of VolumeOfVoxels
     * @param volumeEndZ
     *                z coordinate of end point of VolumeOfVoxels
     */
    public VolumeOfVoxels(int numberOfVoxelsX, int numberOfVoxelsY, int numberOfVoxelsZ, float volumeStartX,
            float volumeStartY, float volumeStartZ, float volumeEndX, float volumeEndY, float volumeEndZ) {
        setNumberOfVoxels(numberOfVoxelsX, numberOfVoxelsY, numberOfVoxelsZ);
        this.volumeStartX = volumeStartX;
        this.volumeStartY = volumeStartY;
        this.volumeStartZ = volumeStartZ;
        this.volumeEndX = volumeEndX;
        this.volumeEndY = volumeEndY;
        this.volumeEndZ = volumeEndZ;
        setVoxelArray();
        calculateMidpointAndRadius();
    }

    /**
     * calculates the mid point and the radius of a sphere that would fit in
     * the volume of voxels
     */
    private void calculateMidpointAndRadius() {
        numberOfVoxelsRadius = calculateMin(calculateMin(numberOfVoxelsX, numberOfVoxelsY), numberOfVoxelsZ) / 2;
        midpointX = 0.5f * (volumeEndX - volumeStartX);
        midpointY = 0.5f * (volumeEndY - volumeStartY);
        midpointZ = 0.5f * (volumeEndZ - volumeStartZ);
        radius = calculateMin(calculateMin(midpointX, midpointY), midpointZ);
        midpointX += volumeStartX;
        midpointY += volumeStartY;
        midpointZ += volumeStartZ;
    }

    /**
     * calculates the minimum of two values
     *
     * @param v1
     *                first value to be compared
     * @param v2
     *                second value to be compared
     * @return the minimum of the two values
     */
    private float calculateMin(float v1, float v2) {
        if (v1 <= v2) {
            return v1;
        }
        return v2;
    }

    /**
     * calculates the minimum of two values
     *
     * @param v1
     *                first value to be compared
     * @param v2
     *                second value to be compared
     * @return the minimum of the two values
     */
    private int calculateMin(int v1, int v2) {
        if (v1 <= v2) {
            return v1;
        }
        return v2;
    }

    /**
     * @param v1
     * @param v2
     * @param v3
     * @return the square sum of the three values
     */
    private int squareSum(int v1, int v2, int v3) {
        return v1 * v1 + v2 * v2 + v3 * v3;
    }

    /**
     * checks if a value lies in a range
     *
     * @param v
     *                the value to be checked
     * @param value
     *                the value in the middle of the range
     * @param deviation
     *                the deviation of the value in the middle of the range
     * @return true, if the value lies in the given range, false otherwise
     */
    private boolean checkRange(float v, float value, float deviation) {
        if ((value - deviation <= v) && (v <= value + deviation)) {
            return true;
        }
        return false;
    }

    /**
     * @return number of Voxels in x direction of the VolumeOfVoxels
     */
    public int getNumberOfVoxelsX() {
        return this.numberOfVoxelsX;
    }

    /**
     * @return number of Voxels in y direction of the VolumeOfVoxels
     */
    public int getNumberOfVoxelsY() {
        return this.numberOfVoxelsY;
    }

    /**
     * @return number of Voxels in z direction of the VolumeOfVoxels
     */
    public int getNumberOfVoxelsZ() {
        return this.numberOfVoxelsZ;
    }

    /**
     * @return the total number of Voxels in the VolumeOfVoxels
     */
    public int getTotalNumberOfVoxels() {
        return this.totalNumberOfVoxels;
    }

    /**
     * @return the x coordinate of the start point of the VolumeOfVoxels
     */
    public float getVolumeStartX() {
        return volumeStartX;
    }

    /**
     * @return the y coordinate of the start point of the VolumeOfVoxels
     */
    public float getVolumeStartY() {
        return volumeStartY;
    }

    /**
     * @return the z coordinate of the start point of the VolumeOfVoxels
     */
    public float getVolumeStartZ() {
        return volumeStartZ;
    }

    /**
     * @return the x coordinate of the mid point of the VolumeOfVoxels
     */
    public float getMidpointX() {
        return midpointX;
    }

    /**
     * @return the y coordinate of the mid point of the VolumeOfVoxels
     */
    public float getMidpointY() {
        return midpointY;
    }

    /**
     * @return the z coordinate of the mid point of the VolumeOfVoxels
     */
    public float getMidpointZ() {
        return midpointZ;
    }

    /**
     * @return the x coordinate of the end point of the VolumeOfVoxels
     */
    public float getVolumeEndX() {
        return volumeEndX;
    }

    /**
     * @return the y coordinate of the end point of the VolumeOfVoxels
     */
    public float getVolumeEndY() {
        return volumeEndY;
    }

    /**
     * @return the z coordinate of the end point of the VolumeOfVoxels
     */
    public float getVolumeEndZ() {
        return volumeEndZ;
    }

    /**
     * @return the voxel array containing all the Voxels of the
     *         VolumeOfVoxels
     */
    public Voxel[] getVoxelArray() {
        return voxelArray;
    }

    /**
     * @param intX
     *                x coordinate of the Voxel in the VolumeOfVoxels
     * @param intY
     *                y coordinate of the Voxel in the VolumeOfVoxels
     * @param intZ
     *                z coordinate of the Voxel in the VolumeOfVoxels
     * @return the Voxel with the given coordinates from the voxelArray
     */
    public Voxel getVoxel(int intX, int intY, int intZ) {
        int index = getVoxelArrayIndex(intX, intY, intZ);
        return voxelArray[index];
    }

    public int getNumberOfVoxelsRadius() {
        return numberOfVoxelsRadius;
    }

    public float getRadius() {
        return radius;
    }

    private int getVoxelArrayIndex(int intX, int intY, int intZ) {
        return (intZ * numberOfVoxelsX * numberOfVoxelsY + intY * numberOfVoxelsX + intX);
    }

    private void printVoxel(int i, int j, int k) {
        this.getVoxel(i, j, k).printData();
    }

    private void setNumberOfVoxels(int numberOfVoxelsX, int numberOfVoxelsY, int numberOfVoxelsZ) {
        this.numberOfVoxelsX = numberOfVoxelsX;
        this.numberOfVoxelsY = numberOfVoxelsY;
        this.numberOfVoxelsZ = numberOfVoxelsZ;
        this.totalNumberOfVoxels = numberOfVoxelsX * numberOfVoxelsY * numberOfVoxelsZ;
    }

    private void setVoxelArray() {
        voxelArray = new Voxel[totalNumberOfVoxels];
        for (int i = 0; i < numberOfVoxelsZ; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsX; ++k) {
                    int index = i * numberOfVoxelsX * numberOfVoxelsY + j * numberOfVoxelsX + k;
                    voxelArray[index] = new Voxel(k, j, i, k * volumeEndX / numberOfVoxelsX, j * volumeEndY / numberOfVoxelsY, i * volumeEndZ / numberOfVoxelsZ);
                }
            }
        }
    }

    public void addAllVoxelsInRangeToContainer(Cube3dCreator creator, float value, float deviation) {
        creator.setCubeLength((this.volumeEndX - this.volumeStartX) / this.numberOfVoxelsX);
        float dummy = 0.0f;
        float percent = 0.0f;
        float biggestValue = this.getBiggestVoxelValue();
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    dummy = getVoxelValue(i, j, k);
                    percent = getValuePercentageInRelationToBiggestValue(dummy, biggestValue);
                    if (checkRange(percent, value, deviation)) {// (tag == 1)
                        creator.addCubeToContainer(getVoxel(i, j, k));
                    }
                }
            }
        }
    }

    /**
     * Function to fill the volume of voxels with a sphere or the surface of
     * a sphere.
     *
     * @param surfaceOfSphere If true, fill only the surface into the volume of
     * voxels
     */
    public void fillVolumeOfSphere(boolean surfaceOfSphere) {
        int midX = numberOfVoxelsX / 2;
        int midY = numberOfVoxelsY / 2;
        int midZ = numberOfVoxelsZ / 2;
        float numberOfVoxelsRadiusSquared = numberOfVoxelsRadius * numberOfVoxelsRadius - 1;
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    int distX = midX - i;
                    int distY = midY - j;
                    int distZ = midZ - k;
                    float squareDist = squareSum(distX, distY, distZ);
                    if (surfaceOfSphere) {
                        if (checkRange(squareDist,
                                numberOfVoxelsRadiusSquared,
                                0.05f * numberOfVoxelsRadiusSquared)) {
                            setVoxelValue(i, j, k, 1.0f);
                        }
                    } else {
                        if (squareDist <= numberOfVoxelsRadiusSquared) {
                            setVoxelValue(i, j, k, 1.0f);
                        }
                    }
                }
            }
        }
    }

    public float getBiggestVoxelValue() {
        float dummy = 0.0f;
        float biggestValue = 0.0f;
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    dummy = getVoxelValue(i, j, k);
                    if (dummy > biggestValue) {
                        biggestValue = dummy;
                        voxelWithBiggestValue = getVoxel(i, j, k);
                    }
                }
            }
        }
        return biggestValue;
    }

    /**
     * divides an ArrayList of MiniVoxels into smaller ArrayLists wherein
     * each slice of the volume contains approximately the same number of
     * NiniVoxels
     *
     * @param miniVoxelArrayList
     *                the ArrayList of MiniVoxels to be divided
     * @param tolerance
     *                the percentage of the number of MiniVoxels that can
     *                differ in each slice fom the previous slice
     * @return the divided ArrayList of MiniVoxels
     */
    public List getDivideMiniVoxelArrayList(List miniVoxelArrayList, double tolerance) {
        int numberOfMiniVoxelsInSlice = 0;
        // double numberOfMiniVoxels = 0.0;
        List<ArrayList<MiniVoxel>> dividedMiniVoxelArrayList = new ArrayList<ArrayList<MiniVoxel>>();
        ArrayList<MiniVoxel> partOfDividedMiniVoxelArrayList = new ArrayList<MiniVoxel>();
        dividedMiniVoxelArrayList.add(partOfDividedMiniVoxelArrayList);
        List slice = this.getSliceOfMiniVoxelArrayList(miniVoxelArrayList, 0);
        int sum = slice.size();
        int sliceCount = 1;
        double meanNumberOfMiniVoxels = sum;
        for (int z = 0; z < numberOfVoxelsZ; ++z) {
            slice = this.getSliceOfMiniVoxelArrayList(miniVoxelArrayList, z);
            numberOfMiniVoxelsInSlice = slice.size();
            // wenn nicht in toleranz dann
            if (numberOfMiniVoxelsInSlice < (meanNumberOfMiniVoxels - (meanNumberOfMiniVoxels * tolerance)) || numberOfMiniVoxelsInSlice > (meanNumberOfMiniVoxels + (meanNumberOfMiniVoxels * tolerance))) {
                // neue Liste anlegen
                // großer Liste hinzufügen
                // summe und zähler zurück setzen
                partOfDividedMiniVoxelArrayList = new ArrayList<MiniVoxel>();
                dividedMiniVoxelArrayList.add(partOfDividedMiniVoxelArrayList);
                sum = 0;
                sliceCount = 1;
            }
            Iterator sliceIterator = slice.iterator();
            while (sliceIterator.hasNext()) {
                MiniVoxel miniVoxel = (MiniVoxel) sliceIterator.next();
                partOfDividedMiniVoxelArrayList.add(miniVoxel);
            }
            if (z != 0) {
                sum = sum + numberOfMiniVoxelsInSlice;
            }
            meanNumberOfMiniVoxels = ((double) sum) / ((double) sliceCount);
            sliceCount++;
        }

        // int i = 0;
        // Iterator it = dividedMiniVoxelArrayList.iterator();
        // while (it.hasNext())
        // {
        // System.out.println("i: " + i);
        // Iterator it2 = ((List<ArrayList<MiniVoxel>>)
        // it.next()).iterator();
        // while (it2.hasNext())
        // {
        // ((MiniVoxel) it2.next()).printData();
        // }
        // i++;
        // System.out.println("!!!!!!!!!!!!!!!!!");
        // }

        return dividedMiniVoxelArrayList;
    }

    // percentages for Voxel choosing
    // 0 - 25 %: 12.5f, 12.4f
    // 25 - 50 %: 37.5f, 12.6f
    // 50 - 75 %: 62.5f, 12.4f
    // 75 - 100 %: 87.5f, 12.5f
    // Cube3dCreator cubeCreator_0 = new Cube3dCreator(0.6f / VOV_L,
    // ColorUtil.yellow);
    // this.volumeOfVoxels.addAllVoxelsInRangeToContainer(cubeCreator_0,
    // 80.0f, 8.0f);
    // contentRoot.addChild(cubeCreator_0.getCubeContainer());
    public List getMiniVoxelArrayList(float certainValue, float deviation) {
        List<MiniVoxel> miniVoxelArrayList = new ArrayList<MiniVoxel>();
        Voxel voxel;
        float voxelValue;
        float percent = 0.0f;
        float biggestValue = this.getBiggestVoxelValue();
        //
        // for (int i = 0; i < numberOfVoxelsX; ++i) {
        // for (int j = 0; j < numberOfVoxelsY; ++j) {
        // for (int k = 0; k < numberOfVoxelsZ; ++k) {
        //
        // dummy = getVoxelValue(i, j, k);
        // percent = getValuePercentageInRelationToBiggestValue(dummy,
        // biggestValue);
        //
        // //System.out.println("?????????????????????????????????");
        // //System.out.println("dummy: " + dummy);
        // //System.out.println("percent: " + percent);
        //
        // if (checkRange(percent, value, deviation)) {// (tag == 1)
        // creator.addCubeToContainer(getVoxel(i, j, k));
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    voxelValue = getVoxelValue(i, j, k);
                    percent = getValuePercentageInRelationToBiggestValue(voxelValue, biggestValue);
                    if ((checkRange(percent, certainValue, deviation)) == true) {
                        voxel = getVoxel(i, j, k);
                        float xF = voxel.getX();
                        float yF = voxel.getY();
                        float zF = voxel.getZ();
                        miniVoxelArrayList.add(new MiniVoxel(voxelValue, new Float(xF).doubleValue(), new Float(yF).doubleValue(), new Float(zF).doubleValue(), k));
                    }
                }
            }
        }
        return miniVoxelArrayList;
    }

    @SuppressWarnings("unchecked")
    public List getSliceOfMiniVoxelArrayList(List miniVoxelArrayList, int sliceNumber) {
        ArrayList miniVoxelArrayListSlice = new ArrayList();
        Iterator miniVoxelArrayListIterator = miniVoxelArrayList.iterator();
        while (miniVoxelArrayListIterator.hasNext()) {
            MiniVoxel miniVoxel = (MiniVoxel) miniVoxelArrayListIterator.next();
            if (miniVoxel.getSliceNumber() == sliceNumber) {
                miniVoxelArrayListSlice.add(miniVoxel);
            }
        }
        return miniVoxelArrayListSlice;
    }

    public float getValuePercentageInRelationToBiggestValue(float value, float biggestValue) {
        float onePercent = biggestValue / 100.0f;
        float valuePercent = value / onePercent;
        return valuePercent;
    }

    public float[][][] getVoxelsOfCertainValueAsFloatArray(float certainValue, float deviation) {
        float data[][][] = new float[numberOfVoxelsX][numberOfVoxelsY][numberOfVoxelsZ];
        @SuppressWarnings("unused")
        float voxelValue, min, max;
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    voxelValue = getVoxelValue(i, j, k);
                    min = voxelValue - deviation;
                    max = voxelValue + deviation;
                    if ((checkRange(voxelValue, certainValue, deviation)) == true) {
                        data[i][j][k] = voxelValue;
                    } else {
                        data[i][j][k] = 0.0f;
                    }
                }
            }
        }
        return data;
    }

    public float[][][] getVoxelsValueAsFloatArray() {
        logger.info("\nGet all voxels with value as a single float array");
        float data[][][] = new float[numberOfVoxelsX][numberOfVoxelsY][numberOfVoxelsZ];
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    data[i][j][k] = getVoxelValue(i, j, k);
                }
            }
        }
        return data;
    }

    public int getVoxelTag(int intX, int intY, int intZ) {
        int index = getVoxelArrayIndex(intX, intY, intZ);
        return voxelArray[index].getTag();
    }

    public float getVoxelValue(int intX, int intY, int intZ) {
        int index = getVoxelArrayIndex(intX, intY, intZ);
        return voxelArray[index].getValue();
    }

    public Voxel getVoxelWithBiggestValue() {
        return voxelWithBiggestValue;
    }

    public void printAllVoxels() {
        logger.info("\nPrint all voxels");
        for (int i = 0; i < totalNumberOfVoxels; ++i) {
            logger.info("i: " + i);
            logger.info("voxelArray[i].getValue(): " + voxelArray[i].getValue());
        }
    }

    public void printAllVoxelsByValue(float value, float deviation) {
        logger.info("\nPrint all voxels with value " + value + "(+-" + deviation + ")");
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    float v = getVoxelValue(i, j, k);
                    if (checkRange(v, value, deviation)) {
                        getVoxel(i, j, k).printData();
                    }
                }
            }
        }
    }

    /**
     * Sets the value of the Voxel directly in the voxelArray if index is
     * known
     *
     * @param index
     *                index in the voxelArray
     * @param value
     *                value to be set
     */
    public void setVoxelValue(int index, float value) {
        voxelArray[index].setValue(value);
    }

    /**
     * sets the value at given coordinates in the VolumeOfVoxels
     *
     * @param intX
     *                x coordinate
     * @param intY
     *                y coordinate
     * @param intZ
     *                z coordinate
     * @param value
     *                value to be set
     */
    public void setVoxelValue(int intX, int intY, int intZ, float value) {
        int index = getVoxelArrayIndex(intX, intY, intZ);
        voxelArray[index].setValue(value);
    }

    /**
     * sorts the MiniVoxels in the miniVoxelArrayList depending on their
     * size (biggest first).
     *
     * @param miniVoxelArrayList The list to be ordered.
     */
    @SuppressWarnings("unchecked")
    public void getOrderedMiniVoxelArrayList(List miniVoxelArrayList) {
        Collections.sort(miniVoxelArrayList);
        Collections.reverse(miniVoxelArrayList);

//		ArrayList<MiniVoxel> orderedArrayList = new ArrayList<MiniVoxel>();
//
//		MiniVoxel miniVoxelWithBiggestValue = null;
//		MiniVoxel dummyMiniVoxel = null;
//
//		float biggestValue = 0.0f;
//		float dummyValue = 0.0f;
//
//		int indexOfBiggestMiniVoxel = 0;
//
//		// System.out.println("MiniVoxelArrayList.size(): " +
//		// miniVoxelArrayList.size());
//
//		for (int i = 0; i < miniVoxelArrayList.size(); i++)
//		    {
//			// System.out.println("miniVoxelArrayList.size(): " +
//			// miniVoxelArrayList.size());
//			// System.out.println("i: " + i);
//
//			for (int j = 0; j < miniVoxelArrayList.size(); j++)
//			    {
//
//				dummyMiniVoxel = (MiniVoxel) miniVoxelArrayList.get(j);
//				dummyValue = dummyMiniVoxel.getValue();
//
//				if (dummyValue > biggestValue)
//				    {
//					biggestValue = dummyValue;
//					miniVoxelWithBiggestValue = (MiniVoxel) miniVoxelArrayList.get(j);
//					indexOfBiggestMiniVoxel = j;
//				    }
//
//			    }
//
//			// System.out.println("miniVoxelWithBiggestValue.getValue():
//			// " + miniVoxelWithBiggestValue.getValue());
//			orderedArrayList.add(miniVoxelWithBiggestValue);
//			if (indexOfBiggestMiniVoxel < miniVoxelArrayList.size())
//			    {
//				miniVoxelArrayList.remove(indexOfBiggestMiniVoxel);
//			    }
//			biggestValue = 0.0f;
//		    }
//
//		// System.out.println("orderedArrayList: ");
//		for (int k = 0; k < orderedArrayList.size(); k++)
//		    {
//			// System.out.println("k: " + k);
//			// System.out.println(((MiniVoxel)
//			// orderedArrayList.get(k)).getValue());
//		    }
//
//		return orderedArrayList;
    }

    public List getArrayListOfMiniVoxelsWhichAddUpTo(List orderedMiniVoxelArrayList, float percent) {
        List<MiniVoxel> arrayListOfMiniVoxelsWhichAddUpTo = new ArrayList<MiniVoxel>();
        MiniVoxel next = null;
        float sum = 0.0f;
        float value = 0.0f;
        float perc = 0.0f;
        float totalValue = this.getTotalVoxelValue();
        Iterator it = orderedMiniVoxelArrayList.iterator();
        while (it.hasNext()) {
            next = (MiniVoxel) it.next();
            value = next.getValue();
            perc = this.getValuePercentageInRelationToTotalValue(value, totalValue);
            // System.out.println("perc: " + perc);
            sum += perc;
            arrayListOfMiniVoxelsWhichAddUpTo.add(next);
            if (sum > percent) {
                break;
            }
        }
        return arrayListOfMiniVoxelsWhichAddUpTo;
    }

    /**
     * @param value
     * @param totalValue
     * @return valuePercent
     */
    private float getValuePercentageInRelationToTotalValue(float value, float totalValue) {
        float onePercent = totalValue / 100.0f;
        float valuePercent = value / onePercent;
        return valuePercent;
    }

    /**
     * @return total value of all voxels
     */
    private float getTotalVoxelValue() {
        float dummy = 0.0f;
        float totalValue = 0.0f;
        for (int i = 0; i < numberOfVoxelsX; ++i) {
            for (int j = 0; j < numberOfVoxelsY; ++j) {
                for (int k = 0; k < numberOfVoxelsZ; ++k) {
                    dummy = getVoxelValue(i, j, k);
                    totalValue += dummy;
                }
            }
        }
        return totalValue;
    }
    // public void printAllTagedVoxels()
    // {
    // System.out.println("\nPrint all taged voxels");
    //
    // for (int i = 0; i < numberOfVoxelsX; ++i)
    // {
    // for (int j = 0; j < numberOfVoxelsY; ++j)
    // {
    // for (int k = 0; k < numberOfVoxelsZ; ++k)
    // {
    // int tag = getVoxelTag(i, j, k);
    // if (tag == 1)
    // getVoxel(i, j, k).printData();
    // }
    // }
    // }
    // }
    // public void addAllTagedVoxelsToGraph(BranchGroup graph, Cube3dCreator
    // creator)
    // {
    // System.out.println("\nAdd all taged voxels to graph");
    // creator.setCubeLength((this.volumeEndX - this.volumeStartX) /
    // this.numberOfVoxelsX);
    //
    // for (int i = 0; i < numberOfVoxelsX; ++i)
    // {
    // for (int j = 0; j < numberOfVoxelsY; ++j)
    // {
    // for (int k = 0; k < numberOfVoxelsZ; ++k)
    // {
    // int tag = getVoxelTag(i, j, k);
    // if (tag == 1)
    // graph.addChild(creator.getCubeAsBox(getVoxel(i, j, k)));
    // }
    // }
    // }
    // }
    // public void addAllSumVoxelsToContainer(Cube3dCreator creator, float
    // sum)
    // {
    //
    // float currentSum = 0.0f;
    //
    // float start = this.getBiggestVoxelValue();
    //
    // currentSum += start;
    //
    // float xStart = this.voxelWithBiggestValue.getX();
    // float yStart = this.voxelWithBiggestValue.getY();
    // float zStart = this.voxelWithBiggestValue.getZ();
    //
    // }
    // public void addAllTagedVoxelsToContainer(Cube3dCreator creator)
    // {
    // System.out.println("\nAdd all taged voxels to container");
    // creator.setCubeLength((this.volumeEndX - this.volumeStartX) /
    // this.numberOfVoxelsX);
    //
    // for (int i = 0; i < numberOfVoxelsX; ++i)
    // {
    // for (int j = 0; j < numberOfVoxelsY; ++j)
    // {
    // for (int k = 0; k < numberOfVoxelsZ; ++k)
    // {
    // int tag = getVoxelTag(i, j, k);
    // // float dummy = getVoxelValue(i, j, k);
    // // if (dummy > 0.0f)
    // if (tag == 1)
    // {
    // creator.addCubeToContainer(getVoxel(i, j, k));
    // }
    // }
    // }
    // }
    // }
}
