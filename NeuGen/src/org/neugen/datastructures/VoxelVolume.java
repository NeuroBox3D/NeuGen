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
package org.neugen.datastructures;

import org.neugen.datastructures.neuron.Neuron;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import org.apache.log4j.Logger;
import org.neugen.gui.DensityDialog;

/**
 * Class VoxelVolume
 *
 * @author Jens P Eberhard
 * @author Simone Eberhard
 */
public final class VoxelVolume {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(VoxelVolume.class.getName());
    /** the net */
    private Net net;
    /** part of density : [dendritic, axonal, synaptic] */
    private DensityDialog.DensityPart densityPart;
    /** method of density : [length, volume, number] */
    private DensityDialog.DensityMethod densityMethod;
    /** x-, y- and z-length of one voxel */
    private Point3i voxelSize;
    /** number of voxels in the VoxelVolume / voxelArray */
    private Point3i voxelNumber;
    /** contains all Voxels of the VoxelVolume */
    private List<Voxel> voxelArray;

    public VoxelVolume(ArrayList<int[][]> matrixList, Point3i voxelSize, int intensity, int weight) {

        int width = 0, height = 0;
        if(matrixList.size() > 0) {
            width = matrixList.get(0).length;
            height = matrixList.get(0)[0].length;
        }

        int layerSize = matrixList.size();

        this.densityPart = DensityDialog.DensityPart.IMAGE;
        this.densityMethod = DensityDialog.DensityMethod.NUMBER;

        // size of voxels
        this.voxelSize = new Point3i(voxelSize.x, voxelSize.z, voxelSize.y);
        //voxelSize = new Point3i(voxelLength, voxelLength, voxelLength); // size of voxels (x, y, z)
        // number of voxels
        voxelNumber = new Point3i(); // number of voxels (x, y, z) in VoxelVolume
        /** The lower left corner of the region. */
        Point3f lowLeftCorner = new Point3f(0.0f, 0.0f, 0.0f);
        /** The upper right corner of the region. */
        //Point3f e = new Point3f(par.getLength(), par.getWidth(), par.getHeight());
        Point3f upRightCorner = new Point3f(width, layerSize, height);

        Region region3D = new Region(lowLeftCorner, upRightCorner);
        Region.setInstance(region3D);
        voxelNumber = computeVoxelNumber();

        int totalVoxelNumber = voxelNumber.x * voxelNumber.y * voxelNumber.z; // total number of Voxels in VoxelVolume
        logger.info("totalVoxelNumber: " + totalVoxelNumber);
        // fill voxelArray with empty Voxels
        voxelArray = new ArrayList<Voxel>();

        for (int i = 0; i < totalVoxelNumber; i++) {
            Voxel voxel = new Voxel(densityPart, densityMethod);
            voxelArray.add(i, voxel);
        }

        logger.info("Nucleus part and number method.");
        // fill all voxels with number

        int c =0;
        for(int[][] matrix : matrixList) {
            fillVoxelVolumeWithNumPixel(matrix, c, intensity, weight);
           // c+=10;
            c++;
        }
        // compute the percentage of value in each Voxel and overwrite it
        computeNumberPercentageOfVoxelContent();
    }

