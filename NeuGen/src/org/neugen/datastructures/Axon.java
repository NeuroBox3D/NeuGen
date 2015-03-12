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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.utils.Vrand;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.SubCommonTreeParam;

/**
 * @author Jens Eberhard 
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class Axon extends NeuronalTreeStructure<AxonSection> implements Serializable {

    private static final long serialVersionUID = -4611676473283033478L;
    /** use to log messages */
    private static final Logger logger = Logger.getLogger(Axon.class.getName());
    /** random number generator */
    private static transient Vrand drawNumber;

    private float branchStart;

    public void setBranchStart(float branchStart) {
        this.branchStart = branchStart;
    }



    public Axon() {
        super();
    }

    public static void setDrawNumber(Vrand drawNumber) {
        Axon.drawNumber = drawNumber;
    }

    @SuppressWarnings("unchecked")
    public void set(Point3f startPoint, Point3f endPoint, AxonParam param) {
        if(drawNumber == null) {
            logger.info("set axon draw number!");
            drawNumber = new Vrand(param.getSeedValue());
        }

        logger.info("axon seed value: " + drawNumber.randx);
        float lastRad = 0.0f;
        Point3f lastPoint;
        Vector3f direction = new Vector3f();
        direction.sub(endPoint, startPoint);
        float sectionLength = direction.length();
        direction.normalize();
        // creates first section of axon
        par = param;
        {
            Point3f start = new Point3f(startPoint);
            Point3f end = new Point3f();
            end.scaleAdd(param.getHillock().getLength(), direction, start);
            float startRadius = param.getHillock().getProximalRadius();
            float endRadius = param.getRad().getMax();
            lastRad = endRadius;
            lastPoint = new Point3f(end);
            firstSection = new AxonSection(startRadius, start, endRadius, end, 1);
            firstSection.setName("axon_hillock" + firstSection.getId());
        }

        // creates initial segment of axon
        Point3f start = new Point3f(lastPoint);
        Point3f end = new Point3f();
        end.scaleAdd(param.getInitialSegment().getLength(), direction, start);
        AxonSection initialSegment = new AxonSection(lastRad, start, lastRad, end, 1);
        initialSegment.setName("axon_initial" + initialSegment.getId());

        SectionLink link1 = new SectionLink(firstSection, initialSegment, null);
        {
            SubCommonTreeParam genParam = param.getFirstGen();
            //Length of a segment
            float density = genParam.getNpartsDensity();
            float segmentLength = (float) (1.0f / density);
            if (segmentLength > sectionLength) {
                segmentLength = sectionLength;
            }
            int numberParts = (int) Math.ceil(sectionLength / segmentLength);
            int numberBranch = (int) Math.round(genParam.getNbranchParam() * (drawNumber.fdraw() + 0.5));
            // Branching only at after 1/3 of axon length

            if(branchStart == 0) {
                branchStart = 3;
            }

            logger.info("branch start: " + branchStart );

            int minNumParts = (int) ((numberBranch + 1) * branchStart / (branchStart-1));
            if (numberParts < minNumParts) {
                segmentLength = (segmentLength * numberParts) / minNumParts;
                numberParts = minNumParts;
            }

            // Find branch points
            Map<Integer, Pair<SectionLink, float[]>> branchPoints = new TreeMap<Integer, Pair<SectionLink, float[]>>();
            Pair<SectionLink, float[]> br = new Pair<SectionLink, float[]>();
            // Dummy branch points
            br.first = new SectionLink(initialSegment, null, null);
            branchPoints.put(0, br);
            branchPoints.put(numberParts, new Pair<SectionLink, float[]>());
            while (numberBranch + 2 > branchPoints.size()) {
                int branchPoint = (int) (numberParts / branchStart - 1 + drawNumber.fdraw() * (numberParts * (branchStart-1) / branchStart + 1));
                //logger.info("branchPoint: " + branchPoint);
                branchPoints.put(branchPoint, new Pair<SectionLink, float[]>());
            }

            //logger.debug("branchPoints Size: " + branchPoints.size());
            {
                float oldrad = param.getRad().getMax();
                assert (oldrad > 0.0);
                float endrad = param.getRad().getMin();
                assert (endrad > 0.0);
                int nsegs = numberParts;
                Iterator it = branchPoints.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry branchEntry = (Map.Entry) it.next();
                    float[] radii = Segment.interpolateRadii(oldrad, endrad, nsegs, (Integer) branchEntry.getKey(), param.getA(), param.getC());
                    if (radii[1] < endrad) {
                        radii[1] = endrad;
                    }
                    if (radii[2] < endrad) {
                        radii[2] = endrad;
                    }
                    assert (radii[0] > 0.0f);
                    assert (radii[1] > 0.0f);
                    assert (radii[2] > 0.0f);
                    int branchKey = (Integer) branchEntry.getKey();
                    nsegs = numberParts - branchKey - 1;
                    oldrad = radii[1];
                    branchPoints.get(branchKey).second = radii;
                }
            }

            {
                Iterator<Map.Entry<Integer, Pair<SectionLink, float[]>>> iter1 = branchPoints.entrySet().iterator();
                if (iter1.hasNext()) {
                    //logger.info("draw: " + drawNumber.draw());
                    Iterator<Map.Entry<Integer, Pair<SectionLink, float[]>>> iter2 = branchPoints.entrySet().iterator();
                    iter2.next();

                    Map.Entry<Integer, Pair<SectionLink, float[]>> branchEntry1 = iter1.next();
                    Map.Entry<Integer, Pair<SectionLink, float[]>> branchEntry2;
                    SectionLink nextLink = branchEntry1.getValue().first;

                    float myLength = param.getMyelin().getMyelinatedLegth();
                    float ranLength = param.getMyelin().getRanvierLegth();
                    int my = (int) Math.ceil(myLength / segmentLength);
                    int ran = (int) Math.ceil(ranLength / segmentLength);
                    int strip = my + ran;

                    int iter2_Key = 0;
                    while (iter2.hasNext()) {
                        branchEntry2 = iter2.next();
                        int local_nparts = branchEntry2.getKey() - branchEntry1.getKey();
                        assert (local_nparts > 0.0);
                        int ns;
                        if(strip != 0) {
                            ns = (local_nparts - ran) / strip; //last part is a ranvier node
                        } else {
                            ns = 0;
                        }
                        
                        if (ns < 0) {
                            ns = 0;
                        }
                        int rem = (local_nparts - ran) - ns * strip;
                        int nran = ns + 1 + (rem > 0 ? 1 : 0);
                        if (rem < 0.0) {
                            rem = local_nparts;
                        }
                        int nmy = ns + (rem > ran ? 1 : 0);
                        int remmy = (rem > ran ? rem - ran : 0);
                        int remran = (rem > ran ? ran : rem);

                        float startRad = branchEntry1.getValue().second[1];
                        assert (startRad > 0.0);

                        float endRad = branchEntry2.getValue().second[0];
                        assert (endRad > 0.0);

                        float radInc = (endRad - startRad) / local_nparts;
                        float segRadStart = startRad, segRadEnd;

                        while (nmy > 0 || nran > 0) {
                            Point3f segstart;
                            // Create ranvier node
                            if (ranLength > 0) {
//                              logger.info("link: " + nextLink.secLinkName);
                                AxonSection ranvier = new AxonSection();
                                ranvier.setName("axon" + ranvier.getId());
                                ranvier.secType = Section.SectionType.NOT_MYELINIZED;
                                List<Segment> segments = ranvier.getSegments();
                                List<Segment> parSegments = nextLink.getParental().getSegments();
                                segstart = parSegments.get(parSegments.size() - 1).getEnd();
                                int limit = ran;
                                if (nran == 1 && remran > 0.0) {
                                    limit = remran;
                                }
                                for (int i = 0; i < limit; ++i) {
                                    float randomNumber = drawNumber.fdraw();
                                    Vector3f randomRotVal = drawNumber.getRandomRotVector(randomNumber * (float) Math.PI / 4, direction);
                                    Point3f segend = new Point3f();
                                    segend.scaleAdd(segmentLength, randomRotVal, segstart);
                                    assert (segRadStart > -radInc);
                                    segRadEnd = segRadStart + radInc;
                                    assert (segRadEnd > 0.0);
                                    Segment segment = new Segment();
                                    segment.setSegment(segstart, segend, segRadStart, segRadEnd);
                                    segments.add(segment);
                                    segstart = new Point3f(segend);
                                    segRadStart = segRadEnd;
                                }
                                //logger.info("nextLink.getParental: " + nextLink.getParental().getSectionName());
                                nextLink.set(nextLink.getParental(), ranvier, nextLink.getBranch1());
                                nextLink = new SectionLink(ranvier, null, null);
                                nran--;
                                //logger.info("NeuGenLib.axonSecCounter (ranvier) is: " + NeuGenLib.axonSecCounter);
                            } else {
                                nran = 0;
                            }
                            // Create myelinated segment if counter not 0
                            if (nmy > 0 && myLength > 0) {
                                AxonSection myel = new AxonSection();
                                myel.setName("axon_myel" + myel.getId());
                                myel.secType = Section.SectionType.MYELINIZED;
                                List<Segment> segments = myel.getSegments();
                                List<Segment> parSegments = nextLink.getParental().getSegments();
                                segstart = parSegments.get(parSegments.size() - 1).getEnd();
                                int limit = my;
                                if (nmy == 1 && remmy > 0.0) {
                                    limit = remmy;
                                }
                                for (int i = 0; i < limit; ++i) {
                                    Vector3f randomRotVal = drawNumber.getRandomRotVector(drawNumber.fdraw() * (float) Math.PI / 4, direction);
                                    Point3f segend = new Point3f();
                                    segend.scaleAdd(segmentLength, randomRotVal, segstart);
                                    //assert (segRadStart > -radInc);
                                    segRadEnd = segRadStart + radInc;
                                    assert (segRadEnd > 0.0);
                                    Segment segment = new Segment();
                                    segment.setSegment(segstart, segend, segRadStart, segRadEnd);
                                    segments.add(segment);
                                    segstart = new Point3f(segend);
                                    segRadStart = segRadEnd;
                                }
                                nextLink.set(nextLink.getParental(), myel, nextLink.getBranch1());
                                nextLink = new SectionLink(myel, null, null);
                                nmy--;
                                //logger.info("NeuGenLib.axonSecCounter (mye) is: " + NeuGenLib.axonSecCounter);
                            } else {
                                nmy = 0;
                            }
                        }
                        //logger.info("while inner end ");
                        iter2_Key = branchEntry2.getKey();
                        branchPoints.get(iter2_Key).first = nextLink;
                        branchEntry1 = iter1.next();
                        //branchEntry2 = (Map.Entry) iter2.next();
                    }
                    //logger.info("while outer end");
                }
            }

            {
                // Create branch strings
                SubCommonTreeParam prevGeneration = genParam;
                genParam = genParam.getSiblings();
                assert (genParam != null);
                segmentLength = 1.0f / genParam.getNpartsDensity();
                Iterator<Map.Entry<Integer, Pair<SectionLink, float[]>>> iter1 = branchPoints.entrySet().iterator();
                if (iter1.hasNext()) {
                    // First branch point is not a fork
                    iter1.next();
                    while (iter1.hasNext()) {
                        Map.Entry<Integer, Pair<SectionLink, float[]>> branchEntry = iter1.next();
                        // Last branch is a dummy.
                        if (iter1.hasNext()) {
                            Vector3f vec = new Vector3f(genParam.getLenParam().getX(), genParam.getLenParam().getY(), genParam.getLenParam().getZ());
                            float vecLen = vec.length();
                            sectionLength = vecLen * (drawNumber.fdraw() + 0.5f);
                            if (segmentLength > sectionLength) {
                                segmentLength = sectionLength;
                            }

                            float myLength = param.getMyelin().getMyelinatedLegth();
                            float ranLength = param.getMyelin().getRanvierLegth();
                            int my = (int) Math.ceil(myLength / segmentLength);
                            int ran = (int) Math.ceil(ranLength / segmentLength);
                            int strip = my + ran;
                            SectionLink nextLink = branchEntry.getValue().first;
                            List<Segment> nextLinkSegments = nextLink.getParental().getSegments();
                            direction = nextLinkSegments.get(nextLinkSegments.size() - 1).getDirection();
                            float randAngle = prevGeneration.getBranchAngle().getMin()
                                    + (prevGeneration.getBranchAngle().getMax() - prevGeneration.getBranchAngle().getMin()) * drawNumber.fdraw();

                            direction = drawNumber.getRandomRotVector(randAngle, direction);
                            float endRad = param.getRad().getMin();
                            float startRad = branchEntry.getValue().second[2];
                            if (startRad < endRad) {
                                startRad = endRad;
                            }
                            int local_nparts = (int) (Math.ceil(sectionLength / segmentLength));
                            assert (local_nparts > 0.0);
                            int ns;
                            if(strip != 0) {
                                ns = (local_nparts - ran) / strip; //last part is a ranvier node
                            } else {
                                ns = 0;
                            }
                            
                            if (ns < 0) {
                                ns = 0;
                            }
                            int rem = (local_nparts - ran) - ns * strip;
                            int nmy = ns + (rem > ran ? 1 : 0);
                            int remmy = (rem > ran ? rem - ran : 0);
                            int nran = ns + 1 + (rem > 0 ? 1 : 0);
                            int remran = (rem > ran ? ran : rem);
                            float radInc = (endRad - startRad) / local_nparts;
                            float segRadStart = startRad, segRadEnd;

                            while (nmy > 0 || nran > 0) {
                                Point3f segstart;
                                // Create ranvier node
                                if (ranLength > 0) {
                                    AxonSection ranvier = new AxonSection();
                                    ranvier.setName("axon" + ranvier.getId());
                                    ranvier.secType = Section.SectionType.NOT_MYELINIZED;
                                    List<Segment> segments = ranvier.getSegments();
                                    segstart = nextLink.getParental().getSegments().get(nextLink.getParental().getSegments().size() - 1).getEnd();
                                    int limit = ran;
                                    if (nran == 1 && remran > 0.0) {
                                        limit = remran;
                                    }
                                    //logger.info("limit: " + limit);
                                    for (int i = 0; i < limit; ++i) {
                                        float randomNumber = drawNumber.fdraw();
                                        Vector3f randomRotVal = drawNumber.getRandomRotVector(randomNumber * (float) Math.PI / 4, direction);
                                        Point3f segend = new Point3f();
                                        segend.scaleAdd(segmentLength, randomRotVal, segstart);
                                        //assert (segRadStart > -radInc);
                                        segRadEnd = segRadStart + radInc;
                                        assert (segRadEnd > 0.0);
                                        Segment segment = new Segment();
                                        segment.setSegment(segstart, segend, segRadStart, segRadEnd);
                                        segments.add(segment);
                                        segstart = new Point3f(segend);
                                        segRadStart = segRadEnd;
                                    }
                                    if (nextLink.getBranch0() != null) {
                                        nextLink.set(nextLink.getParental(), nextLink.getBranch0(), ranvier);
                                    } else {
                                        nextLink.set(nextLink.getParental(), ranvier, nextLink.getBranch1());
                                    }
                                    nextLink = new SectionLink(ranvier, null, null);
                                    nran--;
                                } else {
                                    nran = 0;
                                }
                                // Create myelinated segment if counter not 0
                                if (nmy > 0 && myLength > 0) {
                                    AxonSection myel = new AxonSection();
                                    myel.setName("axon_myel" + myel.getId());
                                    myel.secType = Section.SectionType.MYELINIZED;
                                    List<Segment> segments = myel.getSegments();
                                    //segstart = Arrays.copyOf(nextLink.getParental().getSegments().get(nextLink.getParental().getSegments().size() - 1).getEnd(), d);
                                    segstart = nextLink.getParental().getSegments().get(nextLink.getParental().getSegments().size() - 1).getEnd();
                                    int limit = my;
                                    if (nmy == 1 && remmy > 0.0) {
                                        limit = remmy;
                                    }
                                    //logger.info("nmy > 0 limit: " + limit);
                                    for (int i = 0; i < limit; ++i) {
                                        Vector3f randomRotVal = drawNumber.getRandomRotVector(drawNumber.fdraw() * (float) Math.PI / 4, direction);
                                        Point3f segend = new Point3f();
                                        segend.scaleAdd(segmentLength, randomRotVal, segstart);
                                        //assert (segRadStart > -radInc);
                                        segRadEnd = segRadStart + radInc;
                                        assert (segRadEnd > 0.0);
                                        Segment segment = new Segment();
                                        segment.setSegment(segstart, segend, segRadStart, segRadEnd);
                                        segments.add(segment);
                                        segstart = new Point3f(segend);
                                        segRadStart = segRadEnd;
                                    }
                                    //logger.info("myelinated: " + nextLink.getParental().getSectionName());
                                    nextLink.set(nextLink.getParental(), myel, nextLink.getBranch1());
                                    nextLink = new SectionLink(myel, null, null);
                                    //logger.info("NeuGenLib.axonSecCounter (myelined siblings) is: " + NeuGenLib.axonSecCounter);
                                    nmy--;
                                } else {
                                    nmy = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
        link1.updatePolySomaDist();
        logger.debug("axon set end");
    }
}
