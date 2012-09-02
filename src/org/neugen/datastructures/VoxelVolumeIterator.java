package org.neugen.datastructures;

import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import org.apache.log4j.Logger;
import org.neugen.gui.DensityDialog;

/**
 * Class for an Iterator for VoxelVolume.
 *
 * @author Jens P Eberhard
 * @author Simone Eberhard
 */
public class VoxelVolumeIterator {

    /** Use to log messages */
    private final static Logger logger = Logger.getLogger(VoxelVolumeIterator.class.getName());
    /** The VoxelVolume for the Iterator */
    private VoxelVolume vv;
    /** The counter */
    private int counter;
    /** Voxel length */
    private Point3i voxelSize;
    /** number of Voxels in voxelArray */
    int numberOfVoxelsInArray;
    private Point3i voxelNumber;

    /** Constructor */
    public VoxelVolumeIterator(VoxelVolume vVolume) {
        vv = vVolume;
        counter = 0;
        voxelSize = new Point3i(); // size of voxels (x, y, z)
        voxelSize = vv.getVoxelSize();
        voxelNumber = vv.getVoxelNumber();
        numberOfVoxelsInArray = voxelNumber.x * voxelNumber.y * voxelNumber.z;
    }

    /** 
     * value of the next element
     *
     * @return the value of the next element
     */
    public float next() {
        if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                && vv.getDensityMethod().equals(DensityDialog.DensityMethod.LENGTH)) {
            return vv.getVoxelArray().get(counter++).getLengthContent();
        } else if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                && vv.getDensityMethod().equals(DensityDialog.DensityMethod.VOLUME)) {
            return vv.getVoxelArray().get(counter++).getVolumeContent();
        } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.SYNAPTIC)
                && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
            return vv.getVoxelArray().get(counter++).getNumberContent();
        } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.IMAGE)
                && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
            return vv.getVoxelArray().get(counter++).getNumberContent();
        } else {
            return 0.0f;
        }
    }

    /** test for end of struture */
    public boolean hasNext() {
        if (counter < vv.getVoxelArray().size()) {
            return true;
        }
        return false;
    }

    /** 
     * get the value at specified coordinates in VoxelVolume
     *
     * @return the value at specified coordinates in VoxelVolume
     */
    public float get(int x, int y, int z) {
        Point3f coords = new Point3f();
        coords.x = (float) x * voxelSize.x;
        coords.y = (float) y * voxelSize.y;
        coords.z = (float) z * voxelSize.z;
        int index = vv.getVoxelIndex(coords);
        if (index < vv.getVoxelArray().size()) {
            if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                    || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.LENGTH)) {
                return vv.getVoxelArray().get(index).getLengthContent();
            } else if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                    || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.VOLUME)) {
                return vv.getVoxelArray().get(index).getVolumeContent();
            } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.SYNAPTIC)
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
                return vv.getVoxelArray().get(index).getNumberContent();
            } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.IMAGE)
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
                return vv.getVoxelArray().get(index).getNumberContent();
            } else {
                return 0.0f;
            }
        } else {
            logger.info("ERROR: index too big");
            return 0.0f;
        }
    }

    /** 
     * get the value at specified index in voxelArray
     *
     * @return the value at specified index in voxelArray
     */
    public float get(int index) {
        if (index < vv.getVoxelArray().size()) {
            if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                    || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.LENGTH)) {
                return vv.getVoxelArray().get(index).getLengthContent();
            } else if ((vv.getDensityPart().equals(DensityDialog.DensityPart.DENDRITIC)
                    || vv.getDensityPart().equals(DensityDialog.DensityPart.AXONAL))
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.VOLUME)) {
                return vv.getVoxelArray().get(index).getVolumeContent();
            } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.SYNAPTIC)
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
                return vv.getVoxelArray().get(index).getNumberContent();
            } else if (vv.getDensityPart().equals(DensityDialog.DensityPart.IMAGE)
                    && vv.getDensityMethod().equals(DensityDialog.DensityMethod.NUMBER)) {
                return vv.getVoxelArray().get(index).getNumberContent();
            } else {
                return 0.0f;
            }
        } else {
            logger.info("ERROR : index too big");
            return 0.0f;
        }
    }

    public Point3i getVoxelNumber() {
        return voxelNumber;
    }

    /** 
     * get number of Voxels in voxelArray
     *
     * @return the number of Voxels in voxelArray
     */
    public int getNumberOfVoxelsInArray() {
        return numberOfVoxelsInArray;
    }
}
