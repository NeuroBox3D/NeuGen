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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;

/**
 * Each section consists of a set of segments, which are sequentionally connected.
 * 
 * @author Jens Eberhard
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class Section implements Serializable {

    static final long serialVersionUID = -7031941169184524614L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(Section.class.getName());
    private int id = -1;
    private static int secCounter;
    /** Segments this section consists of.*/
    protected final List<Segment> segmentList;
    /** Link to the parent section this section is branching of. (NULL if a root section)*/
    protected SectionLink parentalLink;
    /** Link to the both branches of this section. (NULL, if a final section) */
    protected SectionLink childrenLink;
    protected String name;

    public enum SectionType {

        BASAL(0), APICAL(1), OBLIQUE(2), MYELINIZED(3), NOT_MYELINIZED(4), UNDEFINED(5), SOMA(6);
        private int secNum;

        private SectionType(int secNum) {
            this.secNum = secNum;
        }

        public int getSecNum() {
            return secNum;
        }
    }
    protected SectionType secType;

    public SectionType getSectionType() {
        return secType;
    }

    public void setSectionType(SectionType type) {
        this.secType = type;
    }

    public Section() {
        segmentList = new ArrayList<Segment>();
        setId(secCounter++);
    }

    public Section(Section source) {
        id = source.id;
        name = source.name;
        childrenLink = source.childrenLink;
        parentalLink = source.parentalLink;
        segmentList = source.segmentList;
    }

    /**
     * Constructor to create a line section with a given number of compartments, endradii, and endpoints.
     */
    public Section(float startRadius, Point3f startPoint, float endRadius, Point3f endPoint, int numSegments) {
        segmentList = new ArrayList<Segment>();
        setId(secCounter++);
        float oldRad = startRadius;
        Point3f oldEnd = new Point3f(startPoint);
        Point3f newEnd = new Point3f();
        for (int i = 0; i < numSegments; ++i) {
            float position = (i + 1.0f) / (float) numSegments;
            float newRad = (1 - position) * startRadius + position * endRadius;
            newEnd.x = (1 - position) * startPoint.x + position * endPoint.x;
            newEnd.y = (1 - position) * startPoint.y + position * endPoint.y;
            newEnd.z = (1 - position) * startPoint.z + position * endPoint.z;
            Segment segment = new Segment();
            segment.setSegment(oldEnd, newEnd, oldRad, newRad);
            segmentList.add(segment);
            oldEnd = new Point3f(newEnd);
            oldRad = newRad;
        }
    }

    public static int getSecCounter() {
        return Section.secCounter;
    }

    public static void resetSecCounter() {
        Section.secCounter = 0;
        logger.info("reset section counter:" + Section.secCounter);
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<Segment> getSegments() {
        return segmentList;
    }

    public SectionLink getParentalLink() {
        return parentalLink;
    }

    public SectionLink getChildrenLink() {
        return childrenLink;
    }

    public void setParentalLink(SectionLink parental) {
        parentalLink = parental;
    }

    public void setChildrenLink(SectionLink children) {
        childrenLink = children;
    }

    public int getId() {
        if (id < 0) {
            id = secCounter;
        }
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    /** 
     * Get polygonal composed length of this section.
     *
     * @return polygonal composed length of this section.
     */
    public float getLength() {
        float ret = 0.0f;
        for (int i = 0; i < segmentList.size(); ++i) {
            ret += segmentList.get(i).getLength();
        }
        return ret;
    }

    public Iterator getIterator() {
        return new Iterator(this);
    }

    /**
     * Get relative coordinate of the childs beginning
     * along the parent section. Implicitely the "beginning" is
     * the 0th end of the child
     * @param child
     * @return 0.0 ... 1.0, -1.0 if child is not a real child
     */
    public float getFractAlongParentForChild(Section child) {
        Set<Section> children;
        if (getChildrenLink() == null) {
            children = new HashSet<Section>();
            children.add(child);
        } else {
            children = getChildrenLink().getChildren();
        }
        for (Section ch : children) {
            if (ch == child) {
                Segment firstChildSegment = child.getSegments().get(0);
                Point3f sstart = firstChildSegment.getStart();
                List<Segment> parentSecSegments = getSegments();

                if (parentSecSegments.size() > 0) {
                    Point3f parentStart = parentSecSegments.get(0).getStart();
                    Point3f parentEnd = parentSecSegments.get(parentSecSegments.size() - 1).getEnd();

                    Vector3f tmp = new Vector3f();
                    tmp.sub(sstart, parentStart);
                    float diff = tmp.lengthSquared();

                    tmp.sub(sstart, parentEnd);
                    float diff2 = tmp.lengthSquared();

                    if (diff > diff2) {
                        return 1.0f;
                    } else {
                        return 0.0f;
                    }
                } else {
                    return -1.0f;
                }

            }
        }
        return -1.0f;
    }

    public void printSection() {
        for (Segment segment : segmentList) {
            logger.info(segment.toString());
        }
    }

    public class Iterator implements Serializable, java.util.Iterator<Section> {

        private static final long serialVersionUID = -2311930069184524614L;
        //private LinkedList<Pair<Section, SectionAdress>> processingQueue = new LinkedList<Pair<Section, SectionAdress>>();
        //private final List<Section> processing;
        private final Set<Section> processing;
        /*private SectionAdress sectionAdress = new SectionAdress("0", 0);
        private SectionAdress parentSectionAdress = new SectionAdress("0", 0);*/
        /** Root of the subtree to iterate over. */
        private Section top;
        /** Next section returned by next(). */
        private Section next;
        /** Flag is set if next lies lower. */
        private boolean down;
        /** Flag to show a subtree not completly iterated yet. */
        private boolean unret_st;

        public Iterator(Section section) {
            next = section;
            top = section;
            processing = new HashSet<Section>();
            processing.add(section);
            down = true;
        }

        @Override
        public boolean hasNext() {
            //logger.debug("hastNext processQueue.size: " + processingQueue.size());
            return !processing.isEmpty();
        }

        public Section get(int secId) {
            Section currSec = next();
            int currSecId = currSec.getId();
            while (currSecId != secId) {
                currSec = next();
                if (currSec == null) {
                    return null;
                } else {
                    currSecId = currSec.getId();
                }
            }
            return currSec;
        }

        @Override
        public Section next() {
            java.util.Iterator<Section> it = processing.iterator();
            Section sec = it.next();
            it.remove();
            if (sec.getChildrenLink() != null) {
                SectionLink sectionLink = sec.getChildrenLink();
                processing.addAll(sectionLink.getChildren());
            }
            return sec;
        }


        /*public SectionAdress getParentSectionAdress() {
        return parentSectionAdress;
        }

        public SectionAdress getSectionAdress() {
        return sectionAdress;
        }*/

        /*
        public Section getNext() {
        Section ret = next;
        if (ret == null) {
        return null;
        }

        if (ret == top && !down) {
        ret = null;
        }
        if (next != null) {
        Section n1 = next;
        if (down) {
        SectionLink childrenLink = next.getChildrenLink();
        boolean branches = false;
        if (childrenLink != null) {
        Section branch0 = childrenLink.getBranch0();
        Section branch1 = childrenLink.getBranch1();
        if (branch0 != null || branch1 != null) {
        if (branch0 != null) {
        next = branch0;
        branches = true;
        } else {
        next = branch1;
        branches = true;
        }
        }
        }

        if (!branches) {
        SectionLink parentalLink = next.getParentalLink();
        Section parentSec = null;
        if (parentalLink != null) {
        parentSec = parentalLink.getParental();
        if (parentSec != null) {
        SectionLink parentSecChildrenLink = parentSec.getChildrenLink();
        n1 = parentSecChildrenLink.getBranch1();
        } else {
        n1 = null;
        }
        } else {
        n1 = null;
        }

        if (next == top) {
        next = null;
        } else if (n1 != next) {
        if (n1 == null) {
        next = parentalLink.getParental();
        down = false;
        getNext();
        } else {
        next = n1;
        }
        } else {
        next = parentalLink.getParental();
        down = false;
        getNext();
        }
        }
        } else {
        if (next == top) {
        next = null;
        } else {
        SectionLink parentalLink = next.getParentalLink();
        Section branch1 = parentalLink.getParental().getChildrenLink().getBranch1();

        if (branch1 != null) {
        if (branch1 != next) {
        n1 = branch1;
        }
        }
        if (n1 == next) {
        next = parentalLink.getParental();
        getNext();
        } else {
        down = true;
        next = n1;
        }
        }
        }
        }
        return ret;
        }

         * 
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        /*public Section nextOld() {
        if (processingQueue.size() > 0) {
        Pair<Section, SectionAdress> pair = processingQueue.getLast();
        Section section = pair.first;
        SectionAdress sectionAdressLoc = pair.second;
        processingQueue.removeLast();
        if (section.getChildrenLink() != null) {
        logger.debug("section has children links: " + section.getSectionName());
        SectionLink sectionLink = (SectionLink) section.getChildrenLink();
        int numSecChildren = sectionLink.childrenSet.size();
        java.util.Iterator<Section> it = sectionLink.childrenSet.iterator();
        int counter = 0;
        while (it.hasNext()) {
        Section secChild = it.next();
        SectionAdress newSectionAdress;
        String branch = sectionAdressLoc.branch;
        int localID = sectionAdressLoc.localID;
        if (numSecChildren == 1) {
        localID++;
        } else {
        localID = 0;
        branch = branch + counter;
        }
        newSectionAdress = new SectionAdress(branch, localID, sectionAdressLoc);
        processingQueue.addFirst(new Pair<Section, SectionAdress>(secChild, newSectionAdress));
        logger.debug("put this section in the queue: " + secChild.getSectionName());
        counter++;
        }*/

        /*for (int i = 0; i < numSecChildren; i++) {
        Section secChild = sectionLink.childrenSet.get(i);
        SectionAdress newSectionAdress;
        String branch = sectionAdressLoc.branch;
        int localID = sectionAdressLoc.localID;
        if (numSecChildren == 1) {
        localID++;
        } else {
        localID = 0;
        branch = branch + i;
        }
        newSectionAdress = new SectionAdress(branch, localID, sectionAdressLoc);
        processingQueue.addFirst(new Pair<Section, SectionAdress>(secChild, newSectionAdress));
        logger.debug("put this section in the queue: " + secChild.getSectionName());
        }*/
        /*} else {
        logger.debug("section has no children links: " + section.getSectionName());
        }
        this.sectionAdress = sectionAdressLoc;
        this.parentSectionAdress = sectionAdressLoc.parentAdress;
        return section;
        }
        return null;
        }*/

        /*public class SectionAdress {

        // path of branchings
        public String branch = "0";
        // local id of a unbranched part
        public int localID = 0;
        public SectionAdress parentAdress = null;

        public SectionAdress(String branch, int localID) {
        super();
        this.branch = branch;
        this.localID = localID;
        }

        public SectionAdress(String branch, int localID,
        SectionAdress parentAdress) {
        super();
        this.branch = branch;
        this.localID = localID;
        this.parentAdress = parentAdress;
        }

        @Override
        public String toString() {
        return branch + "_" + localID;
        }
        }*/
    }
}
