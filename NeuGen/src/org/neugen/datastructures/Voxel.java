/*
 * Voxel.java
 *
 * Created on December 26, 2005
 *
 * Class for a three-dimensional voxel with a value (and tag)
 * 
 */
package org.neugen.datastructures;

import org.apache.log4j.Logger;
import org.neugen.gui.DensityDialog;

/**
 * Class Voxel
 * 
 * @author Jens P Eberhard
 * @author Simone Eberhard
 */
public class Voxel {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(Voxel.class.getName());
    /** the x coordinate of the Voxel  */
    private int intX;
    /** the y coordinate of the Voxel */
    private int intY;
    /** the z coordinate of the Voxel */
    private int intZ;
    /** the x coordinate of the Voxel */
    private float x;
    /** the y coordinate of the Voxel */
    private float y;
    /** the z coordinate of the Voxel */
    private float z;
    /**
     * the value of the Voxel can be a (percentage of a) length or volume of
     * a net part (dendrite or axon) or a (percentage of a) number of a net
     * part (synapses)
     */
    private float value;
    /**
     * the tag of the Voxel can be used to determine wether a Voxel is set
     * (tag = 1) or not (tag = 0)
     */
    private int tag;
    /** content of dendritic or axonal length in voxel  */
    private float lengthContent;
    /** content of dendritic or axonal volume in voxel  */
    private float volumeContent;
    /** content of numbers of synapses in voxel */
    private float numberContent;
    /** percentage of content of dendritic length in voxel */
    private float lengthPercentContent;
    /** percentage of content of dendritic volume in voxel  */
    private float volumePercentContent;
    /** percentage of content of numbers of synapses in voxel */
    private float numberPercentContent;
    /** not specified content  */
    private float content;

    /**
     * Constructor
     *
     * @param densityPart : one of [dendritic, axonal, synaptic]
     * @param densityMethod : one of [length, volume, number]
     */
    public Voxel(DensityDialog.DensityPart densityPart, DensityDialog.DensityMethod densityMethod) {
            if ((densityPart.equals(DensityDialog.DensityPart.DENDRITIC)
                    || densityPart.equals(DensityDialog.DensityPart.AXONAL)) && densityMethod.equals(DensityDialog.DensityMethod.LENGTH)) {
                lengthContent = 0.0f;
                lengthPercentContent = 0.0f;
            } else if ((densityPart.equals(DensityDialog.DensityPart.DENDRITIC)
                    || densityPart.equals(DensityDialog.DensityPart.AXONAL)) && densityMethod.equals(DensityDialog.DensityMethod.VOLUME)) {
                volumeContent = 0.0f;
                volumePercentContent = 0.0f;
            } else if (densityPart.equals(DensityDialog.DensityPart.SYNAPTIC) && densityMethod.equals(DensityDialog.DensityMethod.NUMBER)) {
                numberContent = 0.0f;
                numberPercentContent = 0.0f;
            } else if (densityPart.equals(DensityDialog.DensityPart.IMAGE) && densityMethod.equals(DensityDialog.DensityMethod.NUMBER)) {
                numberContent = 0.0f;
                numberPercentContent = 0.0f;
            }
    }

    /**
     * Creates a new instance of Voxel
     *
     * @param intX
     *                the x coordinate of the voxel
     * @param intY
     *                the y coordinate of the voxel
     * @param intZ
     *                the z coordinate of the voxel
     */
    public Voxel(int intX, int intY, int intZ) {
        this.intX = intX;
        this.intY = intY;
        this.intZ = intZ;
        this.value = 0.0f;
        this.tag = 0;
    }

    /**
     * Creates a new instance of Voxel
     *
     * @param intX
     *                the x coordinate of the Voxel
     * @param intY
     *                the y coordinate of the Voxel
     * @param intZ
     *                the z coordinate of the Voxel
     * @param x
     *                the x coordinate of the Voxel
     * @param y
     *                the y coordinate of the Voxel
     * @param z
     *                the z coordinate of the Voxel
     */
    public Voxel(int intX, int intY, int intZ, float x, float y, float z) {
        this(intX, intY, intZ);
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = 0.0f;
        this.tag = 0;
    }