    /** Constructor */
    public VoxelVolume(Net net, int voxelLength, DensityDialog.DensityPart densityPart, DensityDialog.DensityMethod densityMethod) {
        this.net = net; // the net
        this.densityPart = densityPart;
        this.densityMethod = densityMethod;
        // size of voxels
        voxelSize = new Point3i(voxelLength, voxelLength, voxelLength); // size of voxels (x, y, z)
        // number of voxels
        voxelNumber = new Point3i(); // number of voxels (x, y, z) in VoxelVolume
        voxelNumber = computeVoxelNumber();
        int totalVoxelNumber = voxelNumber.x * voxelNumber.y * voxelNumber.z; // total number of Voxels in VoxelVolume
        //logger.info("totalVoxelNumber: " + totalVoxelNumber);
        // fill voxelArray with empty Voxels
        voxelArray = new ArrayList<Voxel>();
        for (int i = 0; i < totalVoxelNumber; i++) {
            Voxel voxel = new Voxel(densityPart, densityMethod);
            voxelArray.add(i, voxel);
        }
        logger.info("voxelArray.size: " + voxelArray.size());
        // fill the Voxels in the VoxelVolume / voxelArray with value
        if ((densityPart.equals(DensityDialog.DensityPart.DENDRITIC)
                || densityPart.equals(DensityDialog.DensityPart.AXONAL))
                && densityMethod.equals(DensityDialog.DensityMethod.LENGTH)) {
            logger.info("Axonal or dendritic part and lenght method.");
            // fill all voxels with length
            fillVoxelVolumeWithLen();
            // compute the percentage of value in each Voxel and overwrite it
            computeLengthPercentageOfVoxelContent();
        } else if ((densityPart.equals(DensityDialog.DensityPart.DENDRITIC)
                || densityPart.equals(DensityDialog.DensityPart.AXONAL)) && densityMethod.equals(DensityDialog.DensityMethod.VOLUME)) {
            logger.info("Axonal or dendritic part and volume method.");
            // fill all voxels with volume
            fillVoxelVolumeWithVol();
            // compute the percentage of value in each Voxel and overwrite it
            computeVolumePercentageOfVoxelContent();
        } else if (densityPart.equals(DensityDialog.DensityPart.SYNAPTIC) && densityMethod.equals(DensityDialog.DensityMethod.NUMBER)) {
            logger.info("Synaptic part and number method.");
            // fill all voxels with number
            fillVoxelVolumeWithNum();
            // compute the percentage of value in each Voxel and overwrite it
            computeNumberPercentageOfVoxelContent();
        }
    }

    /** computes number of voxels [x, y, z] */
    private Point3i computeVoxelNumber() {
        Point3i voxelNum = new Point3i();
        Point3f upRightCorner = Region.getInstance().getUpRightCorner();
        logger.info("upRightCorner: " + upRightCorner.toString());
        float xNumber = (upRightCorner.x) / voxelSize.x; // number of voxels in x direction
        float yNumber = (upRightCorner.y) / voxelSize.y; // number of voxels in y direction
        float zNumber = (upRightCorner.z) / voxelSize.z; // number of voxels in z direction
        voxelNum.x = (int) xNumber;
        voxelNum.y = (int) yNumber;
        voxelNum.z = (int) zNumber;
        return voxelNum;
    }

    /**
     * returns the voxelArray
     *
     * @return the voxel arraylist
     */
    public List<Voxel> getVoxelArray() {
        return voxelArray;
    }

    /**
     * returns part of density
     *
     * @return the density part
     */
    public DensityDialog.DensityPart getDensityPart() {
        return densityPart;
    }

    /**
     * returns method of density
     *
     * @return the density method
     */
    public DensityDialog.DensityMethod getDensityMethod() {
        return densityMethod;
    }

    /** returns x-, y- and z-length of one Voxel */
    public Point3i getVoxelSize() {
        return voxelSize;
    }

    /** returns number of voxels in the VoxelVolume */
    public Point3i getVoxelNumber() {
        return voxelNumber;
    }

    /**
     * computes the index of the voxel in which a given coordinate lays
     *
     * @return the index of the voxel
     */
    public int getVoxelIndex(Point3f coordinates) {
        int index;
        int xRow, yColumn, zSlice;
        Point3i coordinatesInVoxelVolume = new Point3i();
        coordinatesInVoxelVolume.x = (int) Math.floor(coordinates.x / voxelSize.x);
        coordinatesInVoxelVolume.y = (int) Math.floor(coordinates.y / voxelSize.y);
        coordinatesInVoxelVolume.z = (int) Math.floor(coordinates.z / voxelSize.z);
        // coordinates can extend outside column
        if (coordinatesInVoxelVolume.x >= voxelNumber.x) {
            return -1;
        }
        if (coordinatesInVoxelVolume.y >= voxelNumber.y) {
            return -2;
        }
        if (coordinatesInVoxelVolume.z >= voxelNumber.z) {
            return -3;
        }
        xRow = coordinatesInVoxelVolume.x;
        yColumn = coordinatesInVoxelVolume.y * voxelNumber.x;
        zSlice = coordinatesInVoxelVolume.z * voxelNumber.y * voxelNumber.x;
        index = xRow + yColumn + zSlice;
        if (index < -1) {
            return -1;
        }
        return index;
    }

