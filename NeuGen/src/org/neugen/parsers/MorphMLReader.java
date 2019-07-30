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

import java.io.FileInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.neugen.datastructures.neuron.NeuronBase;
import org.neugen.datastructures.Section;
import org.neugen.datastructures.Segment;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.vecmath.Point3f;

import org.apache.log4j.Logger;
import org.neugen.datastructures.AxonSection;
import org.neugen.datastructures.NetBase;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.DendriteSection;
import org.neugen.datastructures.Net;
import org.neugen.datastructures.neuron.Neuron;
import org.neugen.datastructures.SectionLink;

/**
 * @todo: setze den Sektionentyp!
 *
 * Read a neuroML file in xml format and create a neuronal net.
 * @author Sergei Wolf
 */
public final class MorphMLReader extends XMLFilterImpl {

    /** use to log messages */
    private static final Logger logger = Logger.getLogger(MorphMLReader.class.getName());
    // If getAncestorElement is called for non existent ancestors...
    private static final String NULL_ELEMENT = "--- Null Element ---";
    private Section currentSection;
    private Segment currentSegment;
    private Stack<String> elementStack;
    private Neuron neuron;
    private Net net;
    //cable id: for neugen export files
    private int currentSecId;
    private Map<Integer, Section> sections;
    private Map<Integer, Section> somaSec;
    private Map<Integer, Section> axonSec;
    private Map<Integer, Section> dendSec;
    private Map<Integer, Segment> segments;
    // diese Tabelle speichert zu jedem Segment die zugehörige Sektion-Id
    private Map<Integer, Integer> segIdSecId;
    /**
     * @todo
     * Soma kann auch aus mehreren Sektionen aufgebaut sein.
     * Merke in der Liste die IDs der Sektionen.
     * Wenn nicht alle Sektionen des Soma rausgeschrieben werden,
     * dann braucht man eine Liste in Cellipsoid
     *
     */
    private List<Integer> somaSecIds;
    private int contentProgress;
    private NeuroMLReaderTask myTask;
    /** total number of sections */
    private int totalNumSecs;
    /** total numbert of segments */
    private int totalNumSegs;

    public MorphMLReader() {
        init();
    }

    public MorphMLReader(NeuroMLReaderTask task) {
        myTask = task;
        init();
    }

    public void init() {
        contentProgress = 0;
        totalNumSecs = 0;
        totalNumSegs = 0;
        currentSecId = 0;

        sections = new HashMap<Integer, Section>();
        somaSec = new HashMap<Integer, Section>();
        axonSec = new HashMap<Integer, Section>();
        dendSec = new HashMap<Integer, Section>();
        segments = new HashMap<Integer, Segment>();
        segIdSecId = new HashMap<Integer, Integer>();
        somaSecIds = new ArrayList<Integer>();
        elementStack = new Stack<String>();
    }

    public void setProgress(int textSize) {
        if (myTask != null) {
            contentProgress += textSize;
            //System.out.println("ProgrtessBar: value " + contentProgress + ", " + myTask.fileLength);
            if (contentProgress >= myTask.fileLength) {
                contentProgress = myTask.fileLength;
            }
            myTask.setMyProgress(contentProgress, 0, myTask.fileLength);
        }
    }

    public int getTotalNumSecs() {
        return totalNumSecs;
    }

    public void incrementTotalNumSecs() {
        totalNumSecs++;
    }

    public int getTotalNumSegs() {
        return totalNumSegs;
    }

    public void incrementTotalNumSegs() {
        totalNumSegs++;
    }

    public Net getNet() {
        return net;
    }

    public void setCurrentElement(String newElement) {
        elementStack.push(newElement);
    }

    public String getCurrentElement() {
        return elementStack.peek();
    }

    public void stepDownElement() {
        elementStack.pop();
    }

    public String getParentElement() {
        return getAncestorElement(1);
    }

    /**
     * Taking the child parent thing to it's logical extension...
     * parent element is 1 generation back, parent's parent is 2 back, etc.
     */
    public String getAncestorElement(int generationsBack) {
        if (elementStack.size() < generationsBack + 1) {
            return NULL_ELEMENT;
        }
        return elementStack.elementAt(elementStack.size() - (generationsBack + 1));
    }

