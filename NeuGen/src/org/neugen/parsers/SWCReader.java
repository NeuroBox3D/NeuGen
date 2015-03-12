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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.AxonSection;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.DendriteSection;
import org.neugen.datastructures.NetBase;
import org.neugen.datastructures.neuron.NeuronBase;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.Pair;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.SectionLink;
import org.neugen.datastructures.Segment;
import org.neugen.utils.NeuGenLogger;
import org.neugen.gui.NeuGenView;

/**
 * SWC format
 *
 * n T x y z R P
 *
 * n is an integer label (normally increasing by one from one line to the next) that identifies the point
 * T is an integer that represents the type of neuronal segment.
 *      Value   Meaning
 *      0 	Undefined
 *      1 	Soma
 *      2 	Axon
 *      3 	Dendrite
 *      4 	Apical dendrite
 *      5 	Fork point
 *      6 	End point
 *      7 	Custom
 *
 * x, y, z gives the cartesian coordinates of each node.
 *
 * R is the radius of the section.
 *
 * P is an integer that represents the point preceding the current one when moving away from the soma.
 *
 * P indicates the parent (the integer label) of the current point or -1 to indicate an origin (soma)
 * and at forks in the neuron.
 * 
 * The swc file may contain a header with comments.
 * Each line of the header must start with a # character.
 *
 * @author Sergei Wolf
 *
 *
 */
public class SWCReader {

    private static final Logger logger = Logger.getLogger(SWCReader.class.getName());
    private Net net;    // neuronal net
    private Neuron neuron;  // neuron
    private Cellipsoid soma;    // soma of neuron
    private Axon axon;  // axon of neuron
    private AxonSection currentAxonSec;
    private Dendrite dendrite;
    private DendriteSection currentDendriteSec;
    private List<Dendrite> denList;
    private Map<Integer, Section> undefinedSections = new HashMap<Integer, Section>();
    // id of segment, Segment and type of section
    private Map<Integer, Pair<Segment, SWCSectionType>> segments = new HashMap<Integer, Pair<Segment, SWCSectionType>>();
    private NeuGenView ngView = NeuGenView.getInstance();
    private String swcComment = "";

    public enum SWCSectionType {

        UNDEFINED(0), SOMA(1), AXON(2), DENDRITE(3), APICAL(4);
        private int secNum;

        private SWCSectionType(int secNum) {
            this.secNum = secNum;
        }

        public int getSecNum() {
            return secNum;
        }
    }

    public SWCReader() {
        /*
        net = ngView.getNet();
        if(net == null) {
            net = new NetBase();
        }
        */
        net = new NetBase();
        neuron = new NeuronBase();
        soma = neuron.getSoma();
        axon = neuron.getAxon();
        denList = neuron.getDendrites();
    }

    public Net getNet() {
        return net;
    }

