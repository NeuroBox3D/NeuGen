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
package org.neugen.parsers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Cons;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Pair;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;

/**
 * Class to export data of neuroal net to a simplified hoc format.
 * 
 * @author Sergei Wolf
 */
public final class SimpleHocWriter {

    /** Use to log messages. */
    private final static Logger logger = Logger.getLogger(SimpleHocWriter.class.getName());
    public static final int d = 3;
    public static final char AXON_TYPE = 'a';
    public static final char SOMA_TYPE = 's';
    public static final char DENDRITIC_TYPE = 'd';
    public DecimalFormat df;
    public int globalID;
    public int somaID;
    //char parentType;
    //int parentID;
    ///map for the parentIDs
    public Map<Section, Pair<Integer, Character>> sectionIds;

    /// Function to increment global ID
    public int incrementGlobalID() {
        return globalID++;
    }

    public int getGlobalID() {
        return globalID;
    }

    /// Function to save the integer id
    public void insertIntoSecID(Section sec, int secID, char type) {
        //(*section_ids)[sec] = pair<unsigned int, char>(secID, type);
        sectionIds.put(sec, new Pair<Integer, Character>(secID, type));
    }

    public SimpleHocWriter() {
        String pattern = "###.###";
        Locale loc = new Locale("en", "US");
        NumberFormat nf = NumberFormat.getNumberInstance(loc);
        df = (DecimalFormat) nf;
        df.applyPattern(pattern);
        sectionIds = new HashMap<Section, Pair<Integer, Character>>();
    }

    /**
     * Function to get the parent section id in integer format and the type of the section
     */
    private Pair<Integer, Character> getParentSectionID(Section sec) {
        if (sec.getParentalLink() != null && sec.getParentalLink().getParental() != null) {
            Section parentSec = sec.getParentalLink().getParental();
            return sectionIds.get(parentSec);
            /*
            int parID = parentSec.getId();
            Character type;
            Section.SectionType parSecType = parentSec.getSectionType();
            if (parSecType == Section.SectionType.SOMA) {
            type = SOMA_TYPE;
            } else if (parSecType == Section.SectionType.MYELINIZED || parSecType == Section.SectionType.NOT_MYELINIZED) {
            type = AXON_TYPE;
            } else if (parSecType == Section.SectionType.APICAL || parSecType == Section.SectionType.BASAL || parSecType == Section.SectionType.OBLIQUE) {
            type = DENDRITIC_TYPE;
            } else {
            type = null;
            }
            Pair<Integer, Character> pData = new Pair<Integer, Character>(parID, type);
            return pData;
             *
             */
        } //soma is parent
        else {
            Pair<Integer, Character> somaData = new Pair<Integer, Character>();
            somaData.first = somaID;
            somaData.second = SOMA_TYPE;
            return somaData;
        }

    }

    /**
     * Function to write data of segment into shoc file
     *
     * @param fw the stream for the shoc file
     * @param start_end for 0 write data of start of segment, for 1 write data of end of segment.
     * @throws IOException
     */
    public final void writetoshocSegment(Writer fw, int start_end, Segment segment) throws IOException {
        if (start_end == 0) {
            fw.write("\t");
            Point3f sstart = segment.getStart();
            fw.write(df.format(sstart.x) + " " + df.format(sstart.y) + " " + df.format(sstart.z) + " ");
            float rad = segment.getStartRadius() * 2;
            testRad(rad);
            fw.write(df.format(rad) + "\n");
        }
        if (start_end == 1) {
            fw.write("\t");
            Point3f send = segment.getEnd();
            fw.write(df.format(send.x) + " " + df.format(send.y) + " " + df.format(send.z) + " ");
            float rad = segment.getEndRadius() * 2;
            testRad(rad);
            fw.write(df.format(rad) + "\n");
        }
    }

    /**
     * Function to write the data of dendrite into a shoc file.
     * @param fw the stream for the shoc file.
     * @param neuron the neuron of neural network
     * @throws IOException
     */
    public final void writetoshocDendrite(Writer fw, Neuron neuron) throws IOException {
        Integer parentID = null;
        Character parentType = null;
        for (Dendrite dendrite : neuron.getDendrites()) {
            Section firstSection = dendrite.getFirstSection();
            Section.Iterator secIterator = firstSection.getIterator();
            while (secIterator.hasNext()) {
                Section section = secIterator.next();
                int nsegs = section.getSegments().size();
                if (nsegs > 0) {
                    Pair<Integer, Character> parentData = getParentSectionID(section);
                    if (parentData == null) {
                        logger.info("parentData ist null");
                        //int somaId = neuron.getSoma().getEllipsoid().getId();
                        int somaId = somaID;
                        parentData = new Pair<Integer, Character>(somaId, SOMA_TYPE);
                    }
                    parentID = parentData.first;
                    parentType = parentData.second;

                    if (parentType.equals(SOMA_TYPE)) {
                        Point3f somaMid = neuron.getSoma().getMid();
                        logger.info("(dend data)parent is soma, somaMid: " + somaMid);
                        Point3f segStart = section.getSegments().get(0).getStart();
                        logger.info("dend seg start: " + segStart.toString());

                        
                        boolean compareP = somaMid.equals(segStart);
                        if (compareP) {
                            logger.info("die punkte sind gleich");
                            segStart.z = somaMid.z + 5;
                        } else {
                            logger.info("die punkte sind verschieden");
                            segStart.x = somaMid.x;
                            segStart.y = somaMid.y;
                            segStart.z = somaMid.z + 5;
                            //continue;
                        }
                    
                    }

                    //int id = section.getId();
                    int id = globalID;
                    fw.write("begin dendrite " + id + "\n");
                    fw.write("\tparent " + parentType.toString() + " " + parentID.toString() + "\n");
                    fw.write("\t" + nsegs + "\n");

                    for (Segment segment : section.getSegments()) {
                        writetoshocSegment(fw, 0, segment);
                    }

                    Segment segment = section.getSegments().get(nsegs - 1);
                    writetoshocSegment(fw, 1, segment);
                    fw.write("end\n");
                    fw.flush();
                    insertIntoSecID(section, globalID, DENDRITIC_TYPE);
                    incrementGlobalID();
                }
            }
        }
    }