    /**
     * wird immer aufgerufen, wenn Zeichen im Dokument auftauchen
     *
     * @param ch Charakter Array
     * @param start Startindex der Zeichen in ch
     * @param length Länge der Zeichenkette
     */
    @Override
    public void characters(char[] ch, int start, int length) {
        String contents = new String(ch, start, length);
        if (contents.trim().length() > 0) {
            //System.out.print(getCurrentElement());
            String metaData = MetadataConstants.GROUP_ELEMENT;
            if (getCurrentElement().equals(metaData)) {
                Section sec = sections.get(currentSecId);
                //System.out.println("Die Meta und ID: " + currentSecId + ", " + contents);
                //this.sections.remove(currentSecId);
                if (contents.equals(DataStructureConstants.AXONAL_GROUP)) {
                    axonSec.put(currentSecId, sec);
                } else if (contents.equals(DataStructureConstants.DENDRITIC_GROUP)) {
                    dendSec.put(currentSecId, sec);
                } else if (contents.equals(DataStructureConstants.SOMA_GROUP)) {
                    somaSec.put(currentSecId, sec);
                } else {
                    sections.put(currentSecId, sec);
                }
            }
        }
        setProgress(length);
    }

    @Override
    public void startDocument() {
        net = new NetBase();
    }

    @Override
    public void endDocument() {
        //System.out.println("ProgressBar: " + contentProgress);
        //System.out.println("FileLength: " + myTask.fileLength);
        //setProgress(myTask.fileLength);
        int i = 0;
        for (Neuron n : net.getNeuronList()) {
            n.setIndex(i);
            i++;
        }

        if (myTask != null) {
            myTask.setMyProgress(myTask.fileLength, 0, myTask.fileLength);
        }
    }