    public void readSWC(File f) {
        Reader in = null;
        try {
            in = new FileReader(f);
            BufferedReader br = new BufferedReader(in);
            String strLine;
            //soma data
            List<Section> somaSections = soma.getSections();
            Section somaSection = new Section();
            somaSection.setSectionType(Section.SectionType.SOMA);
            somaSections.add(somaSection);
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim();
                if (strLine.startsWith("#")) {
                    swcComment += strLine.trim().substring(1) + "\n";
                } else if (strLine.length() == 0) {
                    continue;
                } else {
                    String[] temp = null;
                    temp = strLine.split("\\s+");
                    if (temp.length == 7) {
                        int segId = Integer.parseInt(temp[0]); // id of segment
                        int swcSecType = Integer.parseInt(temp[1]); // type of section
                        float x = Float.parseFloat(temp[2]);    // x coord
                        float y = Float.parseFloat(temp[3]);    // y coord
                        float z = Float.parseFloat(temp[4]);    // z coord
                        float r = Float.parseFloat(temp[5]);    // r radius
                        int parentId = Integer.parseInt(temp[6]);   // parent id

                        Point3f segStart = null;        // start of segment
                        Point3f segEnd = null;          // end of segment
                        float segStarRad;                 // start of segment
                        float segEndRad;                   // end radius of segment

                        Section.SectionType ngSecType;
                        if(swcSecType == SWCSectionType.DENDRITE.getSecNum()) {
                            ngSecType = Section.SectionType.BASAL;
                        } else if(swcSecType == SWCSectionType.APICAL.getSecNum()) {
                            ngSecType = Section.SectionType.APICAL;
                        } else if(swcSecType == SWCSectionType.AXON.getSecNum()) {
                            ngSecType = Section.SectionType.NOT_MYELINIZED;
                        } else {
                            ngSecType = Section.SectionType.UNDEFINED;
                        }

                        // Soma section
                        if (swcSecType == SWCSectionType.SOMA.getSecNum()) {
                            Segment somaSeg = new Segment();
                            somaSeg.setId(segId);
                            somaSeg.setName("soma_" + segId);
                            segments.put(segId, new Pair<Segment, SWCSectionType>(somaSeg, SWCSectionType.SOMA));
                            segStart = new Point3f(x, y, z);
                            segStarRad = r;
                            //segEnd = null;
                            //segEndRad = -1;
                            segEnd = segStart;
                            segEndRad = r;
                            somaSeg.setSegment(segStart, segEnd, segStarRad, segEndRad);
                            somaSection.getSegments().add(somaSeg);
                            if (segments.containsKey(parentId)) {
                                logger.info("soma hat mehrere segmente!: " + segId);
                                Segment parSegment = segments.get(parentId).first;
                                parSegment.setEnd(new Point3f(x, y, z));
                                parSegment.setEndRadius(r);
                            }
                            //Axon section
                        } else if (swcSecType == SWCSectionType.AXON.getSecNum()) {
                            //logger.info("axon segment id: " + segId);
                            //logger.info("parent id: " + parentId);
                            Segment currentSeg = new Segment();
                            currentSeg.setId(segId);
                            segments.put(segId, new Pair<Segment, SWCSectionType>(currentSeg, SWCSectionType.AXON));
                            Segment parSegment = segments.get(parentId).first;
                            currentSeg.setParent(parSegment);
                            SWCSectionType parentSectionType = segments.get(parentId).second;
                            // soma is parent section
                            if (parentSectionType == SWCSectionType.SOMA) {
                                if (axon.getFirstSection() == null) {
                                    axon.setFirstSection(new AxonSection());
                                    currentAxonSec = axon.getFirstSection();
                                    currentAxonSec.setSectionType(ngSecType);
                                }
                                /*
                                // setze das Ende und den Endradius des Somasegments
                                if (parSegment.getEnd() == null) {
                                logger.info("id of axon seg: " + segId);
                                logger.info("setze die Enddaten des Somas!");
                                //parSegment.setEnd(new Point3f(x, y, z));
                                logger.info("start of soma: " + parSegment.getStart());
                                logger.info("end of soma: " + parSegment.getEnd());
                                //parSegment.setEndRadius(r);
                                }
                                 * 
                                 */
                            }
                            if (parentSectionType == SWCSectionType.SOMA || parentSectionType == SWCSectionType.AXON) {
                                segEnd = new Point3f(x, y, z);
                                segStart = parSegment.getEnd();
                                segStarRad = parSegment.getEndRadius();
                                if(parentSectionType == SWCSectionType.SOMA) {
                                  segStarRad = r;
                                }                                
                                segEndRad = r;
                                if (segStart == null) {
                                    segStart = segEnd;
                                    segStarRad = segEndRad;
                                }
                                currentSeg.setSegment(segStart, segEnd, segStarRad, segEndRad);
                                currentSeg.setParent(parSegment);
                                // kein branch, da der parent keine children hat
                                if (parSegment.getChild() == null) {
                                    parSegment.setChild(currentSeg);
                                    if (parentSectionType == SWCSectionType.SOMA) {
                                        somaSection.getSegments().add(currentSeg);
                                    } else {
                                        currentAxonSec.getSegments().add(currentSeg);
                                    }
                                    currentAxonSec.getSegments().add(currentSeg);
                                } else {
                                    if (currentAxonSec.getChildrenLink() == null) {
                                        AxonSection branch0 = new AxonSection();
                                        branch0.setSectionType(ngSecType);
                                        AxonSection branch1 = new AxonSection();
                                        branch1.getSegments().add(currentSeg);
                                        branch1.setSectionType(ngSecType);
                                        Segment parentChild = parSegment.getChild();
                                        // copy segments to branch0
                                        while (parentChild != null) {
                                            if (currentAxonSec.getSegments().contains(parentChild)) {
                                                currentAxonSec.getSegments().remove(parentChild);
                                                branch0.getSegments().add(parentChild);
                                            } else {
                                                break;
                                            }
                                            parentChild = parentChild.getChild();
                                        }
                                        
                                        SectionLink childrenLink = new SectionLink();
                                        childrenLink.setParental(currentAxonSec);
                                        currentAxonSec.setChildrenLink(childrenLink);
                                        childrenLink.setBranch0(branch0);
                                        childrenLink.setBranch1(branch1);
                                        childrenLink.getChildren().add(branch0);
                                        childrenLink.getChildren().add(branch1);
                                        branch0.setParentalLink(childrenLink);
                                        branch1.setParentalLink(childrenLink);
                                        currentAxonSec = branch1;
                                    } else {
                                        logger.info("axon section hat einen link zu anderen sektion!!");
                                    }
                                }
                            } else {
                                logger.info("parent type of axon segment is undefined! (ca1a.CNG.swc)");
                                if (currentAxonSec == null) {
                                    logger.info("parent of this axon segment is a dendrite?");
                                    if (axon.getFirstSection() == null) {
                                        axon.setFirstSection(new AxonSection());
                                        currentAxonSec = axon.getFirstSection();
                                        currentAxonSec.setSectionType(ngSecType);
                                    }
                                }
                            }
                            // dendrite section
                        } else if (swcSecType == SWCSectionType.DENDRITE.getSecNum() || swcSecType == SWCSectionType.APICAL.getSecNum()) { // dendrite section
                            Segment currentSeg = new Segment();
                            currentSeg.setId(segId);
                            segments.put(segId, new Pair<Segment, SWCSectionType>(currentSeg, SWCSectionType.DENDRITE));
                            Segment parSegment = segments.get(parentId).first;
                            currentSeg.setParent(parSegment);
                            SWCSectionType parentSegType = segments.get(parentId).second;
                            if (parentSegType == SWCSectionType.SOMA) {
                                dendrite = new Dendrite();
                                denList.add(dendrite);
                                currentDendriteSec = dendrite.getFirstSection();
                                currentDendriteSec.setSectionType(ngSecType);
                                /*
                                if (parSegment.getEnd() == null) {
                                parSegment.setEnd(new Point3f(x, y, z));
                                parSegment.setEndRadius(r);
                                }
                                 * 
                                 */
                            }
                            if (parentSegType == SWCSectionType.APICAL || parentSegType == SWCSectionType.DENDRITE || parentSegType == SWCSectionType.SOMA) {
                                segEnd = new Point3f(x, y, z);
                                segStart = parSegment.getEnd();
                                segStarRad = parSegment.getEndRadius();
                                if (parentSegType == SWCSectionType.SOMA) {
                                    segStarRad = r;
                                }

                                segEndRad = r;
                                if (segStart == null) {
                                    segStart = segEnd;
                                    segStarRad = segEndRad;
                                }
                                
                                currentSeg.setSegment(segStart, segEnd, segStarRad, segEndRad);
                                currentSeg.setParent(parSegment);
                                // link sections
                                if (parSegment.getChild() == null) {
                                    parSegment.setChild(currentSeg);
                                    if (parentSegType == SWCSectionType.SOMA) {
                                        somaSection.getSegments().add(currentSeg);
                                    } else {
                                        currentDendriteSec.getSegments().add(currentSeg);
                                    }
                                } else {
                                    if (currentDendriteSec.getChildrenLink() == null) {
                                        // branch0 is part of the old section
                                        DendriteSection branch0 = new DendriteSection();
                                        branch0.setSectionType(ngSecType);
                                        // branch1 is new section
                                        DendriteSection branch1 = new DendriteSection();
                                        branch1.setSectionType(ngSecType);
                                        /*
                                        if (parentSegType == SWCSectionType.APICAL) {
                                            branch0.setSectionType(DendriteSection.SectionType.APICAL);
                                            branch1.setSectionType(DendriteSection.SectionType.APICAL);
                                        } else {
                                            branch0.setSectionType(DendriteSection.SectionType.BASAL);
                                            branch1.setSectionType(DendriteSection.SectionType.BASAL);
                                        }
                                         *
                                         */
                                        branch1.getSegments().add(currentSeg);
                                        Segment parentChild = parSegment.getChild();
                                        //logger.info("Verweigung(parent ID, child 0 ID, child 1 ID): " + parSegment.getId() + ", " + parentChild.getId() + ", " + currentSeg.getId());
                                        // copy segments to branch0
                                        while (parentChild != null) {
                                            if (currentDendriteSec.getSegments().contains(parentChild)) {
                                                currentDendriteSec.getSegments().remove(parentChild);
                                                branch0.getSegments().add(parentChild);
                                            } else {
                                                break;
                                            }
                                            parentChild = parentChild.getChild();
                                        }
                                        SectionLink childrenLink = new SectionLink();
                                        childrenLink.setParental(currentDendriteSec);
                                        currentDendriteSec.setChildrenLink(childrenLink);
                                        childrenLink.setBranch0(branch0);
                                        childrenLink.setBranch1(branch1);
                                        childrenLink.getChildren().add(branch0);
                                        childrenLink.getChildren().add(branch1);
                                        branch0.setParentalLink(childrenLink);
                                        branch1.setParentalLink(childrenLink);
                                        currentDendriteSec = branch1;
                                    } else {
                                        logger.info("diese dendriten sektion hat einen link zu anderen sektionen!");
                                    }
                                }
                            }
                            if (parentSegType == SWCSectionType.AXON) {
                                logger.info("parent segment of a dendrite segment is an axon segment!! why :) ??");
                            }
                        } else {
                            //logger.info("Dieses Segment ist undefiniert(wahrscheinlich die verschiedenen Schichten..): " + secType);
                            //logger.info("Id des Segmenten: " + segId);
                            Segment curSeg = new Segment();
                            curSeg.setId(segId);
                            segments.put(segId, new Pair<Segment, SWCSectionType>(curSeg, SWCSectionType.UNDEFINED));
                            Segment parSegment = segments.get(parentId).first;
                            SWCSectionType segType = segments.get(parentId).second;
                            segEnd = new Point3f(x, y, z);
                            segStart = parSegment.getEnd();
                            segStarRad = parSegment.getEndRadius();
                            segEndRad = r;
                            if (segStart == null) {
                                segStart = segEnd;
                                segStarRad = segEndRad;
                            }
                            curSeg.setSegment(segStart, segEnd, segStarRad, segEndRad);
                            curSeg.setParent(parSegment);
                            if (segType == SWCSectionType.UNDEFINED) {
                                Section undefinedSec = undefinedSections.get(parentId);
                                while (undefinedSec == null) {
                                    Segment parSeg = parSegment.getParent();
                                    if (parSeg == null) {
                                        continue;
                                    }
                                    int newParID = parSeg.getId();
                                    parSegment = parSeg;
                                    undefinedSec = undefinedSections.get(newParID);
                                    //logger.info("parentid: " + newParID);
                                }
                                undefinedSec.getSegments().add(curSeg);
                            } else {
                                Section undefinedSec = new Section();
                                undefinedSec.setSectionType(Section.SectionType.UNDEFINED);
                                undefinedSec.getSegments().add(curSeg);
                                undefinedSections.put(segId, undefinedSec);
                            }
                        }
                    }
                }
            }
            //set soma rad and mid point
            List<Segment> somaSegments = somaSection.getSegments();
            float somaRad = 0.0f;
            for (Segment sSeg : somaSegments) {
                if (sSeg.getEndRadius() < 0) {
                    somaSegments.remove(sSeg);
                    continue;
                }
                somaRad += sSeg.getStartRadius();
                somaRad += sSeg.getEndRadius();
            }
            int somaSegNum = somaSegments.size();
            logger.info("number of soma segments: " + somaSegNum);
            logger.info("somaRad before: " + somaRad);
            somaRad /= 2.0f * somaSegNum;
            logger.info("somaRad after: " + somaRad);
            Point3f somaRadii = new Point3f(somaRad, somaRad, somaRad);
            soma.changeRadii(somaRadii);

            Point3f startPoint = somaSegments.get(0).getStart();
            Point3f endPoint = null;
            if (somaSegNum > 2) {
                endPoint = somaSegments.get(somaSegNum - 1).getEnd();
            } else {
                endPoint = new Point3f(startPoint);
            }
            Point3f center = new Point3f();
            center.add(startPoint, endPoint);
            center.scale(0.5f);
            soma.setCellipsoid(center, somaRadii);
            soma.setCylindricRepresentant(somaSection);
            neuron.getUndefinedSections().addAll(undefinedSections.values());
            net.getNeuronList().add(neuron);

            neuron.setName(f.getName());

            if (ngView != null) {
                ngView.outPrintln(swcComment);
                ngView.setNet(net);
            }
            neuron.infoNeuron();
        } catch (IOException e) {
            logger.error(e, e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public static void main(String[] args) {
        NeuGenLogger.initLogger();
        //File swcData = new File("demo/n400.swc");
        //File swcData = new File("demo/n25fts.CNG.swc");
        //File swcData = new File("demo/n145.swc");
        //File swcData = new File("demo/ca1b.swc");
        File swcData = new File("demo/NM2.swc");
        SWCReader readSWC = new SWCReader();
        readSWC.readSWC(swcData);
    }
}