    /**
     * @return the x coordinate of the Voxel
     */
    public final float getX() {
        return x;
    }

    /**
     * @return the y coordinate of the Voxel
     */
    public final float getY() {
        return y;
    }

    /**
     * @return the z coordinate of the Voxel
     */
    public final float getZ() {
        return z;
    }

    /**
     * @return the tag of the voxel (default or not set = 0, set = 1)
     */
    public final int getTag() {
        return tag;
    }

    /**
     * @return the value of the MiniVoxel can be a (percentage of a) length
     *         or volume of a net part (dendrite or axon) or a (percentage
     *         of a) number of a net part (synapses)
     */
    public final float getValue() {
        return value;
    }

    /**
     * @param value
     *                the value of the MiniVoxel can be a (percentage of a)
     *                length or volume of a net part (dendrite or axon) or a
     *                (percentage of a) number of a net part (synapses)
     */
    public final void setValue(float value) {
        this.value = value;
    }

    /**
     * prints data of the Voxel: coordinates, (tag) and value
     */
    public final void printData() {
        logger.info("Voxel:");
        logger.info(intX + " " + intY + " " + intZ + "; x " + x + " y " + y + " z " + z + "; ");
        logger.info(tag + " " + value);
    }

    /**
     * sets the x coordinate of the Voxel
     *
     * @param x
     *                the x coordinate of the Voxel
     */
    @SuppressWarnings("unused")
    private void setX(float x) {
        this.x = x;
    }

    /**
     * sets the y coordinate of the Voxel
     *
     * @param y
     *                the y coordinate of the Voxel
     */
    @SuppressWarnings("unused")
    private void setY(float y) {
        this.y = y;
    }

    /**
     * sets the z coordinate of the Voxel
     * @param z the z coordinate of the Voxel
     */
    @SuppressWarnings("unused")
    private void setZ(float z) {
        this.z = z;
    }

    /**
     * sets the tag of the Voxel
     * @param tag the tag of the Voxel, 0 = not tagged, 1 = tagged
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * add content of dendritic or axonallength in voxel
     */
    public void addLengthContent(float lenCont) {
        lengthContent += lenCont;
    }

    /**
     * add content of dendritic or axonal volume in voxel
     */
    public void addVolumeContent(float volCont) {
        volumeContent += volCont;
    }

    /**
     * add content of number of synapses in voxel
     */
    public void addNumberContent(float numCont) {
        numberContent += numCont;
    }

    /**
     * add not specified content
     */
    public void addContent(float cont) {
        content += cont;
    }

    /**
     * set percentage of content of dendritic or axonal length in voxel
     */
    public void setLengthPercentContent(float lenPercCon) {
        lengthPercentContent = lenPercCon;
    }

    /**
     * set percentage of content of dendritic or axonal volume in voxel
     */
    public void setVolumePercentContent(float volPercCon) {
        volumePercentContent = volPercCon;
    }

    /**
     * set percentage of number of synapses in voxel
     */
    public void setNumberPercentContent(float numPercCon) {
        numberPercentContent = numPercCon;
    }

    /**
     * set not specified content
     */
    public void setContent(float cont) {
        content = cont;
    }

    /** 
     * get content of dendritic or axonal length in voxel
     */
    public float getLengthContent() {
        return lengthContent;
    }

    /**
     * get content of dendritic or axonal volume in voxel
     */
    public float getVolumeContent() {
        return volumeContent;
    }

    /**
     * get content of number of synapses in voxel
     */
    public float getNumberContent() {
        return numberContent;
    }

    /**
     * get percentage of content of dendritic or axonal length in voxel
     */
    public float getLengthPercentContent() {
        return lengthPercentContent;
    }

    /**
     * get percentage of content of dendritic or axonal volume in voxel
     */
    public float getVolumePercentContent() {
        return volumePercentContent;
    }

    /**
     * get percentage of content of number of synapses in voxel
     */
    public float getNumberPercentContent() {
        return numberPercentContent;
    }
}
