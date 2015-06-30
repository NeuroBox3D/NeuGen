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
 * File: NeuGenLib.java
 * Created on 05.08.2009, 10:17:08
 *
 */
package org.neugen.gui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neugen.backend.NGBackend;
import org.neugen.parsers.ConfigParser;
import org.neugen.parsers.ConfigParserContainer;
import org.neugen.datastructures.neuron.NeuronCA1Pyramidal;
import org.neugen.datastructures.neuron.NeuronCalbindin;
import org.neugen.datastructures.neuron.NeuronCalretinin;
import org.neugen.datastructures.neuron.NeuronCholecystokinin;
import org.neugen.datastructures.NetHippocampus;
import org.neugen.datastructures.neuron.NeuronL23Pyramidal;
import org.neugen.datastructures.neuron.NeuronL4stellate;
import org.neugen.datastructures.neuron.NeuronL5APyramidal;
import org.neugen.datastructures.neuron.NeuronL5BPyramidal;
import org.neugen.datastructures.NetNeocortex;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.neuron.NeuronParvalbumin;
import org.neugen.datastructures.neuron.NeuronPyramidal;
import org.neugen.datastructures.neuron.NeuronSomatostatin;
import org.neugen.datastructures.neuron.NeuronStarpyramidal;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.DendriteParam.ApicalParam;
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.NeuronParam.SomaParam;
import org.neugen.datastructures.parameter.SubCommonTreeParam;

/**
 * @author Alexander Wanner
 * @author Simone Eberhard
 * @author Sergei Wolf
 */
public final class NeuGenLib implements Serializable {

    static final long serialVersionUID = -8689337816399140143L;
    /** use to log messages */
    private static final Logger logger = Logger.getLogger(NeuGenLib.class.getName());
    private Net net;
    private final NeuGenView ngView;
    private final NeuGenLibTask task;

    public NeuGenLib() {
        ngView = NeuGenView.getInstance();
        task = NeuGenLibTask.getInstance();
    }

    public Net getNet() {
        return net;
    }

    public void destroy() {
        NeuGenLibTask.setInstance(null);
    }
    
    public static void clearOldParamData() {
        NetParam.setInstance(null);
        Region.setInstance(null);
        Region.Param.setInstance(null);
        NeuronParam.setInstance(null);
        NeuronL4stellate.Param.setInstance(null);
        NeuronPyramidal.PyramidalParam.setInstance(null);
        NeuronStarpyramidal.Param.setInstance(null);
        NeuronL23Pyramidal.Param.setInstance(null);
        NeuronL5APyramidal.Param.setInstance(null);
        NeuronL5BPyramidal.Param.setInstance(null);

        NeuronCA1Pyramidal.Param.setInstance(null);
        NeuronCalbindin.Param.setInstance(null);
        NeuronCalretinin.Param.setInstance(null);
        NeuronCholecystokinin.Param.setInstance(null);
        NeuronParvalbumin.Param.setInstance(null);
        NeuronSomatostatin.Param.setInstance(null);

        if (ConfigParserContainer.getInternaParser() != null) {
            ConfigParserContainer.getInternaParser().getResult().clear();
            ConfigParserContainer.setInternaParser(null);
        }

        if (ConfigParserContainer.getParamParser() != null) {
            ConfigParserContainer.getParamParser().getResult().clear();
            ConfigParserContainer.setParamParser(null);
        }
        //Axon.setDrawNumber(null);
        //Dendrite.setDrawNumber(null);
        //NeuronBase.setDrawNumber(null);
        //NetBase.setDrawNumber(null);
        System.gc();
    }

