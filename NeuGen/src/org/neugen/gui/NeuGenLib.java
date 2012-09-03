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
import org.neugen.datastructures.xml.XMLObject;
import org.neugen.datastructures.parameter.NetParam;
import org.neugen.datastructures.parameter.NeuronParam;

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
            } else if (e.getKey().contains(NeuGenConstants.INTERNA_FNAME)) {
                ConfigParserContainer.setInternaParser(new ConfigParser(e.getKey(), e.getValue()));
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
            ngView.outPrintln("End generate net.");
            timeEnd = System.currentTimeMillis();
            diffTime = (float) (timeEnd - timeStart) / (1000f);
            ngView.outPrintln("Time to generate net: " + diffTime + " seconds.\n");

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
            ngView.outPrintln("End generate net.");
            timeEnd = System.currentTimeMillis();
            diffTime = (float) (timeEnd - timeStart) / (1000f);
            ngView.outPrintln("Time to generate net: " + diffTime + " seconds.\n");

            runInterconnect(NetHippocampus.cellTypesNumber);
        }
        ngView.setNet(net);
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
    }
}
