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

import org.neugen.datastructures.neuron.NeuronCalretinin;
import org.neugen.datastructures.neuron.NeuronCalbindin;
import org.neugen.datastructures.neuron.NeuronParvalbumin;
import org.neugen.datastructures.neuron.NeuronSomatostatin;
import org.neugen.datastructures.neuron.NeuronCholecystokinin;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.neuron.NeuronCA1Pyramidal;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.neuron.NeuronBase;
import org.neugen.utils.Frand;
import org.neugen.gui.NeuGenLibTask;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.gui.Trigger;

/**
 *
 * @author Sergei Wolf
 */
public final class NetHippocampus extends NetBase implements Serializable, Net {

    private static final long serialVersionUID = -7052030069294524614L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(NetHippocampus.class.getName());
    //hippocampus data
    public static final int cellTypesNumber = 6;
    // soma/mm^2 pyramidale 18150
    private transient final NetParam.NetCA1Param nCA1pyramidal;
    // oriens (106), pyramidale (349), radiatum (68)
    private transient final NetParam.NetCA1Param nCalretinin; // CR
    // oriens (44), pyramidale (0), radiatum (22), lacunosum-moleculare (32)
    private transient final NetParam.NetCA1Param nCalbindin; // CB
    // oriens (0), pyramidale (72), radiatum (25)
    private transient final NetParam.NetCA1Param nCholecystokinin; // CCK
    // oriens (125) and pyramidale (350)
    private transient final NetParam.NetCA1Param nParvalbumin;   // PV
    // oriens (121)
    private transient final NetParam.NetCA1Param nSomatostatin;  // SOM
    private float lengthX, widthY, heightZ;
    private static float strOriensHeight, strPyramidaleHeight, strRadiatumHeight, strLacMolecHeight;
    private transient NeuGenLibTask ngLibTask;

    public NetHippocampus() {
        super();
        netParam = NetParam.getInstance();
        setDrawNumber(new Frand(netParam.getSeedValue()));
        synNumbers = new long[cellTypesNumber][cellTypesNumber];

        typeCellNames.clear();
        typeCellNames.add(DataStructureConstants.CA1_PYRAMIDAL);
        typeCellNames.add(DataStructureConstants.CB_CALBINDIN);
        typeCellNames.add(DataStructureConstants.CR_CALRETININ);
        typeCellNames.add(DataStructureConstants.CCK_CHOLECYSTOKININ);
        typeCellNames.add(DataStructureConstants.PV_PARVALBUMIN);
        typeCellNames.add(DataStructureConstants.SOM_SOMATOSTATIN);


        nneuron = 0;
        nCA1pyramidal = netParam.getnCA1pyramidal();
        calcNeuronNum(nCA1pyramidal);
        nCalbindin = netParam.getnCalbindin();
        calcNeuronNum(nCalbindin);
        nCalretinin = netParam.getnCalretinin();
        calcNeuronNum(nCalretinin);
        nCholecystokinin = netParam.getnCholecystokinin();
        calcNeuronNum(nCholecystokinin);
        nParvalbumin = netParam.getnParvalbumin();
        calcNeuronNum(nParvalbumin);
        nSomatostatin = netParam.getnSomatostatin();
        calcNeuronNum(nSomatostatin);

        typeCellNumbers = new int[cellTypesNumber];
        reInitTypeCellNumbers();

        Region.Param.CA1Param ca1RegionParam = Region.Param.getInstance().getCa1Param();
        lengthX = ca1RegionParam.getLength();
        widthY = ca1RegionParam.getWidth();
        heightZ = ca1RegionParam.getHeight();

        strOriensHeight = ca1RegionParam.getStratumOriens();
        strPyramidaleHeight = ca1RegionParam.getStratumPyramidale();
        strRadiatumHeight = ca1RegionParam.getStratumRadiatum();
        strLacMolecHeight = ca1RegionParam.getStratumLacunosum();
       
        region.setCA1Size();
        ngLibTask = NeuGenLibTask.getInstance();
    }

    public void reInitTypeCellNumbers() {
        typeCellNumbers[0] = getNumCA1Pyramidal();
        typeCellNumbers[1] = getNumCalbindin();
        typeCellNumbers[2] = getNumCalretinin();
        typeCellNumbers[3] = getNumCholecystokinin();
        typeCellNumbers[4] = getNumParvalbumin();
        typeCellNumbers[5] = getNumSomatosatin();
    }

