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

import org.neugen.parsers.NGX.WriteToNGX;
import org.neugen.datastructures.neuron.NeuronL5APyramidal;
import org.neugen.datastructures.neuron.NeuronL5BPyramidal;
import org.neugen.datastructures.neuron.NeuronStarpyramidal;
import org.neugen.datastructures.neuron.NeuronL4stellate;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.neuron.NeuronL23Pyramidal;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.neuron.NeuronBase;
import org.neugen.utils.Frand;
import org.neugen.gui.NeuGenLibTask;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.NeuGenView;
import org.neugen.gui.Trigger;
import org.neugen.parsers.HOCUtil;
import org.neugen.parsers.HocWriter;
import org.neugen.parsers.NGX.NGXAlphaSynapse;
import org.neugen.parsers.NGX.NGXExp2Synapse;
import org.neugen.parsers.NGX.NGXSynapse;
import org.neugen.parsers.TXT.TXTExp2Synapse;
import org.neugen.parsers.TXT.TXTSynapse;
import org.neugen.parsers.TXT.WriteToTXT;

/**
 * @author Jens Eberhard
 * @author Simone Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public final class NetNeocortex extends NetBase implements Serializable, Net {

    private static final long serialVersionUID = -7041930070394524614L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(NetNeocortex.class.getName());
    //neocortex data
    public static final int cellTypesNumber = 5;
    private int nL4stellate;
    private int npyramidal;
    private int nL23pyramidal;
    private int nL5pyramidal;
    private int nL5Apyramidal;
    private int nL5Bpyramidal;
    private int nstarpyramidal;

    public NetNeocortex() {
        super();
        region.setColumnSize();
        netParam = NetParam.getInstance();
        setDrawNumber(new Frand(netParam.getSeedValue()));

        nL4stellate = netParam.getNumL4stellate();
        nL23pyramidal = netParam.getNumL23pyramidal();
        nL5Apyramidal = netParam.getNumL5Apyramidal();
        nL5Bpyramidal = netParam.getNumL5Bpyramidal();
        nstarpyramidal = netParam.getNumStarpyramidal();
        nL5pyramidal = nL5Apyramidal + nL5Bpyramidal;
        npyramidal = nL23pyramidal + nL5pyramidal;
        nneuron = nL4stellate + npyramidal + nstarpyramidal;

        synNumbers = new long[cellTypesNumber][cellTypesNumber];
        typeCellNames.clear();
        typeCellNames.add(DataStructureConstants.L4_STELLATE);
        typeCellNames.add(DataStructureConstants.L23_PYRAMIDAL);
        typeCellNames.add(DataStructureConstants.L5A_PYRAMIDAL);
        typeCellNames.add(DataStructureConstants.L5B_PYRAMIDAL);
        typeCellNames.add(DataStructureConstants.STAR_PYRAMIDAL);

        typeCellNumbers = new int[cellTypesNumber];
        reInitTypeCellNumbers();

        cellOffsets = new int[6];
        cellOffsets[0] = 0;
        reInitCellOffsets();
    }

    public void reInitCellOffsets() {
        cellOffsets[1] = nL4stellate;
        cellOffsets[2] = cellOffsets[1] + nL23pyramidal;
        cellOffsets[3] = cellOffsets[2] + nL5Apyramidal;
        cellOffsets[4] = cellOffsets[3] + nL5Bpyramidal;
        cellOffsets[5] = cellOffsets[4] + nstarpyramidal;
    }

    public void reInitTypeCellNumbers() {
        typeCellNumbers[0] = nL4stellate;
        typeCellNumbers[1] = nL23pyramidal;
        typeCellNumbers[2] = nL5Apyramidal;
        typeCellNumbers[3] = nL5Bpyramidal;
        typeCellNumbers[4] = nstarpyramidal;
    }

    public enum NeuronTypes {

        L4_STELLATE(1), L23_PYRAMIDAL(2), L5A_PYRAMIDAL(3), L5B_PYRAMIDAL(4), L4_STAR_PYRAMIDAL(5);
        private int num;

        public int getNum() {
            return num;
        }

        NeuronTypes(int num) {
            this.num = num;
        }
    }

    @Override
    public void generate() {
        NeuGenLibTask ngLibTask = NeuGenLibTask.getInstance();
        Region.Param.ColumnParam columnPar = Region.Param.getInstance().getColumnParam();
        float lengthX = columnPar.getLength();
        float widthY = columnPar.getWidth();
        float heightZ = columnPar.getHeight();

        float layer1Height = columnPar.getLayer1();
        float layer23Height = columnPar.getLayer23();
        float layer4Height = columnPar.getLayer4();
        float layer5AHeight = columnPar.getLayer5A();
        float layer5BHeight = columnPar.getLayer5B();
        float layer6Height = columnPar.getLayer6();

        nnf_synapses = 0;
        if (!neuronList.isEmpty()) {
            neuronList.clear();
        }

        NeuronBase.setDrawNumber(null);

        int i = 0;
        if (nL4stellate > 0) {
            NeuronL4stellate.deleteData();
            logger.info("generate " + nL4stellate + " L4stellate neurons");
            float l4start = layer6Height + layer5BHeight + layer5AHeight;
            float somaRad = NeuronL4stellate.Param.getInstance().getSomaParam().getRadValue();
            if (layer4Height > 0 && layer1Height > somaRad) {
                for (; i < cellOffsets[1]; ++i) {
                    Neuron l4Stellate = new NeuronL4stellate();
                    l4Stellate.setIndex(i);
                    /*
                    if(testSomaCollision(l4Stellate, somaMid, somaRad)) {
                    somaMid.x = lengthX * drawNumber.fdraw();
                    somaMid.y = widthY * drawNumber.fdraw();
                    somaMid.z = l4start + 50.0f + 100.0f * drawNumber.fdraw();
                    }
                    if(testSomaCollision(l4Stellate, somaMid, somaRad)) {
                    trigger.outPrintln("zelle verwerfen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    this.nL4stellate--;
                    continue;
                    }
                     */
                    Point3f somaMid = new Point3f();
                    if (nL4stellate == 1) {
                        somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
                        somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
                    } else {
                        somaMid.x = lengthX * drawNumber.fdraw();
                        somaMid.y = widthY * drawNumber.fdraw();
                    }
                    somaMid.z = l4start + layer4Height / 2.0f * drawNumber.fdraw();
                    l4Stellate.setSoma(somaMid, somaRad);
                    l4Stellate.setNeuron();
                    l4Stellate.infoNeuron();
                    neuronList.add(i, l4Stellate);
                    if (ngLibTask != null) {
                        ngLibTask.setMyProgress((0.1f + ((float) i + 1)) / nneuron * 0.1f);
                    }
                }
            } else {
                nL4stellate = 0;
            }
        }

        if (nL23pyramidal > 0) {
            logger.info("generate " + nL23pyramidal + " L23pyramidal neurons");
            NeuronL23Pyramidal.deleteData();
            float l23start = layer6Height + layer5BHeight + layer5AHeight + layer4Height;
            float somaRad = NeuronL23Pyramidal.Param.getInstance().getSomaParam().getRadValue();
            if (layer23Height > 0 && layer23Height > somaRad) {
                for (; i < cellOffsets[2]; ++i) {
                    Neuron l23Pyramidal = new NeuronL23Pyramidal();
                    l23Pyramidal.setIndex(i);
                    //testSomaCollision(l23Pyramidal, somaMid, somaRad);
                    Point3f somaMid = new Point3f();
                    if (nL23pyramidal == 1) {
                        somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
                        somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
                    } else {
                        somaMid.x = lengthX * drawNumber.fdraw();
                        somaMid.y = widthY * drawNumber.fdraw();
                    }
                    somaMid.z = l23start + layer23Height / 2.0f * drawNumber.fdraw();
                    l23Pyramidal.setSoma(somaMid, somaRad);
                    l23Pyramidal.setNeuron();
                    l23Pyramidal.infoNeuron();
                    neuronList.add(i, l23Pyramidal);
                    if (ngLibTask != null) {
                        ngLibTask.setMyProgress((0.1f + ((float) i + 1)) / nneuron * 0.1f);
                    }
                }
            } else {
                nL23pyramidal = 0;
            }
        }

        if (nL5Apyramidal > 0) {
            NeuronL5APyramidal.deleteData();
            logger.info("generate " + nL5Apyramidal + " L5Apyramidal neurons");
            float l5Astart = layer6Height + layer5BHeight;
            float somaRad = NeuronL5APyramidal.Param.getInstance().getSomaParam().getRadValue();
            if (layer5AHeight > 0 && layer5AHeight > somaRad) {
                for (; i < cellOffsets[3]; ++i) {
                    Neuron l5APyramidal = new NeuronL5APyramidal();
                    l5APyramidal.setIndex(i);
                    //testSomaCollision(l5APyramidal, somaMid, somaRad);
                    //l5APyramidal.setSoma(somaMid, somaRad);
                    Point3f somaMid = new Point3f();
                    if (nL5Apyramidal == 1) {
                        somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
                        somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
                    } else {
                        somaMid.x = lengthX * drawNumber.fdraw();
                        somaMid.y = widthY * drawNumber.fdraw();
                    }
                    somaMid.z = l5Astart + layer5AHeight / 2.0f * drawNumber.fdraw();
                    l5APyramidal.setSoma(somaMid, somaRad);
                    l5APyramidal.setNeuron();
                    l5APyramidal.infoNeuron();
                    neuronList.add(i, l5APyramidal);
                    if (ngLibTask != null) {
                        ngLibTask.setMyProgress((0.1f + ((float) i + 1)) / nneuron * 0.1f);
                    }
                }
            } else {
                nL5Apyramidal = 0;
            }
        }

        if (nL5Bpyramidal > 0) {
            NeuronL5BPyramidal.deleteData();
            logger.info("generate " + nL5Bpyramidal + " L5Bpyramidal neurons");
            float l5Bstart = layer6Height;
            //Point3f somaMid = new Point3f(lengthX / 2.0f, widthY / 2.0f, 500.0f);
            float somaRad = NeuronL5BPyramidal.Param.getInstance().getSomaParam().getRadValue();
            if (layer5BHeight > 0 && layer5BHeight > somaRad) {
                for (; i < cellOffsets[4]; ++i) {
                    Neuron l5BPyramidal = new NeuronL5BPyramidal();
                    l5BPyramidal.setIndex(i);
                    //testSomaCollision(l5BPyramidal, somaMid, somaRad);
                    Point3f somaMid = new Point3f();
                    if (nL5Bpyramidal == 1) {
                        somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
                        somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
                    } else {
                        somaMid.x = lengthX * drawNumber.fdraw();
                        somaMid.y = widthY * drawNumber.fdraw();
                    }
                    somaMid.z = l5Bstart + layer5BHeight / 2.0f * drawNumber.fdraw();
                    l5BPyramidal.setSoma(somaMid, somaRad);
                    l5BPyramidal.setNeuron();
                    l5BPyramidal.infoNeuron();
                    neuronList.add(i, l5BPyramidal);
                    if (ngLibTask != null) {
                        ngLibTask.setMyProgress((0.1f + ((float) i + 1)) / nneuron * 0.1f);
                    }
                }
            } else {
                nL5Bpyramidal = 0;
            }
        }

        if (nstarpyramidal > 0) {
            NeuronStarpyramidal.deleteData();
            logger.info("generate " + nstarpyramidal + " starpyramidal neurons");
            float l4start = layer6Height + layer5BHeight + layer5AHeight;
            for (; i < cellOffsets[5]; ++i) {
                Neuron starPyramidal = new NeuronStarpyramidal();
                starPyramidal.setIndex(i);
                float somaRad = NeuronStarpyramidal.Param.getInstance().getSomaParam().getRadValue();
                //testSomaCollision(starPyramidal, somaMid, somaRad);
                Point3f somaMid = new Point3f();
                if (nstarpyramidal == 1) {
                    somaMid.x = lengthX / 2.0f + drawNumber.fpm_onedraw() * lengthX / 10.0f;
                    somaMid.y = widthY / 2.0f + drawNumber.fpm_onedraw() * widthY / 10.0f;
                } else {
                    somaMid.x = lengthX * drawNumber.fdraw();
                    somaMid.y = widthY * drawNumber.fdraw();
                }
                somaMid.z = l4start + layer4Height / 2.0f * drawNumber.fdraw();
                starPyramidal.setSoma(somaMid, somaRad);
                starPyramidal.setNeuron();
                starPyramidal.infoNeuron();
                neuronList.add(i, starPyramidal);
                if (ngLibTask != null) {
                    ngLibTask.setMyProgress((0.1f + ((float) i + 1)) / nneuron * 0.1f);
                }
            }
        }
        setTotalNumOfSegments();
    }

    public boolean testSomaCollision(Neuron target, Point3f somaMid, float somaRad) {
        for (Neuron curNeuro : neuronList) {
            boolean collide = curNeuro.getSoma().collide(somaMid, somaRad);
            if (collide) {
                //curNeuro.setCollide(collide);
                //target.setCollide(collide);
                logger.info("collide is true");
                return true;
            }
        }
        return false;
    }

    /**
     * Function for interconnecting the net for a cortical column.
     * It connects the neurons with each other
     * and computes the number of synapses.
     */
    @Override
    public void interconnect() {
	if (NeuGenConstants.WITH_GUI) {
        	Trigger trigger = Trigger.getInstance();
        	trigger.outPrintln("interconnect neurons");
        	trigger.outPrintln("interconnect-phase");
        	trigger.outPrintln("interconnect");
	}

        NetParam netParameter = NetParam.getInstance();
        float distSynapse = netParameter.getDistSynapse();
        EpsilonDB sdbL4Stel = new EpsilonDB(distSynapse);
        EpsilonDB sdbL23Pyr = new EpsilonDB(distSynapse);
        EpsilonDB sdbL5A = new EpsilonDB(distSynapse);
        EpsilonDB sdbL5B = new EpsilonDB(distSynapse);
        EpsilonDB sdbStar = new EpsilonDB(distSynapse);
        int i = 0;
        for (i = 0; i < nL4stellate; ++i) {
            Neuron n = neuronList.get(i);
            Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
            while (secIter.hasNext()) {
                Section section = secIter.next();
                for (Segment segment : section.getSegments()) {
                    sdbL4Stel.insert(new AxonSegmentData(n, section, segment));
                }
            }
        }

        for (; i < nL4stellate + nL23pyramidal; ++i) {
            Neuron n = neuronList.get(i);
            Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
            while (secIter.hasNext()) {
                Section section = secIter.next();
                for (Segment segment : section.getSegments()) {
                    sdbL23Pyr.insert(new AxonSegmentData(n, section, segment));
                }
            }
        }

        for (; i < nL4stellate + nL23pyramidal + nL5Apyramidal; ++i) {
            Neuron n = neuronList.get(i);
            Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
            while (secIter.hasNext()) {
                Section section = secIter.next();
                for (Segment segment : section.getSegments()) {
                    sdbL5A.insert(new AxonSegmentData(n, section, segment));
                }
            }
        }

        for (; i < nL4stellate + npyramidal; ++i) {
            Neuron n = neuronList.get(i);
            Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
            while (secIter.hasNext()) {
                Section section = secIter.next();
                for (Segment segment : section.getSegments()) {
                    sdbL5B.insert(new AxonSegmentData(n, section, segment));
                }
            }
        }

        for (; i < nL4stellate + npyramidal + nstarpyramidal; i++) {
            Neuron n = neuronList.get(i);
            Section.Iterator secIter = n.getAxon().getFirstSection().getIterator();
            while (secIter.hasNext()) {
                Section section = secIter.next();
                for (Segment segment : section.getSegments()) {
                    sdbStar.insert(new AxonSegmentData(n, section, segment));
                }
            }
        }

        int[][] connectionMatrix = {
            {1, 1, 1, 0, 1},
            {0, 1, 1, 1, 1},
            {0, 1, 1, 0, 1},
            {0, 0, 0, 1, 1},
            {1, 1, 1, 1, 1}
        };

        NeuGenLibTask ngLibTask = NeuGenLibTask.getInstance();
        float progress = 0;
        if (ngLibTask != null) {
            progress = ngLibTask.getProgress();
            progress /= 100.0f;
            ngLibTask.setMyProgress(progress);
        }

        int[] limits = {nL4stellate, nL23pyramidal, nL5Apyramidal, nL5Bpyramidal, nstarpyramidal};
        EpsilonDB[] dbs = {sdbL4Stel, sdbL23Pyr, sdbL5A, sdbL5B, sdbStar};
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
					    
                                            Cons co = new Cons.Builder(dbRec.segment, denSegment)
						    .neuron1(dbRec.neuron).neuron1AxSection(dbRec.section)
						    .neuron2(neuron).neuron2DenSection(denSection)
						    .build();
					    
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
	if (NeuGenConstants.WITH_GUI) {
		Trigger trigger = Trigger.getInstance();
        	trigger.outPrintln("end of interconnect");
	}
    }

    public int getNumL23pyramidal() {
        return nL23pyramidal;
    }

    public int getNumL4stellate() {
        return nL4stellate;
    }

    public int getNumL5Apyramidal() {
        return nL5Apyramidal;
    }

    public int getNumL5Bpyramidal() {
        return nL5Bpyramidal;
    }

    public int getNumL5pyramidal() {
        return nL5pyramidal;
    }

    public int getNumPyramidal() {
        return npyramidal;
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

    /**
     * Function to get the type of a neuron from its index in neuron_list,
     * 1=L2/3, 2=L4, 3=L5A, 4=L5B, 5=star  (simone)
     */
    @Override
    public int getTypeOfNeuron(int indexOfNeuron) {
        int typeOfNeuron = -1;
        //order of indices of neurons in neuron_list
        int a = nL4stellate + nL23pyramidal;
        int b = a + nL5Apyramidal;
        int c = b + nL5Bpyramidal;
        int d_loc = c + nstarpyramidal; //nneuron

        //L4
        if ((0 <= indexOfNeuron) && (indexOfNeuron < nL4stellate)) {
            typeOfNeuron = NeuronTypes.L4_STELLATE.getNum();
            //logger.info("neuron type L4: " + typeOfNeuron);
            return typeOfNeuron;
        }
        //L2/3
        if ((nL4stellate <= indexOfNeuron) && (indexOfNeuron < a)) {
            typeOfNeuron = NeuronTypes.L23_PYRAMIDAL.getNum();
            //logger.info("neuron type L2/3: " + typeOfNeuron);
            return typeOfNeuron;
        }
        //L5A
        if ((a <= indexOfNeuron) && (indexOfNeuron < b)) {
            typeOfNeuron = NeuronTypes.L5A_PYRAMIDAL.getNum();
            //logger.info("neuron type L5A: " + typeOfNeuron);
            return typeOfNeuron;
        }
        //L5B
        if ((b <= indexOfNeuron) && (indexOfNeuron < c)) {
            typeOfNeuron = NeuronTypes.L5B_PYRAMIDAL.getNum();
            //logger.info("neuron type L5B: " + typeOfNeuron);
            return typeOfNeuron;
        }
        //star
        if ((c <= indexOfNeuron) && (indexOfNeuron < d_loc)) {
            typeOfNeuron = NeuronTypes.L4_STAR_PYRAMIDAL.getNum();
            //logger.info("neuron type STAR: " + typeOfNeuron);
            return typeOfNeuron;
        }
        //logger.info("no neuron: " + typeOfNeuron);
        return typeOfNeuron;
    }

    /**
     * @brief provide NGX data
     * @return WriteToNGX
     */
    @Override
    public WriteToNGX getNGXData() {
	    return new WriteNGXData();
    }
    
    @Override
    public WriteToTXT getTXTData() {
	    return new WriteTXTData();
    }
    
    public class WriteTXTData implements WriteToTXT {
	     public float get_uEPSP_Value(int typeN1, int typeN2) {
            float uEPSP = -1.0000f; //unitary EPSP variable for the various neuron type combinations

            //which type combination? -> which uEPSP?
            if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L4_STELLATE.ordinal())) {
                uEPSP = 0.0016f;
                //cout << "typeN1: L4, typeN2: L4, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0007f;
                //cout << "typeN1: L4, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0006f;
                //cout << "typeN1: L4, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0010f;
                //cout << "typeN1: L2/3, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0008f;
                //cout << "typeN1: L2/3, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0003f;
                //cout << "typeN1: L2/3, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0020f;
                //cout << "typeN1: L5A, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0005f;
                //cout << "typeN1: L5A, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5B_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0013f;
                //cout << "typeN1: L5B, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else {
                uEPSP = 0.0010f;
                //cout << "typeN1: " << typeN1 << ", typeN2: " << typeN2 << ", uEPSP: ??? " << uEPSP << endl;
            }
            return uEPSP;
        }	    

	     
	    
	@Override
	public ArrayList<TXTSynapse> writeExp2Synapses() {
		ArrayList<TXTSynapse> txtsynapses = new ArrayList<TXTSynapse>();
		float wfactor = 0.001f;
		List<Cons> synapseList = getSynapseList();
		for (int j = 0; j < synapseList.size(); j++) {
			Cons synapse = synapseList.get(j);
			int typeN1 = -1;
			int typeN2 = -1;
			if (synapse.getNeuron1() == null) {
				continue;
			}

                	typeN1 = getTypeOfNeuron(synapse.getNeuron1().getIndex());
                	typeN2 = getTypeOfNeuron(synapse.getNeuron2().getIndex());

 	                int n_idx = synapse.getNeuron2().getIndex();

			int c_idx = 0;
			for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
			    if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
				break;
			    }
			    c_idx++;
			}

			int sec_id = synapse.getNeuron2DenSection().getId();
			float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);

						TXTExp2Synapse ngxsynapse = new TXTExp2Synapse();
			Section dend_section = synapse.getNeuron2DenSection();
			int to_index = (int) (dend_section.getSegments().size() * dd);
			