    /** fills the Voxels of the VoxelVolume with the dendritic or axonal length contained in this voxel */
    public void fillVoxelVolumeWithLen() {
        float lengthOfSegment;
        int index;
        if (densityPart.equals(DensityDialog.DensityPart.DENDRITIC)) {
            for (Neuron neuron : net.getNeuronList()) {
                for (Dendrite currentDen : neuron.getDendrites()) {
                    if (currentDen.getFirstSection() == null) {
                        return;
                    }
                    Section.Iterator secit = currentDen.getFirstSection().getIterator();
                    while (secit.hasNext()) {
                        Section sec = secit.next();
                        for (Segment segment : sec.getSegments()) {
                            lengthOfSegment = segment.getLength();
                            //test in which voxel center lays (getVoxelIndex)
                            //add length to this voxel
                            index = this.getVoxelIndex(segment.getCenter());
                            if (index != -1 && index != -2 && index != -3) {
                                try {
                                    Voxel voxel = this.voxelArray.get(index);
                                    voxel.addLengthContent(lengthOfSegment);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    if (logger.isDebugEnabled()) {
                                        logger.error(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if ((densityPart.equals(DensityDialog.DensityPart.AXONAL))) {
            for (Neuron neuron : net.getNeuronList()) {
                Axon axon = neuron.getAxon();
                if (axon.getFirstSection() == null) {
                    return;
                }
                Section.Iterator secit = axon.getFirstSection().getIterator();
                while (secit.hasNext()) {
                    Section sec = secit.next();
                    for (Segment segment : sec.getSegments()) {
                        lengthOfSegment = segment.getLength();
                        //test in which voxel center lays (getVoxelIndex)
                        //add length to this voxel
                        index = this.getVoxelIndex(segment.getCenter());
                        if (index != -1 && index != -2 && index != -3) {
                            try {
                                Voxel voxel = this.voxelArray.get(index);
                                voxel.addLengthContent(lengthOfSegment);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                if (logger.isDebugEnabled()) {
                                    logger.error(ex);
                                }
                            }
                        }
                    }
                }
            }
        } else if ((densityPart.equals(DensityDialog.DensityPart.SYNAPTIC))) {
            logger.info("NOT YET IMPLEMENTED: SYNAPTIC");
        } else {
            logger.info("NOT YET IMPLEMENTED: " + densityPart);
        }
    }

    /** fills the Voxels of the VoxelVolume with the dendritic volume contained in this voxel */
    public void fillVoxelVolumeWithVol() {
        int index;
        if (densityPart.equals(DensityDialog.DensityPart.DENDRITIC)) {
            for (Neuron neuron : net.getNeuronList()) {
                for (Dendrite currentDen : neuron.getDendrites()) {
                    if (currentDen.getFirstSection() == null) {
                        return;
                    }
                    Section.Iterator secit = currentDen.getFirstSection().getIterator();
                    while (secit.hasNext()) {
                        Section sec = secit.next();
                        for (Segment segment : sec.getSegments()) {
                            float volumeOfSegment = segment.getVolume();
                            //test in which voxel center lays (getVoxelIndex)
                            //add volume to this voxel
                            index = this.getVoxelIndex(segment.getCenter());
                            if (index != -1 && index != -2 && index != -3) {
                                try {
                                    Voxel voxel = voxelArray.get(index);
                                    voxel.addVolumeContent(volumeOfSegment);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    if (logger.isDebugEnabled()) {
                                        logger.error(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (densityPart.equals(DensityDialog.DensityPart.AXONAL)) {
            for (Neuron neuron : net.getNeuronList()) {
                Axon axon = neuron.getAxon();
                if (axon.getFirstSection() == null) {
                    return;
                }
                Section.Iterator secit = axon.getFirstSection().getIterator();
                while (secit.hasNext()) {
                    Section sec = secit.next();
                    for (Segment segment : sec.getSegments()) {
                        float volumeOfSegment = segment.getVolume();
                        //test in which voxel center lays (getVoxelIndex)
                        //add volume to this voxel
                        index = getVoxelIndex(segment.getCenter());
                        if (index != -1 && index != -2 && index != -3) {
                            try {
                                Voxel voxel = voxelArray.get(index);
                                voxel.addVolumeContent(volumeOfSegment);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                if (logger.isDebugEnabled()) {
                                    logger.error(ex);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            logger.info("NOT YET IMPLEMENTED");
        }
    }

    /** fills the Voxels of the VoxelVolume with the synaptic numbers contained in this voxel */
    public void fillVoxelVolumeWithNum() {
        if (densityPart.equals(DensityDialog.DensityPart.SYNAPTIC)) {
            logger.info("fill voxels with synapses");
            for (Cons synapse : net.getSynapseList()) {
                if (synapse.getNeuron1() == null) {
                    continue;
                }
                Segment axSeg = synapse.getNeuron1AxSegment();
                Segment denSeg = synapse.getNeuron2DenSectionSegment();
                Point3f axSegEnd = axSeg.getEnd();
                Point3f denSegEnd = denSeg.getEnd();
                Point3f center = new Point3f();
                center.add(axSegEnd, denSegEnd);
                center.scale(0.5f);

                int index = getVoxelIndex(center);
                if (index != -1 && index != -2 && index != -3) {
                    try {
                        Voxel voxel = voxelArray.get(index);
                        voxel.addNumberContent(1);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        if (logger.isDebugEnabled()) {
                            logger.error(ex);
                        }
                    }
                }
            }
        }
    }

    /** fills the Voxels of the VoxelVolume with the pixel numbers contained in this voxel */
    public void fillVoxelVolumeWithNumPixel(int[][] matrix, int layer, int grayVal, int weight) {
        //logger.info("fill voxels with pixel");
        int width = matrix.length;
        int height = matrix[0].length;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int curVal = matrix[i][j];
                if (curVal > grayVal) {
                    //System.out.println(matrix[i][j] + " ");
                    Point3f center = new Point3f(i, layer, j);
                    int index = getVoxelIndex(center);
                    if (index != -1 && index != -2 && index != -3) {
                        try {
                            Voxel voxel = voxelArray.get(index);
                            voxel.addNumberContent((float) Math.pow(curVal, weight));
                            //voxel.addNumberContent(1);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            if (logger.isDebugEnabled()) {
                                logger.error(ex);
                            }
                        }
                    }
                }
            }
        }
    }

    /** computes the length percentage of the content of the voxel */
    public void computeLengthPercentageOfVoxelContent() {
        float totalContent = 0.0f;
        float onePercent;
        float voxelContent;
        float voxelPercentContent;
        for (Voxel voxel : voxelArray) {
            totalContent += voxel.getLengthContent();
        }
        for (Voxel voxel : voxelArray) {
            voxelContent = voxel.getLengthContent();
            onePercent = totalContent / 100.0f;
            voxelPercentContent = voxelContent / onePercent;
            voxel.setLengthPercentContent(voxelPercentContent);
        }
    }

    /** computes the volume percentage of the content of the voxel */
    public void computeVolumePercentageOfVoxelContent() {
        float totalContent = 0.0f;
        float onePercent;
        float voxelContent;
        float voxelPercentContent;
        for (Voxel voxel : voxelArray) {
            totalContent += voxel.getVolumeContent();
        }
        for (Voxel voxel : voxelArray) {
            voxelContent = voxel.getVolumeContent();
            onePercent = totalContent / 100.0f;
            voxelPercentContent = voxelContent / onePercent;
            voxel.setVolumePercentContent(voxelPercentContent);
        }
    }

    /** computes the number percentage of the content of the voxel */
    public void computeNumberPercentageOfVoxelContent() {
        float totalContent = 0.0f;
        float onePercent;
        float voxelContent;
        float voxelPercentContent;
        for (Voxel voxel : voxelArray) {
            totalContent += voxel.getNumberContent();
        }
        for (Voxel voxel : voxelArray) {
            voxelContent = voxel.getNumberContent();
            onePercent = totalContent / 100.0f;
            voxelPercentContent = voxelContent / onePercent;
            voxel.setNumberPercentContent(voxelPercentContent);
        }
    }
}
