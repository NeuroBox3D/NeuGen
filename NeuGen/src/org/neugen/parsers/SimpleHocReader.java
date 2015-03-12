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
package org.neugen.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.AxonSection;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.NetBase;
import org.neugen.datastructures.neuron.NeuronBase;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.SectionLink;
import org.neugen.datastructures.Segment;

/**
 * @author Sergei Wolf
 */
public final class SimpleHocReader {

    private NeuronBase neuron;
    private Cellipsoid soma;
    private Axon axon;
    private List<Dendrite> denList;
    private NetBase net;
    private Map<Integer, Section> sections = new HashMap<Integer, Section>();
    private Map<Integer, Segment> segments = new HashMap<Integer, Segment>();
    private int segId = 0;
    private boolean newCellCreated = false;
    private boolean oneSecInCell = true;
    // diese Tabelle speichert zu jedem Segment die zugehörige Sektion-Id
    private Map<Integer, Integer> segIdSecId = new HashMap<Integer, Integer>();
    //Soma kann auch aus mehreren Sektionen aufgebaut sein. Merke in der Liste die IDs der Sektionen
    // wenn nicht alle Sektionen des Soma rausgeschrieben werden, dann brauche ich eine Liste in Cellipsoid
    private List<Integer> somaSecIds = new ArrayList<Integer>();

    public NetBase getNet() {
        return net;
    }

    private void setStartOfSegment(String data, Segment segment) {
        //System.out.println(data);
        String[] dataArray = data.split(" ");
        String xVal = dataArray[0]; //System.out.println("x: " + xVal);
        String yVal = dataArray[1]; //System.out.println("y: " + yVal);
        String zVal = dataArray[2]; //System.out.println("z: " + zVal);
        String diamVal = dataArray[3]; //System.out.println("d: " + diamVal);
        segment.setStart(xVal, yVal, zVal, diamVal);
    }

    private void setEndOfSegment(String data, Segment segment) {
        //System.out.println(data);
        String[] dataArray = data.split(" ");
        String xVal = dataArray[0]; //System.out.println("x: " + xVal);
        String yVal = dataArray[1]; //System.out.println("y: " + yVal);
        String zVal = dataArray[2]; //System.out.println("z: " + zVal);
        String diamVal = dataArray[3]; //System.out.println("d: " + diamVal);
        segment.setEnd(xVal, yVal, zVal, diamVal);
    }

    private void addDataToNet() {
        if (newCellCreated) {
            System.out.println("save net");
            net.getNeuronList().add(neuron);
            /*NeuGenView.outPrintln("name of neuron: " + neuron.getInstanceName());
            NeuGenView.outPrintln("soma: ");
            NeuGenView.outPrintln(" number of sections: " + soma.getSomaSections().size());
            NeuGenView.outPrintln(" number of segments: " + soma.getNumberOfSomaSegments());
            NeuGenView.outPrintln("axon: ");
            NeuGenView.outPrintln(" number of sections: " + axon.getNumberOfSections());
            NeuGenView.outPrintln(" number of segments: " + axon.getNumberOfSegments());
            NeuGenView.outPrintln("dendrites: " + neuron.getDen_list().size());
            NeuGenView.outPrintln(" number of sections: " + neuron.getNumberOfAllDenSections());
            NeuGenView.outPrintln(" number of segments: " + neuron.getNumberOfAllDenSegments());
            NeuGenView.outPrintln();*/
        }
    }