    public static Map<String, XMLObject> initParamTable() {
        String paramPath = NeuGenView.getParamPath();
        String internaPath = NeuGenView.getInternaPath();
        String outputPath = NeuGenView.getOutputOptionsPath();

        XMLObject param = NeuGenView.getInstance().getParamTrees().get(NeuGenConstants.PARAM);
        XMLObject interna = NeuGenView.getInstance().getParamTrees().get(NeuGenConstants.INTERNA);
        XMLObject outputOptions = NeuGenView.getInstance().getParamTrees().get(NeuGenConstants.OUTPUT);

        Map<String, XMLObject> allParam = new HashMap<String, XMLObject>();
        allParam.put(paramPath, param);
        allParam.put(internaPath, interna);
        allParam.put(outputPath, outputOptions);
        return allParam;
    }

    public static void initParamData(Map<String, XMLObject> allParam, String project) {
        clearOldParamData();
        for (Map.Entry<String, XMLObject> e : allParam.entrySet()) {
            if (e.getKey().contains(NeuGenConstants.PARAM_FNAME)) {
                ConfigParserContainer.setParamParser(new ConfigParser(e.getKey(), e.getValue()));
                System.err.println("*** KEY *** " + e.getKey() + " *** VALUE ***" + e.getValue());
            } else if (e.getKey().contains(NeuGenConstants.INTERNA_FNAME)) {
                ConfigParserContainer.setInternaParser(new ConfigParser(e.getKey(), e.getValue()));
                System.err.println("*** KEY *** " + e.getKey() + " *** VALUE ***" + e.getValue());
            }
        }
        //NeuronParam.getInstance();
        //Axon.setDrawNumber(new Vrand(nParam.getAxonParam().getSeedValue()));
        //Dendrite.setDrawNumber(new Vrand(nParam.getDendriteParam().getSeedValue()));
        //NeuronBase.setDrawNumber(new Vrand(nParam.getSeedValue()));
    }

    public void run(String projectType) {
        if (task != null) {
            task.setMyProgress(0.05f);
        }
        long timeStart = 0;
        long timeEnd = 0;
        float diffTime = 0.0f;

        if (projectType.equals(NeuGenConstants.NEOCORTEX_PROJECT)) {
            net = new NetNeocortex();
            //generate neural net
            timeStart = System.currentTimeMillis();
            net.generate();
 //           ngView.outPrintln("End generate net.");
            timeEnd = System.currentTimeMillis();
            diffTime = (float) (timeEnd - timeStart) / (1000f);
         //   ngView.outPrintln("Time to generate net: " + diffTime + " seconds.\n");

            runInterconnect(NetNeocortex.cellTypesNumber);
            /*
            //interconnect cells of neuronal net
            timeStart = System.currentTimeMillis();
            net.interconnect();
            timeEnd = System.currentTimeMillis();
            diffTime = (float) (timeEnd - timeStart) / (1000f);
            ngView.outPrintln("Time to interconnect neurons: " + diffTime + " seconds.\n");
            System.gc();
            //create synapses
            net.createNonFunSynapses();
            System.gc();

            String synMes = "\n";
            synMes += " number of synapses: " + net.getNumSynapse() + "\n";
            long nbilSyn = net.getNumSynapse() - net.getNumNonFunSynapses();

            synMes += " number of bilateral synapses: " + nbilSyn + "\n";
            synMes += " nonfunctional synapses: " + net.getNumNonFunSynapses() + "\n";
            synMes += " connectivity of cell types: " + "\n";

            for (int i = 0; i < NetNeocortex.cellTypesNumber; ++i) {
                for (int j = 0; j < NetNeocortex.cellTypesNumber; j++) {
                    //logger.info(net.typeNames.get(i));
                    float val = ((float) net.getNumOfSynapses(i, j)) / nbilSyn;
                    if (val > 0) {
                        synMes += "\t" + net.getTypeCellNames().get(i) + "->" + net.getTypeCellNames().get(j) + " " + val + "\n";
                        //logger.info("i: " + i + ", type: " + net.typeNames.get(i));
                        //logger.info("j: " + j + ", type: " + net.typeNames.get(j));
                    }
                }
            }
            ngView.outPrintln(synMes);
            Map<String, Float> presynapticConv = net.computeAPSN();
            logger.info("presynapticConv: " + presynapticConv.size());
            String preSynMes = "\n";
            preSynMes += "numbers of presynaptics converging on average postsynaptic cell: \n";
            for (Map.Entry<String, Float> elem : presynapticConv.entrySet()) {
                if (elem.getValue() > 0) {
                    preSynMes += "\t" + elem.getKey() + " " + elem.getValue() + "\n";
                }
            }
            presynapticConv.clear();
            ngView.outPrintln(preSynMes);
             * 
             */

            /*
            CSVWriter csvW = new CSVWriter(net, "outputData.csv");
            csvW.writeAverageNeuronInfo();
            ngView.outPrintln("write average neuron info to outputData.csv");
            ngView.outPrintln("end");
             */
        } else if (projectType.equals(NeuGenConstants.HIPPOCAMPUS_PROJECT)) {
            net = new NetHippocampus();
            //generate neural net
            timeStart = System.currentTimeMillis();
            net.generate();
            //ngView.outPrintln("End generate net.");
            timeEnd = System.currentTimeMillis();
            diffTime = (float) (timeEnd - timeStart) / (1000f);
            //ngView.outPrintln("Time to generate net: " + diffTime + " seconds.\n");

            runInterconnect(NetHippocampus.cellTypesNumber);
        }
	
	if (NeuGenConstants.WITH_GUI) {
 	       ngView.setNet(net);
	} else {
		NGBackend.logger.info("Without GUI!");
	}
	
        if (task != null) {
            task.setMyProgress(1.0f);
        }
        //System.gc();
    }