    public int getNumCA1Pyramidal() {
        return nCA1pyramidal.getNum();
    }

    public int getNumCalretinin() {
        return nCalretinin.getNum();
    }

    public int getNumCalbindin() {
        return nCalbindin.getNum();
    }

    public int getNumCholecystokinin() {
        return nCholecystokinin.getNum();
    }

    public int getNumParvalbumin() {
        return nParvalbumin.getNum();
    }

    public int getNumSomatosatin() {
        return nSomatostatin.getNum();
    }

    private void calcNeuronNum(NetParam.NetCA1Param cellNum) {
        nneuron += cellNum.getNumOriens();
        nneuron += cellNum.getNumPyramidale();
        nneuron += cellNum.getNumProximalRadiatum();
        nneuron += cellNum.getNumDistalRadiatum();
        nneuron += cellNum.getNumLacunosumMoleculare();
    }

    private void addCA1Pyramidal(Point3f somaMid, int index) {
        Neuron ca1Pyramidal = new NeuronCA1Pyramidal();
        ca1Pyramidal.setIndex(index);
        float somaRad = NeuronCA1Pyramidal.Param.getInstance().getSomaParam().getRadValue();
        ca1Pyramidal.setSoma(somaMid, somaRad);
        ca1Pyramidal.setNeuron();
        ca1Pyramidal.infoNeuron();
        neuronList.add(ca1Pyramidal);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private void addCalretinin(Point3f somaMid, int index) {
        Neuron crInterneuron = new NeuronCalretinin();
        float somaRad = crInterneuron.getParam().getSomaParam().getRadValue();
        logger.info("soma rad of calretinin: " + somaRad);
        crInterneuron.setIndex(index);
        crInterneuron.setSoma(somaMid, somaRad);
        crInterneuron.setNeuron();
        crInterneuron.infoNeuron();
        neuronList.add(crInterneuron);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private void addCalbindin(Point3f somaMid, int index) {
        Neuron cbInterneuron = new NeuronCalbindin();
        cbInterneuron.setIndex(index);
        float somaRad = cbInterneuron.getParam().getSomaParam().getRadValue();
        cbInterneuron.setSoma(somaMid, somaRad);
        cbInterneuron.setNeuron();
        cbInterneuron.infoNeuron();
        neuronList.add(cbInterneuron);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private void addCholecystokinin(Point3f somaMid, int index) {
        Neuron cckInterneuron = new NeuronCholecystokinin();
        cckInterneuron.setIndex(index);
        float somaRad = cckInterneuron.getParam().getSomaParam().getRadValue();
        cckInterneuron.setSoma(somaMid, somaRad);
        cckInterneuron.setNeuron();
        cckInterneuron.infoNeuron();
        neuronList.add(cckInterneuron);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private void addParvalbumin(Point3f somaMid, int index) {
        Neuron pvInterneuron = new NeuronParvalbumin();
        pvInterneuron.setIndex(index);
        float somaRad = pvInterneuron.getParam().getSomaParam().getRadValue();
        pvInterneuron.setSoma(somaMid, somaRad);
        pvInterneuron.setNeuron();
        pvInterneuron.infoNeuron();
        neuronList.add(index, pvInterneuron);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private void addSomatostatin(Point3f somaMid, int index) {
        Neuron somInterneuron = new NeuronSomatostatin();
        somInterneuron.setIndex(index);
        float somaRad = somInterneuron.getParam().getSomaParam().getRadValue();
        somInterneuron.setSoma(somaMid, somaRad);
        somInterneuron.setNeuron();
        somInterneuron.infoNeuron();
        neuronList.add(somInterneuron);
        if (ngLibTask != null) {
            ngLibTask.setMyProgress((0.1f + ((float) index + 1)) / nneuron * 0.1f);
        }
    }

    private Point3f getPointInStratumOriens(int cellNum) {
        Point3f somaMid = new Point3f();
        if (cellNum == 1) {
            somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
            somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
        } else {
            somaMid.x = drawNumber.fdraw() * lengthX;
            somaMid.y = drawNumber.fdraw() * widthY;
        }

        somaMid.z = drawNumber.fdraw() * strOriensHeight;
        return somaMid;
    }

    private Point3f getPointInStratumPyramidale(int cellNum) {
        Point3f somaMid = new Point3f();

        if (cellNum == 1) {
            somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
            somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
        } else {
            somaMid.x = drawNumber.fdraw() * lengthX;
            somaMid.y = drawNumber.fdraw() * widthY;
        }

        somaMid.z = ((drawNumber.fdraw() * strPyramidaleHeight) + strOriensHeight);
        //somaMid.z = ((0.5f * strPyramidaleHeight) + strOriensHeight);
        return somaMid;
    }

    private Point3f getPointInStratumProximalRadiatum(int cellNum) {
        float minHeight = strOriensHeight + strPyramidaleHeight;
        Point3f somaMid = new Point3f();

        if (cellNum == 1) {
            somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
            somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
        } else {
            somaMid.x = drawNumber.fdraw() * lengthX;
            somaMid.y = drawNumber.fdraw() * widthY;
        }

        somaMid.z = ((drawNumber.fdraw() * strRadiatumHeight / 2.0f) + minHeight);
        return somaMid;
    }

    private Point3f getPointInStratumDistalRadiatum(int cellNum) {
        float minHeight = strOriensHeight + strPyramidaleHeight + (strRadiatumHeight / 2.0f);
        Point3f somaMid = new Point3f();

        if (cellNum == 1) {
            somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
            somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
        } else {
            somaMid.x = drawNumber.fdraw() * lengthX;
            somaMid.y = drawNumber.fdraw() * widthY;
        }

        somaMid.z = ((drawNumber.fdraw() * strRadiatumHeight / 2.0f) + minHeight);
        return somaMid;
    }

    private Point3f getPointInStratumLacunosumMoleculare(int cellNum) {
        Point3f somaMid = new Point3f();

        if (cellNum == 1) {
            somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
            somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
        } else {
            somaMid.x = drawNumber.fdraw() * lengthX;
            somaMid.y = drawNumber.fdraw() * widthY;
        }

        somaMid.z = ((drawNumber.fdraw() * strLacMolecHeight) + (heightZ - strLacMolecHeight));
        return somaMid;
    }

    @Override
    public void generate() {
        nnf_synapses = 0;
        neuronList.clear();
        NeuronBase.setDrawNumber(null);
        /*
        cellOffsets = new int[7];
        cellOffsets[0] = 0;
        cellOffsets[1] = nCA1pyramidal;
        cellOffsets[2] = cellOffsets[1] + nCalretinin;
        cellOffsets[3] = cellOffsets[2] + nCalbindin;
        cellOffsets[4] = cellOffsets[3] + nCholecystokinin;
        cellOffsets[5] = cellOffsets[4] + nParvalbumin;
        cellOffsets[6] = cellOffsets[5] + nSomatostatin;
         */

        typeCellNames.clear();
        typeCellNames.add(DataStructureConstants.CA1_PYRAMIDAL);
        typeCellNames.add(DataStructureConstants.CR_CALRETININ);
        typeCellNames.add(DataStructureConstants.CB_CALBINDIN);
        typeCellNames.add(DataStructureConstants.CCK_CHOLECYSTOKININ);
        typeCellNames.add(DataStructureConstants.PV_PARVALBUMIN);
        typeCellNames.add(DataStructureConstants.SOM_SOMATOSTATIN);
        int i = 0;

        // CA1 pyramidal
        {
            int cellNum = nCA1pyramidal.getNum();
            logger.info("generate " + cellNum + " CA1pyramidal neurons");
            Point3f somaMid;

            NeuronCA1Pyramidal.deleteData();

            for (int j = 0; j < nCA1pyramidal.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addCA1Pyramidal(somaMid, i);
            }

            for (int j = 0; j < nCA1pyramidal.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addCA1Pyramidal(somaMid, i);
            }

            for (int j = 0; j < nCA1pyramidal.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addCA1Pyramidal(somaMid, i);
            }

            for (int j = 0; j < nCA1pyramidal.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addCA1Pyramidal(somaMid, i);
            }

            for (int j = 0; j < nCA1pyramidal.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addCA1Pyramidal(somaMid, i);
            }
        }

        // Calretinin (CR)
        {
            int cellNum = nCalretinin.getNum();
            logger.info("generate " + cellNum + " Calretinin (CR) interneurons");
            Point3f somaMid;

            NeuronBase.deleteData();

            for (int j = 0; j < nCalretinin.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addCalretinin(somaMid, i);
            }

            for (int j = 0; j < nCalretinin.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addCalretinin(somaMid, i);
            }

            for (int j = 0; j < nCalretinin.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addCalretinin(somaMid, i);
            }

            for (int j = 0; j < nCalretinin.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addCalretinin(somaMid, i);
            }

            for (int j = 0; j < nCalretinin.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addCalretinin(somaMid, i);
            }
        }

        // Calbindin (CB)
        {
            int cellNum = nCalbindin.getNum();
            logger.info("generate " + cellNum + " Calbindin (CB) interneurons");
            Point3f somaMid;

            NeuronBase.deleteData();

            for (int j = 0; j < nCalbindin.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addCalbindin(somaMid, i);
            }

            for (int j = 0; j < nCalbindin.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addCalbindin(somaMid, i);
            }

            for (int j = 0; j < nCalbindin.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addCalbindin(somaMid, i);
            }

            for (int j = 0; j < nCalbindin.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addCalbindin(somaMid, i);
            }

            for (int j = 0; j < nCalbindin.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addCalbindin(somaMid, i);
            }
        }

        // Cholecystokinin (CCK)
        //if (nCholecystokinin > 0)
        {
            int cellNum = nCholecystokinin.getNum();
            logger.info("generate " + cellNum + " Cholecystokinin (CCK) interneurons");
            Point3f somaMid;

            NeuronBase.deleteData();

            for (int j = 0; j < nCholecystokinin.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addCholecystokinin(somaMid, i);
            }

            for (int j = 0; j < nCholecystokinin.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addCholecystokinin(somaMid, i);
            }

            for (int j = 0; j < nCholecystokinin.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addCholecystokinin(somaMid, i);
            }

            for (int j = 0; j < nCholecystokinin.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addCholecystokinin(somaMid, i);
            }

            for (int j = 0; j < nCholecystokinin.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addCholecystokinin(somaMid, i);
            }
        }

        // Parvalbumin (PV)
        //if (nParvalbumin > 0)
        {
            int cellNum = nParvalbumin.getNum();
            logger.info("generate " + cellNum + " Parvalbumin (PV) interneurons");
            Point3f somaMid;

            NeuronBase.deleteData();

            for (int j = 0; j < nParvalbumin.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addParvalbumin(somaMid, i);
            }

            for (int j = 0; j < nParvalbumin.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addParvalbumin(somaMid, i);
            }

            for (int j = 0; j < nParvalbumin.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addParvalbumin(somaMid, i);
            }

            for (int j = 0; j < nParvalbumin.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addParvalbumin(somaMid, i);
            }

            for (int j = 0; j < nParvalbumin.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addParvalbumin(somaMid, i);
            }
        }

        // Somatostatin (SOM)
        //if (nSomatostatin > 0)
        {
            int cellNum = nSomatostatin.getNum();
            logger.info("generate " + cellNum + " Somatostatin (SOM) interneurons");
            Point3f somaMid;

            NeuronBase.deleteData();

            for (int j = 0; j < nSomatostatin.getNumOriens(); ++j, ++i) {
                somaMid = getPointInStratumOriens(cellNum);
                addSomatostatin(somaMid, i);
            }

            for (int j = 0; j < nSomatostatin.getNumPyramidale(); ++j, ++i) {
                somaMid = getPointInStratumPyramidale(cellNum);
                addSomatostatin(somaMid, i);
            }

            for (int j = 0; j < nSomatostatin.getNumProximalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumProximalRadiatum(cellNum);
                addSomatostatin(somaMid, i);
            }

            for (int j = 0; j < nSomatostatin.getNumDistalRadiatum(); ++j, ++i) {
                somaMid = getPointInStratumDistalRadiatum(cellNum);
                addSomatostatin(somaMid, i);
            }

            for (int j = 0; j < nSomatostatin.getNumLacunosumMoleculare(); ++j, ++i) {
                somaMid = getPointInStratumLacunosumMoleculare(cellNum);
                addSomatostatin(somaMid, i);
            }
        }
    }

    private int insertAxonSegment(EpsilonDB eDB, int nIdx, int listIndex) {
        int i;
        for (i = listIndex; i < nIdx; ++i) {
            Neuron n = neuronList.get(i);
            if(n.getAxon() != null) {
                if(n.getAxon().getFirstSection() != null) {
                    Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
                    while (secIter.hasNext()) {
                        Section section = secIter.next();
                        for (Segment segment : section.getSegments()) {
                            eDB.insert(new AxonSegmentData(n, section, segment));
                        }
                    }
                }
            }
        }
        return i;
    }

    private int[][] getConnectionMatrix() {

        /**
         *                  CA1 pyramidal | calbindin | calretinin | cholecystokinin | parvalbumin | somatosatin
         * CA1 pyramidal        1              1            0               1               1             1
         * calbindin            1              0            0               0               0             0
         * calretinin           0              0            0               0               0             0
         * cholecystokinin      1              0            0               0               0             0
         * parvalbumin          1              0            0               0               0             0
         * somatosatin          1              0            0               0               0             0
         *
         *
         * - parvalbumin are chandelier cells or basket cells
         * - calbindin are bistratified or radiatum-projecting cells
         * - calretinin are classified as interneuron-projecting cells (nur mit den Dendriten anderer Interneurone verbunden)
         * - cholecystokinin are basket cells
         * - somatosatin cells are o-lm cells
         *
         */
        
        int[][] connectionMatrix = {
            {1, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0}
        };


        return connectionMatrix;
    }

    @Override
    public void interconnect() {
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln("interconnect neurons");
        trigger.outPrintln("interconnect-phase");
        trigger.outPrintln("interconnect");

        NetParam netParameter = NetParam.getInstance();
        float distSynapse = netParameter.getDistSynapse();
        EpsilonDB sdbCA1Pyr = new EpsilonDB(distSynapse);
        EpsilonDB sdbCB = new EpsilonDB(distSynapse);
        EpsilonDB sdbCR = new EpsilonDB(distSynapse);
        EpsilonDB sdbCCK = new EpsilonDB(distSynapse);
        EpsilonDB sdbPV = new EpsilonDB(distSynapse);
        EpsilonDB sdbSOM = new EpsilonDB(distSynapse);

        int nCA1 = getNumCA1Pyramidal();
        int nCB = getNumCalbindin();
        int nCR = getNumCalretinin();
        int nCCK = getNumCholecystokinin();
        int nPV = getNumParvalbumin();
        int nSOM = getNumSomatosatin();

        int i = 0;
        int nIdx = nCA1;
        //1: CA1 pyramidal
        i = insertAxonSegment(sdbCA1Pyr, nIdx, i);

        //2: calbindin
        nIdx += nCB;
        i = insertAxonSegment(sdbCB, nIdx, i);

        //3: calretinin
        nIdx += nCR;
        i = insertAxonSegment(sdbCR, nIdx, i);

        //4: cholecystokinin
        nIdx += nCCK;
        i = insertAxonSegment(sdbCCK, nIdx, i);

        //5: parvalbumin
        nIdx += nPV;
        i = insertAxonSegment(sdbPV, nIdx, i);

        //6: somatosatin
        nIdx += nSOM;
        i = insertAxonSegment(sdbSOM, nIdx, i);

        float progress = 0;
        if (ngLibTask != null) {
            progress = ngLibTask.getProgress();
            progress /= 100.0f;
            ngLibTask.setMyProgress(progress);
        }

        int[][] connectionMatrix = getConnectionMatrix();

        int[] limits = {nCA1, nCB, nCR, nCCK, nPV, nSOM};
        EpsilonDB[] dbs = {sdbCA1Pyr, sdbCB, sdbCR, sdbCCK, sdbPV, sdbSOM};
        int begin = 0;

        //logger.info("1. for Schleife: durchlaufe alle Zellentypen: " + cellTypesNumber);
        // laufe ueber alle zellentypen durch
        for (int c = 0; c < cellTypesNumber; c++) {
            if (ngLibTask != null) {
                progress = 0;
                float tmp = ((float) (c + 1) / (float) cellTypesNumber);
                progress += tmp;
                if (progress >= 1.0f) {
                    progress = 0.9f;
                }
                ngLibTask.setMyProgress(progress);
            }
            //long[][] localSynNumbers = new long[cellTypesNumber][cellTypesNumber];
            long localNsynapse = 0;
            int limitsSize = limits[c] + begin;
            /*
            logger.info("limits[c]: " + limits[c]);
            logger.info("begin: " + begin);
            logger.info("limits_size: " + limits_size);
             */
            //logger.info("2. for Schleife: durchlaufe alle Neurone diese Typen");
            for (int ii = begin; ii < limitsSize; ii++) {
                Neuron neuron = neuronList.get(ii);
                //logger.info("3. for Schleife: durchlaufe alle Dendriten des Neurons: " + neuron.getDenList().size());
                for (Dendrite currentDendrite : neuron.getDendrites()) {
                    Section.Iterator secIter = currentDendrite.getFirstSection().getIterator();
                    //logger.info("secion name:" + den_j.getFirstSection().getSectionName());
                    ///< loop over all section of dendrite j of neuron2
                    // while (sectionIterator.hasNext()) {
                    //logger.info("while Schleife: Durchlaufe alle Sektionen des Dendrites");
                    while (secIter.hasNext()) {
                        Section denSection = secIter.next();
                        //section = sectionIterator.next();
                        //logger.info("section id: " + section.getSectionId());
                        //logger.info("secion name:" + section.getSectionName());
                        //logger.info("for Schleife: durchlaufe alle Segmente der Sektion: nsegs: " + nsegs);
                        for (Segment denSegment : denSection.getSegments()) {
                            Point3f end = denSegment.getEnd();
                            //logger.info("den_seg end: " + (int) v2[0] + " " + (int) v2[1] + " " + (int) v2[2]);
                            // loop over all relevant dbs
                            //logger.info("for Schleifen: durchlaufe die relevante Datenbank für jeden Segment");
                            for (int l = 0; l < cellTypesNumber; l++) {
                                if (connectionMatrix[l][c] == 1) {
                                    //logger.info("rufe get epsilon env auf!");
                                    List<AxonSegmentData> pAxSeg = dbs[l].getEpsilonEnv(end);
                                    for (int m = 0; m < pAxSeg.size(); m++) {
                                        if (pAxSeg.get(m).neuron.getIndex() != ii) {
                                            synNumbers[l][c]++;
                                            AxonSegmentData dbRec = pAxSeg.get(m);
                                            Cons co = new Cons.Builder(dbRec.segment, denSegment).neuron1(dbRec.neuron).neuron1AxSection(dbRec.section).
                                                    neuron2(neuron).neuron2DenSection(denSection).build();
                                            calculateSomaticDistance(co);
                                            synapseList.add(co);
                                            denSegment.has_ds_synapse(true);
                                            dbRec.getSegment().has_ds_synapse(true);
                                            localNsynapse++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            begin += limits[c];
            nsynapse += localNsynapse;
        } // alle Zellentypen durchgelaufen
        if (ngLibTask != null) {
            ngLibTask.setMyProgress(1.0f);
        }
        trigger.outPrintln("end of interconnect");

        // CA1 pyramidal
        /*
        for (i = 0; i < nIdx; ++i) {
        Neuron n = neuronList.get(i);
        Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
        while (secIter.hasNext()) {
        Section section = secIter.next();
        for (Segment segment : section.getSegments()) {
        sdbCA1Pyr.insert(new AxonSegmentData(n, section, segment));
        }
        }
        }

        // calbindin
        nIdx += nCB;
        for (; i < nIdx; ++i) {
        Neuron n = neuronList.get(i);
        Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
        while (secIter.hasNext()) {
        Section section = secIter.next();
        for (Segment segment : section.getSegments()) {
        sdbCB.insert(new AxonSegmentData(n, section, segment));
        }
        }
        }

        // calretinin
        nIdx += nCR;
        for (; i < nIdx; ++i) {
        Neuron n = neuronList.get(i);
        Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
        while (secIter.hasNext()) {
        Section section = secIter.next();
        for (Segment segment : section.getSegments()) {
        sdbCR.insert(new AxonSegmentData(n, section, segment));
        }
        }
        }

        // cholecystokinin
        nIdx += nCCK;
        for (; i < nIdx; ++i) {
        Neuron n = neuronList.get(i);
        Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
        while (secIter.hasNext()) {
        Section section = secIter.next();
        for (Segment segment : section.getSegments()) {
        sdbCCK.insert(new AxonSegmentData(n, section, segment));
        }
        }
        }

        // parvalbumin
        nIdx += nPV;
        for (; i < nIdx; ++i) {
        Neuron n = neuronList.get(i);
        Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
        while (secIter.hasNext()) {
        Section section = secIter.next();
        for (Segment segment : section.getSegments()) {
        sdbPV.insert(new AxonSegmentData(n, section, segment));
        }
        }
        }
         *
         */

    }

    @Override
    public Map<String, Float> computeAPSN() {
        int[][] preSynNumbers = new int[cellTypesNumber][cellTypesNumber];
        Neuron currentNeuron = null;
        Set<Neuron> neurons = new HashSet<Neuron>();
        // Iterate over the synapses.
        for (Cons con : synapseList) {
            if (con.getNeuron1() == null) {
                continue;
            }
            if (currentNeuron == null) {
                currentNeuron = con.getNeuron2();
                neurons.add(con.getNeuron1());
            } else {
                if (con.getNeuron2().getIndex() != currentNeuron.getIndex()) {
                    // Iterate over presynaptic sides
                    // which are sorted in ascending order due to using set
                    for (Neuron neuron_j : neurons) {
                        int tPre = getTypeOfNeuron(neuron_j.getIndex());
                        int tPost = getTypeOfNeuron(currentNeuron.getIndex());
                        preSynNumbers[tPre - 1][tPost - 1]++;
                    }
                    currentNeuron = con.getNeuron2();
                    neurons.clear();
                }
                neurons.add(con.getNeuron1());
            }
        }
        //logger.info("neurons size: " + neurons.size());
        // Iterate over presynaptic sides
        // which are sorted in ascending order due to using set
        for (Neuron neuron_j : neurons) {
            int tPre = getTypeOfNeuron(neuron_j.getIndex());
            //logger.info("j:" + j);
            //logger.info("tPre: " + tPre);
            //logger.info("currentNeuron: " + currentNeuron);
            int tPost = getTypeOfNeuron(currentNeuron.getIndex());
            //logger.info("tPost: " + tPost);
            preSynNumbers[tPre - 1][tPost - 1]++;
        }
        Map<String, Float> ret = new HashMap<String, Float>();
        for (int i = 0; i < cellTypesNumber; i++) {
            for (int j = 0; j < cellTypesNumber; j++) {
                String key = (typeCellNames.get(i) + "->") + typeCellNames.get(j);
                //logger.info("key: " + key);
                //logger.info("j:  " + j + " ,typeCellNumbers[j]: " + typeCellNumbers[j]);
                if (typeCellNumbers[j] != 0) {
                    float val = (float) preSynNumbers[i][j] / typeCellNumbers[j];
                    ret.put(key, val);
                }
            }
        }
        return ret;
    }

    public enum NeuronTypes {

        CA1_PYRAMIDAL(1), CB_CALBINDIN(2), CR_CALRETININ(3), CCK_Cholecystokinin(4), PV_Parvalbumin(5), SOM_Somatosatin(6);
        private int num;

        public int getNum() {
            return num;
        }

        NeuronTypes(int num) {
            this.num = num;
        }
    }

    /**
     * Function to get the type of a neuron from its index in neuron_list,
     */
    @Override
    public int getTypeOfNeuron(int indexOfNeuron) {
        logger.info("index of this neuron is: " + indexOfNeuron);
        int typeOfNeuron = -1;
        int nCA1 = getNumCA1Pyramidal();
        int nCB = getNumCalbindin();
        int nCR = getNumCalretinin();
        int nCCK = getNumCholecystokinin();
        int nPV = getNumParvalbumin();
        int nSOM = getNumSomatosatin();

        if ((indexOfNeuron >= 0) && (indexOfNeuron < nCA1)) {
            return NeuronTypes.CA1_PYRAMIDAL.getNum();
        }

        int nIdx = nCA1 + nCB;
        if ((indexOfNeuron >= nCA1) && (indexOfNeuron < nIdx)) {
            return NeuronTypes.CB_CALBINDIN.getNum();
        }

        nIdx += nCR;
        if ((indexOfNeuron >= nIdx-nCR && (indexOfNeuron < nIdx))) {
            return NeuronTypes.CR_CALRETININ.getNum();
        }

        nIdx += nCCK;
        if ((indexOfNeuron >= nIdx-nCCK && (indexOfNeuron < nIdx))) {
            return NeuronTypes.CCK_Cholecystokinin.getNum();
        }

        nIdx += nPV;
        if ((indexOfNeuron >= nIdx-nPV && (indexOfNeuron < nIdx))) {
            return NeuronTypes.PV_Parvalbumin.getNum();
        }

        nIdx += nSOM;
        if ((indexOfNeuron >= nIdx-nSOM && (indexOfNeuron < nIdx))) {
            return NeuronTypes.SOM_Somatosatin.getNum();
        }

        return typeOfNeuron;
    }

    @Override
    public WriteToHoc getHocData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