float to_walk2 = dend_section.getLength() * dd;
float to_walk_on_given_edge_dend = 0;
			//System.err.println("to_walk: " + to_walk2);
			int index2 = 0;
			float walked2 = 0f;
			for (int i = 0; i < dend_section.getSegments().size()-1; i++) {
				Point3f p1 = dend_section.getSegments().get(i).getStart();
				Point3f p2 = dend_section.getSegments().get(i+1).getStart();
				Vector3f diff = new Vector3f(p1);
				diff.sub(p2);
				//System.err.println("diff length: " + diff.length());
				walked2 += diff.length();
				if (walked2 >= to_walk2) {
					index2 = i;
					to_walk_on_given_edge_dend = (((walked2 - (walked2 - diff.length())) - (walked2 - to_walk2)) / diff.length());
					System.err.println("to walk on given edge dend:" + to_walk_on_given_edge_dend);
					break;
				}
			}
			//System.err.println("Index2 i: " + index2);
			
			dend_section.getLength();
		

			ngxsynapse.setTo_point_start(dend_section.getSegments().get(index2).getStart());
			ngxsynapse.setTo_Index(index2);
			if (dend_section.getSegments().size() == 1) {
				ngxsynapse.setTo_point_end(dend_section.getSegments().get((index2)).getEnd());
			} else {
				ngxsynapse.setTo_point_end(dend_section.getSegments().get((index2+1)).getStart());
			}
			
			//ngxsynapse.setTo_point_start(dend_section.getSegments().get(0).getStart());
		//	ngxsynapse.setTo_point_end(dend_section.getSegments().get(0).getEnd());
			//System.err.println("segments: " + dend_section.getSegments().size());
			//System.err.println("to start: " + ngxsynapse.getTo_point_start());
			//System.err.println("to end: " + ngxsynapse.getTo_point_end());
			//ngxsynapse.setName("Synapse" + j);
			//ngxsynapse.setId(j);
			ngxsynapse.setTo("N" + n_idx + "dendrite" + sec_id);
		//	ngxsynapse.setTo_loc(dd);
			ngxsynapse.setTo_loc(to_walk_on_given_edge_dend);

                	Section ax_section = synapse.getNeuron1AxSection();
		
                	assert (ax_section.getLength() > 0.0);
                	int axSegPos = 0;
                	for (Segment axSeg : ax_section.getSegments()) {
                    	if (axSeg.getId() == synapse.getNeuron1AxSegment().getId()) {
                        	break;
                    	}
                    		axSegPos++;
                	}
		
			/**
			 * @todo from and to is interchanged here!
			 */
			float ax_local_position = (ax_section.getLength() * axSegPos) / ax_section.getLength();
                	assert (!Float.isInfinite(ax_local_position));
			
		
			float ff = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron1AxSection(), axSegPos);
			assert (ff <= 1.0);
			//System.err.println("ax_local_pos: " + ax_local_position);
			//System.err.println("ff: " + ff);
			int from_index = (int) (ax_section.getSegments().size() * ff);
			
			float to_walk = ax_section.getLength() * ff;
			float to_walk_on_given_edge_axon = 0;
			//System.err.println("to_walk: " + to_walk);
			int index = 0;
			float walked = 0f;
			for (int i = 0; i < ax_section.getSegments().size()-1; i++) {
				Point3f p1 = ax_section.getSegments().get(i).getStart();
				Point3f p2 = ax_section.getSegments().get(i+1).getStart();
				Vector3f diff = new Vector3f(p1);
				diff.sub(p2);
			//	System.err.println("diff length: " + diff.length());
				walked += diff.length();
				if (walked >= to_walk) {
					index = i;
					to_walk_on_given_edge_axon = (((walked - (walked - diff.length())) - (walked - to_walk)) / diff.length());
					//to_walk_on_given_edge = (walked - diff.length() - to_walk) / diff.length();
					System.err.println("to walk on given edge axon:" + to_walk_on_given_edge_axon);
					break;
				}
			}
			//System.err.println("Index i: " + index);
			
			ax_section.getLength();
			
			
			/**
			 * @TODO validate this to be correct!
			 */
			//ngxsynapse.setFrom_point_start(ax_section.getSegments().get(0).getStart());
			//ngxsynapse.setFrom_point_end(ax_section.getSegments().get(0).getEnd());
			ngxsynapse.setFrom_point_start(ax_section.getSegments().get(index).getStart());
			ngxsynapse.setFrom_Index(index);
			if (ax_section.getSegments().size() == 1) {
				ngxsynapse.setFrom_point_end(ax_section.getSegments().get((index)).getEnd());
			} else {
				ngxsynapse.setFrom_point_end(ax_section.getSegments().get((index+1)).getStart());
			}
			/*System.err.println("segments: " + ax_section.getSegments().size());
			System.err.println("from start: " + ngxsynapse.getFrom_point_start());
			System.err.println("from end: " + ngxsynapse.getFrom_point_end());*/
			
			
			ngxsynapse.setFrom("N" + synapse.getNeuron1().getIndex() + ax_section.getName());
			//ngxsynapse.setFrom_loc(ff);
			ngxsynapse.setFrom_loc(to_walk_on_given_edge_axon);
			ngxsynapse.setGmax(1 + wfactor * synapse.getDendriticSomaDistance() * get_uEPSP_Value(typeN1, typeN2));
			
			txtsynapses.add(ngxsynapse);
		}
		return txtsynapses;
	}

		@Override
		public ArrayList<TXTSynapse> writeAlphaSynapses() {
			/// todo
			return new ArrayList<TXTSynapse>();
		}
    }
    
    /**
     * @brief provide exp2synapses and alphasynapses
     */
    @SuppressWarnings("PublicInnerClass")
    public class WriteNGXData implements WriteToNGX {
	 public float get_uEPSP_Value(int typeN1, int typeN2) {
            float uEPSP = -1.0000f; //unitary EPSP variable for the various neuron type combinations

            //which type combination? -> which uEPSP?
            if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L4_STELLATE.ordinal())) {
                uEPSP = 0.0016f;
                //cout << "typeN1: L4, typeN2: L4, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0007f;
                //cout << "typeN1: L4, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0006f;
                //cout << "typeN1: L4, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0010f;
                //cout << "typeN1: L2/3, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0008f;
                //cout << "typeN1: L2/3, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0003f;
                //cout << "typeN1: L2/3, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0020f;
                //cout << "typeN1: L5A, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0005f;
                //cout << "typeN1: L5A, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5B_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0013f;
                //cout << "typeN1: L5B, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else {
                uEPSP = 0.0010f;
                //cout << "typeN1: " << typeN1 << ", typeN2: " << typeN2 << ", uEPSP: ??? " << uEPSP << endl;
            }
            return uEPSP;
        }	    

	    
	@Override
	public ArrayList<NGXSynapse> writeExp2Synapses() {
		ArrayList<NGXSynapse> ngxsynapses = new ArrayList<NGXSynapse>();
		float wfactor = 0.001f;
		List<Cons> synapseList = getSynapseList();
		for (int j = 0; j < synapseList.size(); j++) {
			Cons synapse = synapseList.get(j);
			int typeN1 = -1;
			int typeN2 = -1;
			if (synapse.getNeuron1() == null) {
				continue;
			}

                	typeN1 = getTypeOfNeuron(synapse.getNeuron1().getIndex());
                	typeN2 = getTypeOfNeuron(synapse.getNeuron2().getIndex());

 	                int n_idx = synapse.getNeuron2().getIndex();

			int c_idx = 0;
			for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
			    if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
				break;
			    }
			    c_idx++;
			}

			int sec_id = synapse.getNeuron2DenSection().getId();
			float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
			
			NGXExp2Synapse ngxsynapse = new NGXExp2Synapse();
			ngxsynapse.setName("Synapse" + j);
			ngxsynapse.setId(j);
			ngxsynapse.setTo("N" + n_idx + "dendrite" + sec_id);
			ngxsynapse.setTo_loc(dd);

			Section section = synapse.getNeuron2DenSection();
			ngxsynapse.set_starting_point(section.getSegments().get(0).getStart());
			
                	Section ax_section = synapse.getNeuron1AxSection();
			ngxsynapse.set_ending_point(ax_section.getSegments().get(0).getStart());
			
                	assert (ax_section.getLength() > 0.0);
                	int axSegPos = 0;
                	for (Segment axSeg : ax_section.getSegments()) {
                    	if (axSeg.getId() == synapse.getNeuron1AxSegment().getId()) {
                        	break;
                    	}
                    		axSegPos++;
                	}
		
			/**
			 * @todo from and to is interchanged here!
			 */
			float ax_local_position = (ax_section.getLength() * axSegPos) / ax_section.getLength();
                	assert (!Float.isInfinite(ax_local_position));
		
			float ff = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron1AxSection(), axSegPos);
			assert (ff <= 1.0);
			ngxsynapse.setFrom("N" + synapse.getNeuron1().getIndex() + ax_section.getName());
			ngxsynapse.setFrom_loc(ff);
			ngxsynapse.setGmax(1 + wfactor * synapse.getDendriticSomaDistance() * get_uEPSP_Value(typeN1, typeN2));
			
			ngxsynapses.add(ngxsynapse);
		}
		return ngxsynapses;
	}

	@Override
	public ArrayList<NGXSynapse> writeAlphaSynapses() {
		ArrayList<NGXSynapse> synapses = new ArrayList<NGXSynapse>();
		synapses.addAll(writeAlphaSynapses(NeuronTypes.L4_STELLATE));
		synapses.addAll(writeAlphaSynapses(NeuronTypes.L23_PYRAMIDAL));
		synapses.addAll(writeAlphaSynapses(NeuronTypes.L5B_PYRAMIDAL));
		return synapses;
	}
	
	public ArrayList<NGXSynapse> writeAlphaSynapses(NeuronTypes neuronType) {
		ArrayList<NGXSynapse> alphasynapses = new ArrayList<NGXSynapse>();
            
			  // Factor to rise weight of a synapse per micrometer.
	 float wfactor = 0.001f;
	    for (int j = 0; j < synapseList.size(); j++) {
			Cons synapse = synapseList.get(j);

			if (synapse.getNeuron1() != null) {
			    continue;
			}

			if (neuronType.equals(NeuronTypes.L4_STELLATE)) {
			    if (synapse.getNeuron2().getIndex() >= getNumL4stellate()) {
				continue;
			    }
			} else if (neuronType.equals(NeuronTypes.L23_PYRAMIDAL)) {
			    int nNum = getNumL4stellate() + getNumL23pyramidal();
			    if (synapse.getNeuron2().getIndex() < getNumL4stellate() || synapse.getNeuron2().getIndex() >= nNum) {
				continue;
			    }
			} else if (neuronType.equals(NeuronTypes.L5B_PYRAMIDAL)) {
			    int nNum = getNumL4stellate() + getNumL23pyramidal() + getNumL5Apyramidal();
			    int nNum2 = getNumL4stellate() + getNumPyramidal();
			    if (synapse.getNeuron2().getIndex() < nNum || synapse.getNeuron2().getIndex() > nNum2) {
				continue;
			    }
			} else {
			    continue;
			}

                    int n_idx = synapse.getNeuron2().getIndex();

                    int c_idx = 0;
                    for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
                        if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
                            break;
                        }
                        c_idx++;
                    }

                    int sec_id = synapse.getNeuron2DenSection().getId();
                    float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
                    //float dd = neuronList.get(n_idx).getDendrites().get(d_idx).getdendritesectionData(sec_id, c_idx);

		    NGXAlphaSynapse alpha = new NGXAlphaSynapse();
		    alpha.setFrom("N" + n_idx + "dendrite" + sec_id);
		    alpha.setFrom_loc(dd);
		    alpha.setGmax((1 + wfactor * synapse.getDendriticSomaDistance()) * 0.001f);
		    alphasynapses.add(alpha);
               	}
	    return alphasynapses;
	}
    }
    

       
 @Override
    public WriteToHoc getHocData() {
        return new WriteToHocData();
    }


    /**
     * 
     * TODO: make a factory class.. to generate data
     */
    @SuppressWarnings("PublicInnerClass")
    public class WriteToHocData implements WriteToHoc {

        /**
         * Function to get the uEPSP value of a synapse connecting Neuron1 and Neuron2 depending on their types (simone)
         */
        @Override
        public float get_uEPSP_Value(int typeN1, int typeN2) {
            float uEPSP = -1.0000f; //unitary EPSP variable for the various neuron type combinations

            //which type combination? -> which uEPSP?
            if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L4_STELLATE.ordinal())) {
                uEPSP = 0.0016f;
                //cout << "typeN1: L4, typeN2: L4, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0007f;
                //cout << "typeN1: L4, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L4_STELLATE.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0006f;
                //cout << "typeN1: L4, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0010f;
                //cout << "typeN1: L2/3, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0008f;
                //cout << "typeN1: L2/3, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L23_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0003f;
                //cout << "typeN1: L2/3, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5A_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0020f;
                //cout << "typeN1: L5A, typeN2: L5A, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5A_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L23_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0005f;
                //cout << "typeN1: L5A, typeN2: L2/3, uEPSP: " << uEPSP << endl;
            } else if ((typeN1 == NeuronTypes.L5B_PYRAMIDAL.ordinal())
                    && (typeN2 == NeuronTypes.L5B_PYRAMIDAL.ordinal())) {
                uEPSP = 0.0013f;
                //cout << "typeN1: L5B, typeN2: L5B, uEPSP: " << uEPSP << endl;
            } else {
                uEPSP = 0.0010f;
                //cout << "typeN1: " << typeN1 << ", typeN2: " << typeN2 << ", uEPSP: ??? " << uEPSP << endl;
            }
            return uEPSP;
        }

        @Override
        public final void writetohocChannels(Writer fw) throws IOException {
            int secProcCounter = 1; //counter for secundar procedures

            fw.append("  // na+ channels \n"
                    + "  forall insert na \n");
            int ii = 0;
            for (; ii < getNumL4stellate(); ++ii) {
                if (ii % 101 == 100) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "dendrite\" gbar_na = gna_dend \n");
            }

            for (; ii < getNumL4stellate() + getNumPyramidal(); ++ii) {
                if (ii % 101 == 100) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "dendrite\" gbar_na = gna_dend_pyr \n");
            }

            for (; ii < nneuron; ++ii) {
                if (ii % 101 == 100) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "dendrite\" gbar_na = gna_dend \n");
            }

            fw.append("} \n\n"
                    + "proc init_cell" + secProcCounter++ + "() { \n");
            ii = 0;
            for (; ii < getNumL4stellate(); ++ii) {
                if (ii % 26 == 25) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "axon\" { \n"
                        + "    insert kv \n"
                        + "    gbar_na = 0.06*gna_node \n"
                        + "    gbar_kv = gkv_axon \n"
                        + "  } \n");
            }

            for (; ii < getNumL4stellate() + getNumPyramidal(); ++ii) {
                if (ii % 26 == 25) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "axon\" { \n"
                        + "    insert kv \n"
                        + "    gbar_na = 0.06*gna_node \n"
                        + "    gbar_kv = gkv_axon_pyr \n"
                        + "  } \n");
            }
            for (; ii < nneuron; ++ii) {
                if (ii % 26 == 25) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + ii + "axon\" { \n"
                        + "    insert kv \n"
                        + "    gbar_na = 0.06*gna_node \n"
                        + "    gbar_kv = gkv_axon \n"
                        + "  } \n");
            }

            fw.append("} \n\n"
                    + "  // kv delayed rectifier channels \n");
            for (int i = 0; i < nneuron; i += 100) {
                fw.append("proc init_cell" + secProcCounter++ + "() { \n");
                for (int j = i; j < i + 100 && j < nneuron; j++) {
                    fw.append("  N" + j + "soma { insert kv  gbar_kv = gkv_soma } \n");
                }
                fw.append("} \n\n");
            }

            fw.append("proc init_cell" + secProcCounter++ + "() { \n"
                    + "  // dendritic channels \n");
            for (int i = 0; i < nneuron; ++i) {
                if (i % 21 == 20) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  forsec \"N" + i + "dendrite\" { \n"
                        + "    insert km    gbar_km  = gkm \n"
                        + "    insert kca   gbar_kca = gkca \n"
                        + "    insert ca    gbar_ca = gca \n"
                        + "    insert cad \n"
                        + "  } \n");
            }

            fw.append("} \n\n"
                    + "proc init_cell" + secProcCounter++ + "() { \n");
            for (int i = 0; i < nneuron; ++i) {
                if (i % 11 == 10) {
                    fw.append("} \n"
                            + "proc init_cell" + secProcCounter++ + "(){ \n");
                }
                fw.append("  N" + i + "soma { \n"
                        + "    insert km    gbar_km  = gkm \n"
                        + "    insert kca   gbar_kca = gkca \n"
                        + "    insert ca    gbar_ca = gca \n"
                        + "    insert cad \n"
                        + "    gbar_na = gna_soma \n"
                        + "    gbar_km = gkm_soma \n"
                        + "    gbar_kca = gkca_soma \n"
                        + "    gbar_ca = gca_soma \n"
                        + "  } \n");
            }
            fw.append("\n"
                    + "  forall if(ismembrane(\"k_ion\")) ek = Ek \n"
                    + "  forall if(ismembrane(\"na_ion\")) { \n"
                    + "    ena = Ena \n"
                    + "    // seems to be necessary for 3d cells to shift Na kinetics -5 mV \n"
                    + "    vshift_na = -5 \n"
                    + "  } \n"
                    + "  forall if(ismembrane(\"ca_ion\")) { \n"
                    + "    eca = 140 \n"
                    + "    ion_style(\"ca_ion\",0,1,0,0,0) \n"
                    + "    vshift_ca = 0 \n"
                    + "  } \n"
                    + "} \n\n");

            for (int i = 0; i < secProcCounter; i++) {
                fw.append("init_cell" + i + "() \n");
            }
            fw.append("\n");
            fw.flush();
        }

        @Override
        public final void writetohocAlphaSynapses(Writer fw) throws IOException {
            writetohocAlphaSynapses(fw, NeuronTypes.L4_STELLATE);
            writetohocAlphaSynapses(fw, NeuronTypes.L23_PYRAMIDAL);
            writetohocAlphaSynapses(fw, NeuronTypes.L5B_PYRAMIDAL);
        }

        /**
         *
         * Function to write alpha synapses to a hoc file.
         *
         * @param fw stream for the hoc file
         */
        public final void writetohocAlphaSynapses(Writer fw, NeuronTypes neuronType) throws FileNotFoundException, IOException {
            String projectPath = NeuGenView.getInstance().getProjectDirPath();
            String tcuDataPath = projectPath + NeuGenConstants.FILE_SEP + NeuGenConstants.TCU_DATA_FILE;

            FileReader tcuData = new FileReader(new File(tcuDataPath));
            BufferedReader tcuDataBuffer = new BufferedReader(tcuData);
            String nextLine = null;
            List<String[]> dataList = new ArrayList<String[]>();
            while ((nextLine = tcuDataBuffer.readLine()) != null) {
                nextLine = nextLine.trim();
                String[] data = nextLine.split("\t");
                dataList.add(data);
            }
            int ntime_sec = 100;
            int starttime = 140;
            int sumTime = ntime_sec + starttime;

            double[] tcutab = new double[ntime_sec];
            double[] time = new double[ntime_sec];

            int i = 0;
            for (int j = starttime; j < sumTime; ++j, i++) {
                String[] data = dataList.get(j);
                time[i] = Double.parseDouble(data[0]);
                tcutab[i] = Double.parseDouble(data[1]);
            }

            // Factor to rise weight of a synapse per micrometer.
            float wfactor = 0.001f;
            for (int j = 0; j < synapseList.size(); j++) {
                Cons synapse = synapseList.get(j);

                if (synapse.getNeuron1() != null) {
                    continue;
                }

                if (neuronType.equals(NeuronTypes.L4_STELLATE)) {
                    if (synapse.getNeuron2().getIndex() >= getNumL4stellate()) {
                        continue;
                    }
                } else if (neuronType.equals(NeuronTypes.L23_PYRAMIDAL)) {
                    int nNum = getNumL4stellate() + getNumL23pyramidal();
                    if (synapse.getNeuron2().getIndex() < getNumL4stellate() || synapse.getNeuron2().getIndex() >= nNum) {
                        continue;
                    }
                } else if (neuronType.equals(NeuronTypes.L5B_PYRAMIDAL)) {
                    int nNum = getNumL4stellate() + getNumL23pyramidal() + getNumL5Apyramidal();
                    int nNum2 = getNumL4stellate() + getNumPyramidal();
                    if (synapse.getNeuron2().getIndex() < nNum || synapse.getNeuron2().getIndex() > nNum2) {
                        continue;
                    }
                } else {
                    continue;
                }

                for (int jj = 0; jj < 100; ++jj) {
                    double prob = tcutab[jj];
                    if (getDrawNumber().fdraw() > prob) {
                        continue; // no action for this synapse
                    }

                    int n_idx = synapse.getNeuron2().getIndex();

                    int c_idx = 0;
                    for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
                        if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
                            break;
                        }
                        c_idx++;
                    }

                    int sec_id = synapse.getNeuron2DenSection().getId();
                    float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
                    //float dd = neuronList.get(n_idx).getDendrites().get(d_idx).getdendritesectionData(sec_id, c_idx);

                    fw.append("objectvar Synapse" + j + '_' + jj + "\n"
                            + "N" + n_idx + "dendrite" + sec_id + " Synapse" + j + '_' + jj
                            + " = new AlphaSynapse(" + dd + ")" + "\n"
                            + "Synapse" + j + '_' + jj + ".onset = " + jj + "\n"
                            + "Synapse" + j + '_' + jj + ".tau = 1.7" + "\n"
                            + "Synapse" + j + '_' + jj + ".gmax = " + (1 + wfactor * synapse.getDendriticSomaDistance()) * 0.001 + "\n" // maximum value
                            + "Synapse" + j + '_' + jj + ".e = 0" + "\n"
                            + "\n");
                    fw.flush();
                }
            }
        }

        @Override
        public final void writetohocExp2Synapses(Writer fw, Writer synFW) throws IOException {
            // Factor to rise weight of a synapse per micrometer.
            float wfactor = 0.001f;
            List<Cons> synapseList = getSynapseList();
            for (int j = 0; j < synapseList.size(); j++) {
                Cons synapse = synapseList.get(j);

                int typeN1 = -1;
                int typeN2 = -1;

                if (synapse.getNeuron1() == null) {
                    continue;
                }

                typeN1 = getTypeOfNeuron(synapse.getNeuron1().getIndex());
                typeN2 = getTypeOfNeuron(synapse.getNeuron2().getIndex());

                int n_idx = synapse.getNeuron2().getIndex();

                int c_idx = 0;
                for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
                    if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
                        break;
                    }
                    c_idx++;
                }

                int sec_id = synapse.getNeuron2DenSection().getId();
                float dd = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron2DenSection(), c_idx);
                //float dd = synapse.neuron2.getDendrites().get(d_idx).getdendritesectionData(sec_id, c_idx);
                //logger.info("dendrite secion data: " + dd);
                //int n1_idx = synapse.neuron1_idx;

                if (synFW != null) {
                    //write functional synapse (NetCon) coordinates in file
                    Point3f sc = synapse.getNeuron1AxSegment().getEnd();
                    synFW.append(sc.x + " " + sc.y + " " + sc.z + "\n");
                    synFW.flush();
                }

                fw.append("\nobjectvar Synapse" + j + "\n"
                        + "N" + n_idx + "dendrite" + sec_id + " Synapse" + j + " = new Exp2Syn(" + dd + ")" + "\n"
                        + "Synapse" + j + ".tau1 = 0.2" + "\n" /* ms */
                        + "Synapse" + j + ".tau2 = 1.7" + "\n" /* ms */
                        + "Synapse" + j + ".e = 0.0" + "\n" /* reversal potential, mV */
                        + "objectvar Nc" + j + "\n");

                Section ax_section = synapse.getNeuron1AxSection();
                assert (ax_section.getLength() > 0.0);
                int axSegPos = 0;
                for (Segment axSeg : ax_section.getSegments()) {
                    if (axSeg.getId() == synapse.getNeuron1AxSegment().getId()) {
                        break;
                    }
                    axSegPos++;
                }
		
		float ax_local_position = (ax_section.getLength() * axSegPos) / ax_section.getLength();
                assert (!Float.isInfinite(ax_local_position));
		
		float ff = NeuronalTreeStructure.getDendriteSectionData(synapse.getNeuron1AxSection(), axSegPos);
		assert (ff <= 1.0);
		/**
		 * @author stephanmg <stephan@syntaktischer-zucker.de>
		 * @note to use ax_local_position is a bug, since the ax_local_position
		 *       is not in the range [0,1] - by definition of ax_local_position (cf. above!)
		 * 
		 * 	 instead of using ax_local_position, we are required to know the relative position
		 *       within the given section, where the synaptic connection should end (i. e. end vertex/coordinates),
		 *       which is by definition the corresponding dendrite to the axon at hands 
		 *       (see above Exp2Syn output, where the corresponding axon is used!)
		 * 
		 * 	 ff specified the relative location within bounds [0,1] on the given dendrite.
		 * 
		 */

		/// was ax_local_position
                fw.append("N" + synapse.getNeuron1().getIndex() + ax_section.getName() + " Nc" + j
                        + " = new NetCon(&v(" + ff + "), Synapse" + j + ", -10.0, 0.5, "
                        + HOCUtil.format((1 + wfactor * synapse.getDendriticSomaDistance()) * get_uEPSP_Value(typeN1, typeN2)) + ")" + "\n");
            }
            if (synFW != null) {
                synFW.close();
            }
            fw.flush();
        }

        @Override
        public final void writetohocModel(Writer fw) throws IOException {
            fw.append("objref fih \n");
            fw.append(" fih = new FInitializeHandler(2, \"mkmovie()\")\n");
            fw.append("access N0soma\n");
            fw.append("/*objref st\n");
            fw.append("st=new IClamp(0.5)\n");
            fw.append("st.dur = 10\n");
            fw.append("st.del = 10\n");
            fw.append("st.amp = 0.2*/\n\n");
            fw.append("// --------------------------------------------------------------\n");
            fw.append("// passive & active membrane\n");
            fw.append("// --------------------------------------------------------------\n");
            fw.append("ra        = 150\n");
            fw.append("global_ra = ra\n");
            fw.append("rm        = 30000\n");
            fw.append("c_m       = 0.75\n");
            fw.append("//cm_myelin = 0.04  g_pas_node = 0.02\n");
            fw.append("Ek = -90 \n");
            fw.append("Ena = 60\n");
            fw.append("gna_dend = 13\n");
            fw.append("gna_dend_pyr = 19\n");
            fw.append("gna_node = 30000\n");
            fw.append("gna_soma = 0.05*gna_node\n\n");
            fw.append("gkv_axon = 2000\n");
            fw.append("gkv_axon_pyr = 3000\n");
            fw.append("gkv_soma = 2000\n");
            fw.append("gca = .3\n");
            fw.append("gkm = .1\n");
            fw.append("gkca = 3\n");
            fw.append("gca_soma = gca\n");
            fw.append("gkm_soma = gkm\n");
            fw.append("gkca_soma = gkca\n\n");
            fw.append("proc init_cell0() {\n");
            fw.append("  // passive\n");
            fw.append("  forall {\n");
            fw.append("    insert pas\n");
            fw.append("    Ra = ra\n");
            fw.append("    cm = c_m\n");
            fw.append("    g_pas = 1/rm\n");
            fw.append("    e_pas = v_init\n");
            fw.append("  }\n\n");
            fw.flush();
        }
    }
}