    public void testRad(float rad) {
        if (rad < 0.1f) {
            logger.info("radius ist kleiner als 0.1");
        }
    }

    /**
     * Function to write the data of axon into a shoc file.
     * @param fw the stream for the shoc file.
     * @throws IOException
     */
    public final void writetoshocAxon(Writer fw, Neuron neuron) throws IOException {
        Axon axon = neuron.getAxon();
        if (axon.getFirstSection() == null) {
            return;
        }
        Section firstSection = axon.getFirstSection();
        Section.Iterator secIterator = firstSection.getIterator();
        // loop over all sections of axon neuron
        while (secIterator.hasNext()) {
            Section section = secIterator.next();
            int nsegs = section.getSegments().size();
            if (nsegs > 0) {
                Pair<Integer, Character> parentData = this.getParentSectionID(section);
                if (parentData == null) {
                    logger.info("parentData is null-> soma is parent!: " + section.getName());
                    int somaId = somaID;
                    /*
                    if (neuron.getSoma().getCylindricRepresentant() == null) {
                    somaId = neuron.getSoma().getEllipsoid().getId();
                    } else {
                    somaId = neuron.getSoma().getCylindricRepresentant().getId();
                    }
                     *
                     */
                    parentData = new Pair<Integer, Character>(somaId, SOMA_TYPE);
                }

                Integer parentID = parentData.first;

                /*
                if (parentID == null) {
                logger.info("parent id is null");
                }
                 */
                Character parentType = parentData.second;
                if (parentType.equals(SOMA_TYPE)) {
                    Point3f somaMid = neuron.getSoma().getMid();
                    logger.info("(axon data)parent is soma, somaMid: " + somaMid);
                    Point3f segStart = section.getSegments().get(0).getStart();
                    logger.info("axon seg start: " + segStart.toString());

                    
                    boolean compareP = somaMid.equals(segStart);
                    if (compareP) {
                        logger.info("die punkte sind gleich");
                        segStart.z = somaMid.z + 5;
                    } else {

                        logger.info("die punkte sind verschieden");
                        segStart.x = somaMid.x;
                        segStart.y = somaMid.y;
                        segStart.z = somaMid.z + 5;
                        //continue;
                    }
              
                }

                //original sec id
                //int id = section.getId();
                int id = globalID;

                fw.write("begin axon " + id + "\n");
                fw.write("\tparent " + parentType.toString() + " " + parentID.toString() + "\n");
                fw.write("\t" + nsegs + "\n");

                for (Segment segment : section.getSegments()) {
                    writetoshocSegment(fw, 0, segment);
                }
                Segment segment = section.getSegments().get(nsegs - 1);
                writetoshocSegment(fw, 1, segment);

                insertIntoSecID(section, globalID, AXON_TYPE);
                incrementGlobalID();
                fw.write("end\n");
                fw.flush();
            }
        }
    }

    /**
     * Function to write the data of cellipsoid into a simplified hoc file.
     * @param fw the stream for the hoc file.
     * @param neuron the neuron of net
     * @throws IOException
     */
    public final void writetoshocSoma(Writer fw, Neuron neuron) throws IOException {
        Cellipsoid soma = neuron.getSoma();
        Section section = soma.getEllipsoid();
        
        /*
        Section section = soma.getCylindricRepresentant();
        if (section == null) {
            section = soma.getEllipsoid();
        }
         *
         */
        int nsegs = section.getSegments().size();

        //original id
        //int id = section.getId();
        //global id
        int id = globalID;

        fw.write("begin soma " + id + "\n");
        fw.write("\t" + (nsegs - 1) + "\n");
        for (Segment segment : section.getSegments()) {
            writetoshocSegment(fw, 0, segment);
        }
        /*
        Segment segment = section.getSegments().get(nsegs - 1);
        float[] send = segment.getEnd();
        fw.write("\t");
        for (int i = 0; i < d; ++i) {
        fw.write(send[i] + " ");
        }
        fw.write(segment.getEndRadius() * 2 + "\n");
         */
        somaID = globalID;
        incrementGlobalID();
        fw.write("end\n");
        fw.flush();
    }

