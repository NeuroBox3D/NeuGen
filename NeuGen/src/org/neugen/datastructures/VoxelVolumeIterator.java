/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