    private void runInterconnect(int cellTypesNumber) {
        long timeStart = 0;
        long timeEnd = 0;
        float diffTime = 0.0f;
        //interconnect cells of neuronal net
        timeStart = System.currentTimeMillis();
        net.interconnect();
        timeEnd = System.currentTimeMillis();
        diffTime = (float) (timeEnd - timeStart) / (1000f);
 //       ngView.outPrintln("Time to interconnect neurons: " + diffTime + " seconds.\n");
        System.gc();
        //create synapses
        net.createNonFunSynapses();
        System.gc();

        String synMes = "\n";
        synMes += " number of synapses: " + net.getNumSynapse() + "\n";
        long nbilSyn = net.getNumSynapse() - net.getNumNonFunSynapses();

        synMes += " number of bilateral synapses: " + nbilSyn + "\n";
        synMes += " nonfunctional synapses: " + net.getNumNonFunSynapses() + "\n";
        synMes += " connectivity of cell types: " + "\n";

        for (int i = 0; i < cellTypesNumber; ++i) {
            for (int j = 0; j < cellTypesNumber; j++) {
                //logger.info(net.typeNames.get(i));
                float val = ((float) net.getNumOfSynapses(i, j)) / nbilSyn;
                if (val > 0) {
                    synMes += "\t" + net.getTypeCellNames().get(i) + "->" + net.getTypeCellNames().get(j) + " " + val + "\n";
                    //logger.info("i: " + i + ", type: " + net.typeNames.get(i));
                    //logger.info("j: " + j + ", type: " + net.typeNames.get(j));
                }
            }
        }
 //       ngView.outPrintln(synMes);
        Map<String, Float> presynapticConv = net.computeAPSN();
        logger.info("presynapticConv: " + presynapticConv.size());
        String preSynMes = "\n";
        preSynMes += "numbers of presynaptics converging on average postsynaptic cell: \n";
        for (Map.Entry<String, Float> elem : presynapticConv.entrySet()) {
            if (elem.getValue() > 0) {
                preSynMes += "\t" + elem.getKey() + " " + elem.getValue() + "\n";
            }
        }
        presynapticConv.clear();
	System.out.println(preSynMes);
        //ngView.outPrintln(preSynMes);
    }
}