    public final void writetoshocExp2Synapses(Net net, Writer fw) throws IOException {
        int synId = Section.getSecCounter();
        List<Cons> synapseList = net.getSynapseList();
        for (int j = 0; j < synapseList.size(); j++) {
            Cons synapse = synapseList.get(j);
            if (synapse.getNeuron1() == null) {
                continue;
            }

            int c_idx = 0;
            for (Segment denSeg : synapse.getNeuron2DenSection().getSegments()) {
                if (denSeg.getId() == synapse.getNeuron2DenSectionSegment().getId()) {
                    break;
                }
                c_idx++;
            }

            Section denSec = synapse.getNeuron2DenSection();
            Segment denSeg = denSec.getSegments().get(c_idx);
            Point3f denEnd = denSeg.getEnd();
            float denEndRad = denSeg.getEndRadius();
            Segment axonSeg = synapse.getNeuron1AxSegment();
            Point3f axonEnd = axonSeg.getEnd();
            float axonEndRad = axonSeg.getEndRadius();
            int nsegs = 1;
            fw.append("begin synapse " + synId + "\n"
                    + "\tparent a " + synapse.getNeuron1AxSection().getId() + "\n"
                    + "\tchild d " + synapse.getNeuron2DenSection().getId() + "\n"
                    + "\t" + nsegs + "\n"
                    + "\t" + axonEnd.x + " " + axonEnd.y + " " + axonEnd.z + " " + (axonEndRad * 2) + "\n"
                    + "\t" + denEnd.x + " " + denEnd.y + " " + denEnd.z + " " + (denEndRad * 2) + "\n"
                    + "end\n");
            synId++;
        }
        fw.flush();
    }

    public final void writetoshocUndefSection(Writer fw, Neuron neuron) throws IOException {
        Integer parentID = null;
        Character parentType = null;
        for (Section section : neuron.getUndefinedSections()) {
            int nsegs = section.getSegments().size();
            if (nsegs > 0) {
                Pair<Integer, Character> parentData = getParentSectionID(section);
                if (parentData == null) {
                    logger.info("parentData ist null");
                    //int somaId = neuron.getSoma().getEllipsoid().getId();
                    int somaId = somaID;
                    parentData = new Pair<Integer, Character>(somaId, SOMA_TYPE);
                }
                parentID = parentData.first;
                parentType = parentData.second;

                if (parentType.equals(SOMA_TYPE)) {
                    Point3f somaMid = neuron.getSoma().getMid();
                    logger.info("(dend data)parent is soma, somaMid: " + somaMid);
                    Point3f segStart = section.getSegments().get(0).getStart();
                    logger.info("dend seg start: " + segStart.toString());

                    
                    boolean compareP = somaMid.equals(segStart);
                    if (compareP) {
                        logger.info("die punkte sind gleich");
                        segStart.z = somaMid.z + 5;
                    } else {
                        logger.info("die punkte sind verschieden");
                        segStart.x = somaMid.x;
                        segStart.y = somaMid.y;
                        segStart.z = somaMid.z + 5;
                        //continue;
                    }
            
                }

                //int id = section.getId();
                int id = globalID;
                fw.write("begin dendrite " + id + "\n");
                fw.write("\tparent " + parentType.toString() + " " + parentID.toString() + "\n");
                fw.write("\t" + nsegs + "\n");

                for (Segment segment : section.getSegments()) {
                    writetoshocSegment(fw, 0, segment);
                }

                Segment segment = section.getSegments().get(nsegs - 1);
                writetoshocSegment(fw, 1, segment);
                fw.write("end\n");
                fw.flush();
                insertIntoSecID(section, globalID, DENDRITIC_TYPE);
                incrementGlobalID();
            }
        }
    }

    public final void writetoshocNetGeometry(Writer fw, Net net) throws IOException {
        for (Neuron neuron : net.getNeuronList()) {
            writetoshocSoma(fw, neuron);
            writetoshocAxon(fw, neuron);
            writetoshocDendrite(fw, neuron);
            writetoshocUndefSection(fw, neuron);
            fw.flush();
        }
        writetoshocExp2Synapses(net, fw);
    }

    public void writeNet(Net net, File file) {
        logger.info("Simplifedhoc File: " + file.getAbsolutePath());
        Writer fw = null;
        try {
            fw = new FileWriter(file);
            writetoshocNetGeometry(fw, net);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            logger.error("Could not create file: " + e, e);
        }
    }

    public static void main(String args[]) {
        MorphMLReader netBuilder = new MorphMLReader();
        String fname = "bla.xml";
        netBuilder.runMorphMLReader(fname);
        Net net = netBuilder.getNet();
        System.out.println("Size of the neural net: " + net.getNeuronList().size());
        File file = new File("CA1Migliore.shoc");
        SimpleHocWriter exportSHoc = new SimpleHocWriter();
        exportSHoc.writeNet(net, file);
    }
}