    public void loadFromSHOCFile(File file) {
        try {
            Reader in = new FileReader(file);
            BufferedReader lineReader = new BufferedReader(in);
            String nextLine = null;

            /**
             * @todo: generate parameter files after we loaded the data
             */
            net = new NetBase();
        while ((nextLine = lineReader.readLine()) != null) {
                nextLine = nextLine.trim();
                if (nextLine.length() == 0) {
                } else if (nextLine.startsWith("begin soma")) {
                    //System.out.println("Soma begin: "+ nextLine);
                    String sectionToCreate = nextLine.substring("begin ".length());
                    String[] secData = sectionToCreate.split(" ");
                    String name = secData[0] + secData[1];
                    String somaId = secData[1];
                    //System.out.println("soma Name: "+ name );
                    somaSecIds.add(new Integer(somaId)); //speichere die Sektionen des Soma in die Liste
                    //nsegs
                    String data = lineReader.readLine().trim();
                    secData = data.split(" ");
                    String parentType = null;
                    String nsegs;
                    String parentId = null;
                    if (data.startsWith("parent")) {
                        parentType = secData[1];
                        parentId = secData[2];
                        nsegs = lineReader.readLine().trim();
                        //System.out.println("parent is: " + parentType + ", " + parentId);
                        //oneSecInCell = false;
                    } else {
                        addDataToNet();
                        nsegs = data;
                        System.out.println("new soma start");
                        neuron = new NeuronBase();
                        neuron.setName(name);
                        soma = neuron.getSoma();
                        axon = neuron.getAxon();
                        denList = neuron.getDendrites();
                        newCellCreated = true;
                    }
                    //System.out.println("number of segments: " + nsegs);
                    String proximal = lineReader.readLine().trim();
                    Segment segment = new Segment();
                    segId++;
                    setStartOfSegment(proximal, segment);
                    String distal = lineReader.readLine().trim();
                    setEndOfSegment(distal, segment);
                    segment.setName("seg_" + name + segId);
                    //segment.setSegmentId(segId);

                    Section section = null;
                    Integer sectionIdInteger = new Integer(somaId);
                    if (sections.containsKey(sectionIdInteger)) {
                        section = sections.get(sectionIdInteger);
                    } else {
                        section = new Section();
                        section.setId(sectionIdInteger);
                        sections.put(sectionIdInteger, section);
                    }
                    section.getSegments().add(segment);

                    while (true) {
                        nextLine = lineReader.readLine().trim();
                        if (nextLine.startsWith("end")) {
                            break;
                        }

                        //speichere die id des Segmenten und den Segmenten selbst
                        Integer segmentIdInteger = new Integer(segId);
                        segments.put(segmentIdInteger, segment);
                        segIdSecId.put(segmentIdInteger, sectionIdInteger);
                        Segment parSeg = segment;
                        //String distalSeg = lineReader.readLine().trim();
                        //System.out.println("distal start: " + nextLine);
                        segment = new Segment();
                        segId++;
                        setEndOfSegment(nextLine, segment);
                        segment.setName("seg_" + name + segId);
                        //segment.setSegmentId(segId);
                        segment.setParent(parSeg);
                        //System.out.println("distal end: " + nextLine);
                        section.getSegments().add(segment);
                    }
                    if (nextLine.startsWith("end")) {
                        if (soma.getCylindricRepresentant() == null) {
                            soma.setCylindricRepresentant(section);
                            soma.getSections().add(section);
                        } else {
                            soma.getSections().add(section);
                        }
                    }
                } else if (nextLine.startsWith("begin axon")) {
                    System.out.println("Axon begin: " + nextLine);

                    String sectionToCreate = nextLine.substring("begin ".length());
                    String[] secData = sectionToCreate.split(" ");
                    String name = secData[0] + secData[1];
                    String axonId = secData[1];

                    //nsegs
                    String data = lineReader.readLine().trim();
                    secData = data.split(" ");
                    String parentType = null;
                    String nsegs;
                    String parentSecId = null;

                    parentType = secData[1];
                    parentSecId = secData[2];
                    nsegs = lineReader.readLine().trim();

                    String proximal = lineReader.readLine().trim();
                    Segment segment = new Segment();
                    segId++;
                    setStartOfSegment(proximal, segment);
                    String distal = lineReader.readLine().trim();
                    System.out.println("distal: " + distal);
                    setEndOfSegment(distal, segment);
                    segment.setName("seg_" + name + segId);
                    //segment.setSegmentId(segId);

                    Section section = null;
                    Integer sectionIdInteger = new Integer(axonId);
                    if (sections.containsKey(sectionIdInteger)) {
                        section = sections.get(sectionIdInteger);
                    } else {
                        section = new Section();
                        section.setId(sectionIdInteger);
                        sections.put(sectionIdInteger, section);
                    }
                    section.getSegments().add(segment);
                    while (true) {
                        //speichere die id des Segmenten und den Segmenten selbst
                        Integer segmentIdInteger = new Integer(segId);
                        segments.put(segmentIdInteger, segment);
                        segIdSecId.put(segmentIdInteger, sectionIdInteger);

                        Segment parentSegment = segment.getParent();

                        if (parentSegment != null) {
                            int parentSegId = parentSegment.getId();
                            Integer parentSectionId = segIdSecId.get(parentSegId);

                            if (!parentSectionId.equals(sectionIdInteger)) {
                                System.out.println("section and parent section id: " + axonId + " and " + parentSectionId);
                                //Sektionen verbinden
                                Section parentSection = sections.get(parentSectionId);
                                SectionLink parentLink = parentSection.getChildrenLink();
                                if (parentLink == null) {
                                    parentLink = new SectionLink();
                                    parentSection.setChildrenLink(parentLink);
                                    parentLink.setParental(parentSection);
                                }
                                parentLink.getChildren().add(section);
                                section.setParentalLink(parentLink);
                            }
                        }

                        nextLine = lineReader.readLine().trim();
                        if (nextLine.startsWith("end")) {
                            break;
                        }

                        Segment parSeg = segment;
                        //String distalSeg = lineReader.readLine().trim();
                        //System.out.println("distal start: " + nextLine);
                        segment = new Segment();
                        segId++;
                        segment.setParent(parSeg);
                        setEndOfSegment(nextLine, segment);
                        segment.setName("seg_" + name + segId);
                        segment.setId(segId);
                        //System.out.println("distal end: " + nextLine);
                        section.getSegments().add(segment);
                    }

                    if (nextLine.startsWith("end")) {
                    }
                    if (somaSecIds.contains(new Integer(parentSecId))) {
                        axon.setFirstSection((AxonSection) section);
                    }


                    //System.out.println("parent is: " + parentType + ", " + parentId);

                    //  System.out.println("number of segments: " + nsegs);
                } else if (nextLine.startsWith("begin dendrite")) {
                    //  System.out.println("Dendrite begin: "+ nextLine);

                    String parent = lineReader.readLine().trim();

                    String nsegs = lineReader.readLine().trim();
                    //  System.out.println("number of segments: " + nsegs);
                }

            }
            if (net.getNeuronList().isEmpty()) {
                addDataToNet();
            }
            in.close();
            lineReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Lesen der Datei");
        }
    }
}
