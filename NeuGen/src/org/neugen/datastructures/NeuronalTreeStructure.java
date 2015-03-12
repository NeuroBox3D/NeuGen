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
import java.util.List;
import java.util.Map;
import javax.vecmath.Point3f;
import org.apache.log4j.Logger;
import org.neugen.datastructures.parameter.CommonTreeParam;

/**
 * Base class for tree structures like dendrites and axons.
 * @author Alex Wanner
 * @author Sergei Wolf
 */
public class NeuronalTreeStructure<SectionType extends Section> implements Serializable {

    private static final long serialVersionUID = -3622226473283033478L;
    /** Use to log messages. */
    private static final Logger logger = Logger.getLogger(NeuronalTreeStructure.class.getName());
    protected SectionType firstSection;
    /** Pointer to the common tree parameter.*/
    protected transient CommonTreeParam par;

    public SectionType getFirstSection() {
        return firstSection;
    }

    public void setFirstSection(SectionType section) {
        this.firstSection = section;
    }

    public int getNumOfSegments() {
        int numberOfSegments = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section section = secit.next();
                numberOfSegments += section.getSegments().size();
            }
        }
        return numberOfSegments;
    }

    public int getNumOfSections() {
        int numberOfSections = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                secit.next();
                numberOfSections++;
            }
        }
        return numberOfSections;
    }

    /** 
     * Get total length of neuronal tree structure.
     * 
     * @return the length of neuronal tree structure.
     */
    public float getTotalLength() {
        //logger.info("getLength begin");
        float length = 0.0f;
        int numSec = 0;
        int numSeg = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section sec = secit.next();
                numSec++;
                //logger.info("sec name: " + sec.getSectionName());
                for (Segment segment : sec.getSegments()) {
                    numSeg++;
                    length += segment.getLength();
                }
            }
        }
        /*
        logger.info("numSec: " + numSec);
        logger.info("numSeg: " + numSeg);
        logger.info("getLength end");
         */
        return length;
    }

    /**
     * Get total length of neuronal tree structure.
     *
     * @return the length of neuronal tree structure.
     */
    public Point3f getRadius() {
        //logger.info("getLength begin");
        float radius = 0.0f;
        float averageRadius = 0.0f;
        //float minRadius = 0.15f;
        float minRadius = 10.0f;
        float maxRadius = 0.0f;

        int numSec = 0;
        int numSeg = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section sec = secit.next();
                numSec++;
                //logger.info("sec name: " + sec.getSectionName());
                for (Segment segment : sec.getSegments()) {
                    numSeg++;
                    float sRad = segment.getStartRadius();
                    float eRad = segment.getEndRadius();
                    //length += segment.getLength();
                    radius = sRad;
                    radius+= eRad;
                    radius /=2.0f;
                    averageRadius += radius;

                    if(maxRadius < sRad) {
                        maxRadius = sRad;
                    }

                    if(maxRadius < eRad) {
                        maxRadius = eRad;
                    }

                    
         
                    if(minRadius > sRad && sRad > 0.01f) {
                        minRadius = sRad;
                    }

                    if(minRadius > eRad && eRad > 0.01f) {
                        minRadius = eRad;
                    }

                }
            }
        }

        if(averageRadius != 0.0f) {
            averageRadius /= (float)numSeg;
        }

        if(averageRadius == 0.0f && maxRadius == 0.0f) {
            minRadius = 0.0f;
        }
        /*
        logger.info("numSec: " + numSec);
        logger.info("numSeg: " + numSeg);
        logger.info("getLength end");
         */
        Point3f ret = new Point3f(maxRadius, minRadius, averageRadius);
        return ret;
    }

    public float getNTSVolume() {
        float volume = 0.0f;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section curSec = secit.next();
                for (Segment curSeg : curSec.getSegments()) {
                    volume += curSeg.getVolume();
                }
            }
        }
        return volume;
    }

    public void getNTSSize(
            Map<Integer, Pair<Float, Float>> xData,
            Map<Integer, Pair<Float, Float>> yData,
            Map<Integer, Pair<Float, Float>> zData
            ) {
        if (firstSection != null) {
            Section.Iterator secIter = firstSection.getIterator();
            while (secIter.hasNext()) {
                Section curSec = secIter.next();
                for (Segment curSeg : curSec.getSegments()) {
                    List<Point3f> points = new ArrayList<Point3f>();
                    points.add(curSeg.getStart());
                    points.add(curSeg.getEnd());
                    points.add(curSeg.getCenter());
                    for (Point3f currentSegmentData : points) {
                        // berechne die Breite. die maximale z-Strecke.
                        {
                            int xCoord = (int) currentSegmentData.x;
                            float zCoord = currentSegmentData.z;
                            if (xData.containsKey(xCoord)) {
                                Pair<Float, Float> minMaxZCoord = xData.get(xCoord);
                                float minZ = minMaxZCoord.first;
                                float maxZ = minMaxZCoord.second;

                                if (zCoord < minZ) {
                                    minZ = zCoord;
                                    minMaxZCoord.first = minZ;
                                    //logger.info("new min z");
                                } else if (zCoord > maxZ) {
                                    maxZ = zCoord;
                                    minMaxZCoord.second = maxZ;
                                    //logger.info("new max z");
                                }
                            } else {
                                xData.put(xCoord, new Pair<Float, Float>(zCoord, zCoord));
                            }
                        }

                        // berechen die Laenge. die maximale x-Strecke
                        {
                            int yCoord = (int) currentSegmentData.y;
                            float xCoord = currentSegmentData.x;

                            /*
                            for(int i = -10; i <= 10; i++) {
                            int locCoord = yCoord+i;
                            if(yData.contains(locCoord)) {
                            logger.info("y umgengscoordinaten in der tabelle");
                            }
                            }
                             *
                             */
                            if (yData.containsKey(yCoord)) {
                                Pair<Float, Float> minMaxXCoord = yData.get(yCoord);
                                float minX = minMaxXCoord.first;
                                float maxX = minMaxXCoord.second;
                                if (xCoord < minX) {
                                    minX = xCoord;
                                    minMaxXCoord.first = minX;
                                    //logger.info("new min x");
                                } else if (xCoord > maxX) {
                                    maxX = xCoord;
                                    minMaxXCoord.second = maxX;
                                    //logger.info("new max x");
                                }
                                //yData.put(yCoord, minMaxXCoord);
                                //logger.info("y ist in der tabelle");
                            } else {
                                yData.put(yCoord, new Pair<Float, Float>(xCoord, xCoord));
                            }
                        }

                        // berechen die Hoehe. die maximale y-Strecke
                        {
                            int zCoord = (int) currentSegmentData.z;
                            float yCoord = currentSegmentData.y;
                            /*
                            for(int i = -10; i <= 10; i++) {
                            int locCoord = yCoord+i;
                            if(yData.contains(locCoord)) {
                            logger.info("y umgengscoordinaten in der tabelle");
                            }
                            }
                             *
                             */
                            if (zData.containsKey(zCoord)) {
                                Pair<Float, Float> minMaxXCoord = zData.get(zCoord);
                                float minY = minMaxXCoord.first;
                                float maxY = minMaxXCoord.second;
                                if (yCoord < minY) {
                                    minY = yCoord;
                                    minMaxXCoord.first = minY;
                                    //logger.info("new min x");
                                } else if (yCoord > maxY) {
                                    maxY = yCoord;
                                    minMaxXCoord.second = maxY;
                                    //logger.info("new max x");
                                }
                                //yData.put(yCoord, minMaxXCoord);
                                //logger.info("y ist in der tabelle");
                            } else {
                                zData.put(zCoord, new Pair<Float, Float>(yCoord, yCoord));
                            }
                        }
                    }
                }
            }
        }
    }

    /** 
     * Get surface area of neuronal tree structure.
     *
     * @return The surface are of neuronal tree structure.
     */
    public float getSurfaceArea() {
        float area = 0.0f;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section sec = secit.next();
                for (Segment segment : sec.getSegments()) {
                    area += segment.getSurfaceArea();
                }
            }
        }
        return area;
    }

    /** 
     * Get number of compartments of neuronal tree structure.
     *
     * @return The number of compartments neuronal tree structure.
     */
    public int getNParts() {
        int nsegs = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section sec = secit.next();
                nsegs += sec.getSegments().size();
            }
        }
        return nsegs;
    }

    public int getNBranch() {
        int ret = 0;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            while (secit.hasNext()) {
                Section sec = secit.next();
                if (sec.getChildrenLink() != null) {
                    if (sec.getChildrenLink().getBranch0() != null && sec.getChildrenLink().getBranch1() != null) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Get the relevant data of the dendrite section where the compartment
     * with index cd_idx lies within the given section.
     * @param sec_id The section Id.
     * @param cd_idx The index of the segment.
     * @return The relative position of the segment within the section sec_id.
     */
    public float getDendriteSectionData(int sec_id, int cd_idx) {
        float dd = 0.0f;
        float len = 0.0f;
        float ret = 0.0f;
        if (firstSection != null) {
            Section.Iterator secit = firstSection.getIterator();
            Section sec = secit.get(sec_id);
            //logger.info("section with sec_id found: " + sec.getName());
            for (Segment segment : sec.getSegments()) {
                len += segment.getLength();
            }
            for (int i = 0; !(i > cd_idx); ++i) {
                dd += sec.getSegments().get(i).getLength();
            }
            ret = dd / len;
        }
        return ret;
    }

    /**
     * Get the relevant data of the dendrite section where the compartment
     * with index cd_idx lies within the given section.
     * @param sec_id The section Id.
     * @param cd_idx The index of the segment.
     * @return The relative position of the segment within the section sec_id.
     */
    public static float getDendriteSectionData(Section sec, int cd_idx) {
        float dd = 0.0f;
        float len = 0.0f;
        //logger.info("section with sec_id found: " + sec.getName());
        for (Segment segment : sec.getSegments()) {
            len += segment.getLength();
        }
        for (int i = 0; !(i > cd_idx); ++i) {
            dd += sec.getSegments().get(i).getLength();
        }
        float ret = dd / len;
        return ret;
    }
}