    /**
     * wird bei jedem öffnenden Tag aufgerufen
     * bei leerem Tag wird statElement und endElement direkt hintereinander
     * aufgerufen
     *
     * @param namespaceURI URI des Namespaces für diese Element, kann auch ein leerer String sein
     * @param localName Lokaler Name des Elements, kann auch ein leerer String sein
     * @param qName Qualifizierter Name des Elements: cells,cell,segments,segment usw.
     * @param attributes Liste der Attribute
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        setProgress(localName.length() + namespaceURI.length() + qName.length() + 2);
        for (int i = 0; i < attributes.getLength(); i++) {
            String attName = attributes.getLocalName(i);
            setProgress(attName.length());
        }
        //System.out.println("Member: startElement");
        // lege den Tag Namen auf den Stack
        setCurrentElement(localName);                   //cells,cell,segments,segment,usw.
        // wenn der Namen Cell ist, dann erzeuge eine Zelle
        // <cell name="soma_0">
        if (getCurrentElement().equals(MorphMLConstants.CELL_ELEMENT)) //cell
        {
            String name = attributes.getValue(MorphMLConstants.CELL_NAME_ATTR); //name
            neuron = new NeuronBase();
            neuron.setName(name);
        } //wenn das Tag ein Segment ist
        //<segment id="1541" name = "Seg1_axon_0" parent="1540" cable = "197">
        else if (getCurrentElement().equals(MorphMLConstants.SEGMENT_ELEMENT)) //segment
        {
            String id = attributes.getValue(MorphMLConstants.SEGMENT_ID_ATTR);  //id
            String name = attributes.getValue(MorphMLConstants.SEGMENT_NAME_ATTR);  //name
            String sectionId = attributes.getValue(MorphMLConstants.SEGMENT_CABLE_ID_ATTR); //cable

            Segment segment = new Segment();
            segment.setId(Integer.parseInt(id));
            segment.setName(name);
            incrementTotalNumSegs();

            Section section;
            Integer sectionIdInteger = Integer.valueOf(sectionId);
            if (sections.containsKey(sectionIdInteger)) {
                section = sections.get(sectionIdInteger);
            } else {
                section = new Section();
                section.setId(Integer.parseInt(sectionId));
                sections.put(sectionIdInteger, section);
                incrementTotalNumSecs();
                //logger.info("new Section: " + section.getId());
            }

            Integer segmentIdInteger = Integer.valueOf(id);
            segments.put(segmentIdInteger, segment);
            segIdSecId.put(segmentIdInteger, sectionIdInteger);
            currentSegment = segment;

            section.getSegments().add(segment);
            //logger.info("newSeg: " + name + " added to " + section.getId());
            String parent = attributes.getValue(MorphMLConstants.SEGMENT_PARENT_ATTR);  //parent

            if (parent != null) {
                try {
                    //logger.info("parent: " + parent);
                    Segment parentSegment = segments.get(Integer.valueOf(parent));
                    parentSegment.getName();     // Just to check for null pointer...
                    segment.setParent(parentSegment);
                    int parentSegId = parentSegment.getId();
                    Integer parentSectionId = segIdSecId.get(parentSegId);
                    //System.out.println("Debug start");
                    // nur bei unterschiedlichen Sektionen.
                    if (!parentSectionId.equals(sectionIdInteger)) {
                        //System.out.println("section and parent section id: " + sectionId + " and " + parentSectionId );
                        //Sektionen verbinden
                        Section parentSection = sections.get(parentSectionId);
                        if (parentSection.getChildrenLink() == null) {
                            SectionLink childrenLink = new SectionLink();
                            childrenLink.setParental(parentSection);
                            childrenLink.setBranch0(section);

                            parentSection.setChildrenLink(childrenLink);
                            section.setParentalLink(childrenLink);

                            childrenLink.setParental(parentSection);
                            childrenLink.getChildren().add(section);
                        } else {
                            SectionLink childrenLink = parentSection.getChildrenLink();
                            if (childrenLink.getBranch0() == null) {
                                childrenLink.setBranch0(section);
                                childrenLink.getChildren().add(section);
                            } else if (childrenLink.getBranch1() == null) {
                                childrenLink.setBranch1(section);
                                childrenLink.getChildren().add(section);
                            }

                            if (section.getParentalLink() == null) {
                                section.setParentalLink(childrenLink);
                            }
                        }
                    }
                    //System.out.println("Debug end");
                    //welche id hat der Parent
                    //System.out.println("segID, segPID: " + parent + ", " + parentId);
                    //System.out.println("segmentId, parentSegId: " + segmentIdInteger + ", " + parentId);
                } catch (Exception e) {
                    logger.error("problem location parent of: " + parent, e);
                }
            } else {
                logger.debug("Section has no parent: " + section.getId());
            }
            //System.out.println("Segment gefunden-end");
        } //<proximal x="0" y="0" z="10.68" diameter="10.68"/>
        else if (getCurrentElement().equals(MorphMLConstants.SEGMENT_PROXIMAL_ELEMENT)) //proximal
        {
            //System.out.println("Segment proximal");
            String xVal = attributes.getValue(MorphMLConstants.POINT_X_ATTR);          //x
            String yVal = attributes.getValue(MorphMLConstants.POINT_Y_ATTR);          //y
            String zVal = attributes.getValue(MorphMLConstants.POINT_Z_ATTR);          //z
            String diamVal = attributes.getValue(MorphMLConstants.POINT_DIAM_ATTR);    //diameter
            currentSegment.setStart(xVal, yVal, zVal, diamVal);
            //this.currentSegment.set_segment(sstart, send, sradiusstart, sradiusend);
            //TODO speichere die Daten in die Hash : id + segmentData
            //System.out.println("Segment proximal-end");
        } //<distal x="0" y="0" z="0" diameter="10.68"/>
        else if (getCurrentElement().equals(MorphMLConstants.SEGMENT_DISTAL_ELEMENT)) //distal
        {
            String xVal = attributes.getValue(MorphMLConstants.POINT_X_ATTR);          //x
            String yVal = attributes.getValue(MorphMLConstants.POINT_Y_ATTR);          //y
            String zVal = attributes.getValue(MorphMLConstants.POINT_Z_ATTR);          //z
            String diamVal = attributes.getValue(MorphMLConstants.POINT_DIAM_ATTR);    //diameter
            currentSegment.setEnd(xVal, yVal, zVal, diamVal);
        } //<cable id = "0" name = "soma_0" fractAlongParent = "0"/>
        else if (getCurrentElement().equals(MorphMLConstants.CABLE_ELEMENT) //cable
                && getAncestorElement(1).equals(MorphMLConstants.CABLES_ELEMENT)) {
            String id = attributes.getValue(MorphMLConstants.CABLE_ID_ATTR);       //id
            String name = attributes.getValue(MorphMLConstants.CABLE_NAME_ATTR);   //name
            Section section = null;
            Integer sectionIdInteger = Integer.valueOf(id);
            section = sections.get(sectionIdInteger);
            if (section == null) {
                String mes = "Problem parsing MorphML file. Section/cable " + name
                        + " does not refer to one referenced in the list of segments";
                logger.error(mes);
                throw new SAXException(mes);
            }
            section.setName(name);
            section.setId(Integer.parseInt(id));
            // notwendig, denn in der Funktion End Element werden wir auf die Section wieder zugreifen
            currentSection = section;
            currentSecId = sectionIdInteger;
            //TODO fractAlongParent is not implementet
        }
        //System.out.println("Member: startElement- end");
    }

    /**
     * wird bei jedem schließenden Tag aufgerufen,
     *
     * @param namespaceURI URI des Namespaces für dieses Element, kann auch ein leerer String sein
     * @param localName Lokaler Name des Elements, kann auch ein leerer String sein
     * @param qName Qualifizierter Name des Elements
     */
    //TODO: Axon, Soma und Dendriten erzeugen!!
    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
        // cell end
        if (getCurrentElement().equals(MorphMLConstants.CELL_ELEMENT)) {
            net.getNeuronList().add(neuron);
            net.setTotalNumOfSegments();
            /*myTask.ngView.outPrintln("name of neuron: " + neuron.getInstanceName());
            myTask.ngView.outPrintln("soma: ");
            myTask.ngView.outPrintln(" number of sections: " + neuron.getSoma().getSomaSections().size());
            myTask.ngView.outPrintln(" number of segments: " + neuron.getSoma().getNumberOfSomaSegments());
            myTask.ngView.outPrintln("axon: ");
            myTask.ngView.outPrintln(" number of sections: " + neuron.getAxon().getNumberOfSections());
            myTask.ngView.outPrintln(" number of segments: " + neuron.getAxon().getNumberOfSegments());
            myTask.ngView.outPrintln("dendrites: " + neuron.getDenList().size());
            myTask.ngView.outPrintln(" number of sections: " + neuron.getNumberOfAllDenSections());
            myTask.ngView.outPrintln(" number of segments: " + neuron.getNumberOfAllDenSegments());
            myTask.ngView.outPrintln();*/
            //neuron.infoNeuron();
            //myTask.setTaskOutput(neuron.getInstanceName() + "\n");
            //taskOutput.setCaretPosition(taskOutput.getText().length());
        } else if (getCurrentElement().equals(MorphMLConstants.CABLE_ELEMENT) && getAncestorElement(1).equals(MorphMLConstants.CABLES_ELEMENT)) {
            String somaName = neuron.getName().toLowerCase();
            String currentSectionName = currentSection.getName().toLowerCase();
            //Soma end
            if (currentSectionName.indexOf("soma") >= 0 || currentSectionName.indexOf(somaName) >= 0) {
                somaSecIds.add(currentSecId); //speichere die Sektionen des Soma in die Liste
                if (neuron.getSoma().getCylindricRepresentant() == null) {
                    Cellipsoid soma = neuron.getSoma();
                    List<Segment> segList = currentSection.getSegments();
                    int nsegs = segList.size();
                    if (nsegs >= 1) {
                        Segment firstSeg = segList.get(0);
                        Point3f mid = firstSeg.getCenter();
                        if (nsegs > 1) {
                            Segment lastSeg = segList.get(nsegs - 1);
                            //mid = Fork.add(firstSeg.getStart(), lastSeg.getEnd(), d);
                            mid.add(firstSeg.getStart(), lastSeg.getEnd());
                            mid.scale(0.5f);
                        }
                        soma.setMid(mid);
                    }
                    float radius = 0.0f;
                    for (Segment segment : segList) {
                        if (segment.getStartRadius() > radius) {
                            radius = segment.getStartRadius();
                        }

                        if (segment.getEndRadius() > radius) {
                            radius = segment.getEndRadius();
                        }
                    }
                    Point3f sradii = new Point3f(radius, radius, radius);
                    soma.changeRadii(sradii);
                    soma.setCylindricRepresentant(currentSection);
                    soma.getSections().add(currentSection);
                    logger.info("Soma: " + currentSection.getName());
                } else {
                    //logger.info("Soma hat noch eine Sektion: " + currentSection.getName());
                    // Segment firstSeg = currentSection.getSegments().get(0);
                    //firstSeg.printSegment();
                    //soma.getCylindricRepresentant().setChildren(children);
                    //neu, um alle Sektionen des Somas zu durchlaufen
                    neuron.getSoma().getSections().add(currentSection);
                }
            } //Axon end
            else if (currentSectionName.indexOf("axon") >= 0) {
                SectionLink parentLink = currentSection.getParentalLink();
                if (parentLink != null) {
                    if (parentLink.getParental() != null) {
                        int parentID = parentLink.getParental().getId();
                        if (somaSecIds.contains(parentID)) {
                            logger.info("axon ist an soma angeschlossen!");
                            AxonSection axonSection = new AxonSection(currentSection);
                            neuron.getAxon().setFirstSection(axonSection);
                            //     System.out.println("Axon: " + currentSection.getSectionName() + ", TypeID: "
                            //             + currentSection.getSectionId() + ", an Soma");
                            //Segment firstSeg = currentSection.getSegments().get(0);
                            //firstSeg.printSegment();
                        }
                    }
                } else {
                    logger.info("Axon has no parent! Set first section: " + currentSection.getId());
                    logger.info("number of segments: " + currentSection.getSegments().size());
                    AxonSection axonSection = new AxonSection(currentSection);
                    neuron.getAxon().setFirstSection(axonSection);
                }
            } /*
            else if(currentSectionName.indexOf("dendrite") >=0
            || currentSectionName.indexOf("apical") >=0
            || currentSectionName.indexOf("basal") >=0
            || currentSectionName.indexOf("user") >=0)
             */ //alle anderen Sektionen
            else {
                if (!getCurrentElement().equals(MorphMLConstants.CELL_ELEMENT)) {
//                System.out.println("localName: " + localName);
                    // System.out.println("Dendrite Sektion: " + currentSection.getSectionName());
                    // if (soma.getCylindricRepresentant().getSectionId() == currentSection.getParental().parental.getSectionId()) {
                    SectionLink parentLink = currentSection.getParentalLink();
                    if (parentLink != null) {
                        if (parentLink.getParental() != null) {
                            int parentID = parentLink.getParental().getId();
                            if (somaSecIds.contains(parentID)) {
                                logger.info("parent found! (dendrite)");
                                DendriteSection denSection = new DendriteSection(currentSection);
                                Dendrite dendrite = new Dendrite();
                                dendrite.setFirstSection(denSection);
                                neuron.getDendrites().add(dendrite);
                                logger.info("Dendrit: " + currentSection.getName()
                                        + ", secID: " + currentSection.getId() + ", an Soma");
                            }
                        }
                    } else {
                        DendriteSection denSection = new DendriteSection(currentSection);
                        Dendrite dendrite = new Dendrite();
                        dendrite.setFirstSection(denSection);
                        neuron.getDendrites().add(dendrite);
                    }
                }
            }
        }
        stepDownElement();
        setProgress(qName.length() + 2);
    }

    public void runMorphMLReader(String fname) {
        try {
            File f = new File(fname);
            logger.info("Pfad: " + f.getAbsolutePath());
            FileInputStream instream = null;
            InputSource is = null;
            //neuen Sax Parser erzeugen
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            //MorphMLReader mmlBuilder = new MorphMLReader();
            xmlReader.setContentHandler(this);
            instream = new FileInputStream(f);
            is = new InputSource(instream);
            xmlReader.parse(is);
            logger.info("Total number of segments: " + getTotalNumSegs());
            logger.info("Total number of sections: " + getTotalNumSecs());
        } catch (Exception e) {
            logger.error("Fehler in der Main-Funktion: " + e, e);
        }
    }

    //TODO Meta group soll noch imlementiert werden!
    public static void main(String args[]) {
        //System.out.println(MorphMLReader.class.toString());
        String cA1_1 = "CA1GatingModelView.xml";
        String CA1_2 = "CA1ModelView.xml";
        String CA1_3 = "CA1XiaoModelView.xml";
        //File f = new File("goodCells/CA1MagicalModelView.xml");
        //File f = new File("goodCells/CA1LamotrigineModelView.xml");
        //File f = new File("CA1Migliore.xml");
        MorphMLReader mmlBuilder = new MorphMLReader();
        mmlBuilder.runMorphMLReader(CA1_3);
        //System.out.println(MorphMLReader.class.toString() + " -end+");
    }
}
