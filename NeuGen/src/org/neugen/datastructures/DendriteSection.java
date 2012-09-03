/*
 * File: DendriteSection.java
 * Created on 20.10.2009, 13:44:25
 *
 */
package org.neugen.datastructures;

import org.neugen.datastructures.parameter.ConstrParameter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Alexander Wanner
 */
public final class DendriteSection extends Section implements Serializable {

    private static final long serialVersionUID = -4611546473283033478L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(DendriteSection.class.getName());

    /** Construct a default section. */
    public DendriteSection() {
        super();
    }

    /** Copy a section. */
    public DendriteSection(Section source) {
        super(source);
    }
    private static float privAngleY = 0.0f;



    /*
    public DendriteSection(ConstrParameter cp) {
    logger.setLevel(Level.OFF);
    //set the section type
    secType = cp.sectionTye;
    childrenLink = null;
    parentalLink = null;

    //start point of the section
    Point3f start = new Point3f(cp.sectionStartPoint);
    //end point of the seciton
    Point3f end = new Point3f(cp.sectionEndPoint);

    //start radius
    float startRadius = cp.startRadius;

    // This section is firtst of a branch?
    if (cp.branchDistribution == null) {
    cp.branchDistribution = new ArrayList<Integer>();
    // First section of the whole dendrite?
    if (cp.parentSection == null) {
    float xLength = cp.generationParam.getLenParam().getX();
    float yLength = cp.generationParam.getLenParam().getY();
    float zLength = cp.generationParam.getLenParam().getZ();

    Vector3f vec = new Vector3f(xLength, yLength, zLength);

    logger.info("the vector of first section generation: " + vec.toString());
    }
    }
    }
     *
     */
    /**
     * Constructor. It creates CA1 dendrite sections.
     * @param cp
     * @param respectColumn
     * @param scale
     * @param automaticShortening
     * @param test
     */
    public DendriteSection(ConstrParameter cp, boolean respectColumn, float scale, boolean automaticShortening, boolean down, float lowBranchingLimit) {
        setName("dendrite" + getId());
        logger.setLevel(Level.INFO);
        secType = cp.sectionTye;
        childrenLink = null;
        parentalLink = null;
        Point3f end = new Point3f(cp.sectionEndPoint);
        Point3f start = new Point3f(cp.sectionStartPoint);
        float startRadius = cp.startRadius;
        // This section is firtst of a branch?
        if (cp.branchDistribution == null) {
            //logger.info("first section of a branch");
            cp.branchDistribution = new ArrayList<Integer>();
            // First section of the whole dendrite?
            if (cp.parentSection == null) {
                Vector3f vec = new Vector3f(cp.generationParam.getLenParam().getX(), cp.generationParam.getLenParam().getY(), cp.generationParam.getLenParam().getZ());
                float vecLen = vec.length();
                float rand1 = vecLen * (cp.drawNumber.fdraw() + 0.5f);
                //Vector3f randomRot = cp.drawNumber.getRandomRotVector();
                //Vector3f randomRot = cp.drawNumber.getRandomRotVector((float) Math.toRadians(90));

                float minAngle = cp.generationParam.getBranchAngle().getMin();
                //logger.info("min angle: " + Math.toDegrees(minAngle));
                float maxAngle = cp.generationParam.getBranchAngle().getMax();
                //logger.info("max angle: " + Math.toDegrees(maxAngle));
                //float randAngleY = minAngle + (maxAngle - minAngle) * cp.drawNumber.fpm_onedraw();

                //float randAngleY = (maxAngle * cp.drawNumber.fpm_onedraw() + maxAngle * cp.drawNumber.fpm_onedraw()) / 2.0f;
                float randAngleY = maxAngle * cp.drawNumber.fpm_onedraw();
                //logger.info("random angle y befor: " + Math.toDegrees(randAngleY));

                if (Math.abs(randAngleY) < maxAngle / 2.0f) {
                    if (randAngleY < 0) {
                        randAngleY -= maxAngle / 3.0f;
                    } else {
                        randAngleY += maxAngle / 3.0f;
                    }
                }


                if ((randAngleY < 0 && privAngleY < 0) || (randAngleY > 0 && privAngleY > 0)) {
                    randAngleY *= -1.0f;
                }
                privAngleY = randAngleY;
                //logger.info("random angle y after: " + Math.toDegrees(randAngleY));

                Vector3f randomRot = new Vector3f(0, 0, 1);

                if (down) {
                    randomRot.z = -1;
                }

                if (cp.horizontalDir) {
                    logger.info("horizontal is true!!!!!!!!!!!!!!!!");
                    //Matrix3f rotMatrixLoc = new Matrix3f();
                    if (down) {
                        randomRot = new Vector3f(1, 0, 0);
                        //rotMatrixLoc.rotY(90);
                        //rotMatrixLoc.transform(randomRot);
                    } else {
                        randomRot = new Vector3f(-1, 0, 0);
                        //rotMatrixLoc.rotY(-90);
                        //rotMatrixLoc.transform(randomRot);
                    }
                }

                Matrix3f rotMatrix = new Matrix3f();
                rotMatrix.rotY(randAngleY); // +-50
                rotMatrix.transform(randomRot);

                float randAngleX = minAngle * cp.drawNumber.fpm_onedraw();
                //logger.info("rand angle x: " + Math.toDegrees(randAngleX));
                rotMatrix.rotX(randAngleX);
                rotMatrix.transform(randomRot);

                float randAngleZ = (float) (Math.toRadians(30) * cp.drawNumber.fpm_onedraw());
                //logger.info("random z angle: " + randAngleZ);

                //rotMatrix.rotZ(randAngleZ);
                //rotMatrix.transform(randomRot);

                //logger.info("rot vektor: " + randomRot.toString());
                cp.sectionEndPoint.scaleAdd(rand1, randomRot, start);
            } else {
                //logger.info("parent section is not null!!");
                List<Segment> parentSegments = cp.parentSection.getSegments();
                Point3f parentStart = parentSegments.get(0).getStart();
                Point3f parentEnd = parentSegments.get(parentSegments.size() - 1).getEnd();
                Vector3f direction = new Vector3f();
                direction.sub(parentEnd, parentStart);
                float minAngle = cp.generationParam.getBranchAngle().getMin();
                //logger.info("min angle: " + Math.toDegrees(minAngle));
                float maxAngle = cp.generationParam.getBranchAngle().getMax();
                //logger.info("max angle: " + Math.toDegrees(maxAngle));
                float randAngle = minAngle + (maxAngle - minAngle) * cp.drawNumber.fdraw();
                //logger.info("random angle: " + Math.toDegrees(randAngle));
                Vector3f randomRot = cp.drawNumber.getRandomRotVector(randAngle, direction);


                Vector3f vec = new Vector3f(cp.generationParam.getLenParam().getX(), cp.generationParam.getLenParam().getY(), cp.generationParam.getLenParam().getZ());
                float vecLen = vec.length();

                /*
                Vector3f randomRot = new Vector3f(0, 0, 1);
                if (down) {
                    randomRot.z = -1;
                }

                Matrix3f rotMatrix = new Matrix3f();
                rotMatrix.rotY(randAngle); // +-50
                rotMatrix.transform(randomRot);

                float randAngleX = minAngle * cp.drawNumber.fpm_onedraw();
                //logger.info("rand angle x: " + Math.toDegrees(randAngleX));
                rotMatrix.rotX(randAngleX);
                rotMatrix.transform(randomRot);

                float phi = 2.0f * (float) Math.PI * cp.drawNumber.fdraw();
                rotMatrix.rotZ(phi);
                rotMatrix.transform(randomRot);

                 *
                 */
                cp.sectionEndPoint.scaleAdd(vecLen, randomRot, start);
            }
            // scaliere um den Wert scale!         
            Vector3f direction = new Vector3f();
            direction.sub(cp.sectionEndPoint, start);
            cp.sectionEndPoint.scaleAdd(scale, direction, start);

            if (cp.sectionEndPoint.z > cp.maxZ) {
                Vector3f diff = new Vector3f();
                diff.sub(cp.sectionEndPoint, start);
                diff.z = cp.maxZ - start.z;
                if (diff.x == 0) {
                    diff.x += 0.1f * cp.drawNumber.pm_onedraw();
                }
                if (diff.y == 0) {
                    diff.y += 0.1f * cp.drawNumber.pm_onedraw();
                }
                float len = diff.length();
                len += 1;
                float cor = (float) Math.sqrt((len + diff.z) * (len - diff.z) / (diff.x * diff.x + diff.y * diff.y));
                diff.x *= cor;
                diff.y *= cor;
                cp.sectionEndPoint.add(diff, start);
            }
            testRegionLimits(cp, respectColumn);
            if (cp.parentSection == null) {
                cp.sectionStartPoint = Cellipsoid.correctStart(cp.soma, cp.sectionStartPoint, cp.sectionEndPoint);
            }
            //generateBranchDistribution(cp, start);
            generateBranchLimitDistribution(cp, start, lowBranchingLimit);
        }

        int branchVal = cp.branchDistribution.get(cp.branchLevel);
        int numParts = cp.genNumberSegments - branchVal;
        numParts = numParts - 1;
        float endRadius = cp.endRadius;
        end = new Point3f(cp.sectionEndPoint);

        Vector3f inc = new Vector3f();
        inc.sub(end, start);
        inc.scale(1.0f / numParts);

        float numParts2 = numParts;
        if (cp.branchDistribution.size() - 1 != cp.branchLevel) {
            numParts -= cp.genNumberSegments - cp.branchDistribution.get(cp.branchLevel + 1) - 1;
            end.sub(start);
            end.scale(((float) numParts) / numParts2);
            end.add(start);
        }
        if (numParts < 1) {
            //nparts = 1;
            for (int i = 0; i < cp.branchDistribution.size(); i++) {
                logger.info("branch distribution[" + i + "]=" + cp.branchDistribution.get(i));
            }
            logger.info("nparts < 1: " + numParts);
            logger.info("<1 segments in a section! " + numParts2);
            logger.info("branch_level=" + cp.branchLevel + ' ' + "nex_branch by segment: " + cp.branchDistribution.get(cp.branchLevel + 1)
                    + ' ' + "this branch by segment: " + cp.branchDistribution.get(cp.branchLevel) + ' ' + "gen_nsegs: " + cp.genNumberSegments);
        }

        if (numParts > 1000) {
            logger.error("number of parts is > 1000: " + numParts);
            //System.exit(0);
            numParts = 1000;
        }

        float newRadius = Segment.interpolateRadii(startRadius, cp.endRadius, numParts, 0, 0);
        if (newRadius < cp.minRadius) {
            newRadius = cp.minRadius;
        }
        //if(newRadius > cp.m)
        float oldRadius = startRadius;
        //Point3f cend = new Point3f(end);
        //Point3f cstart = new Point3f(start);
        Point3f cend = end;
        Point3f cstart = start;
        cp.drawNumber.setRotNormal(inc);
        // Generate segments.
        //logger.info("number of new parts: " + numParts);
        for (int i = 0; i < numParts; ++i) {
            //logger.info("generate segments");
            float fdrawLoc = cp.drawNumber.fdraw();
            float rand1 = fdrawLoc + 0.5f;
            float rand2 = /*7.5*/ 0.9f * inc.length() * cp.drawNumber.fpm_onedraw();
            if (Float.isNaN(inc.x)) {
                logger.info("NAN!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN2!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN3!");
            }

            Vector3f rand_rot = cp.drawNumber.getRandomRotOrthogonal(inc);
            cend.x = cstart.x + rand1 * inc.x + rand2 * rand_rot.x;
            cend.y = cstart.y + rand1 * inc.y + rand2 * rand_rot.y;
            cend.z = cstart.z + rand1 * inc.z + rand2 * rand_rot.z;
            if (Float.isNaN(cend.x)) {
                logger.info("NAN4!" + rand_rot + inc);
            }

            Segment segment = new Segment();
            segment.setSegment(cstart, cend, oldRadius, newRadius, cp.soma.getMid());
            segmentList.add(segment);

            cend = new Point3f(cend);
            cstart = new Point3f(cend);
            oldRadius = newRadius;
            newRadius = Segment.interpolateRadii(startRadius, endRadius, numParts, i + 1, 0);
            if (newRadius < cp.minRadius) {
                newRadius = cp.minRadius;
            }
        }

        // Generate branch sections.
        if (cp.branchDistribution.size() - 1 != cp.branchLevel) {
            //logger.info("Generate branch sections.");
            int curBL = cp.branchLevel;
            int noblique = cp.numberOblique;
            cp.parentSection = this;
            cp.branchLevel++;
            Segment lastSegment = segmentList.get(segmentList.size() - 1);
            cp.sectionStartPoint = lastSegment.getEnd();
            cp.startRadius = lastSegment.getEndRadius() * cp.c;
            if (cp.startRadius < cp.minRadius) {
                cp.startRadius = cp.minRadius;
            }

            scale = 1.0f;

            DendriteSection branch0 = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, automaticShortening, down, 0.0f);
            cp.branchDistribution = null;
            if (cp.branchLevel > cp.numberOblique) {
                cp.generationParam = cp.generationParam.getSiblings();
            } else {
                if (cp.obliqueParam == null) {
                    cp.generationParam = null;
                } else {
                    cp.generationParam = cp.obliqueParam.gen_0;
                }
            }
            cp.numberOblique = 0;
            cp.branchLevel = 0;
            cp.startRadius = lastSegment.getEndRadius() * (float) Math.pow((1.0 - Math.pow(cp.c, cp.a)), 1.0 / cp.a);
            if (cp.startRadius < cp.minRadius) {
                cp.startRadius = cp.minRadius;
            }
            cp.endRadius = cp.startRadius * cp.endRadius / startRadius;
            DendriteSection branch1 = null;
            if (cp.generationParam != null) {
                if (!automaticShortening) {
                    if (curBL > noblique - 1) {
                        branch1 = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, false, down, 0.0f);
                    } else {
                        cp.sectionTye = SectionType.OBLIQUE;
                        branch1 = new DendriteSection(new ConstrParameter(cp), false, 1.0f - 0.95f * curBL / noblique, false, down, 0.0f);
                    }
                } else {
                    branch1 = new DendriteSection(new ConstrParameter(cp), respectColumn, (numParts2 - numParts) / cp.genNumberSegments * scale, automaticShortening, down, 0.0f);
                }
            } else {
                branch1 = null;
                logger.debug("NULL branched in DendriteSection::DendriteSection!");
            }
            childrenLink = new SectionLink(this, branch0, branch1);
        }
        //logger.info("end of dendrite section");
    }

    private void generateBranchDistribution(ConstrParameter cp, Point3f start) {
        logger.info("generate branch distribution!");
        //Generate branch distribution.
        float randomNum = cp.drawNumber.fdraw();
        //Anzahl der Zeweige
        int numBranch = (int) (cp.generationParam.getNbranchParam() * (randomNum + 0.5f));
        Vector3f dir = new Vector3f();
        dir.sub(start, cp.sectionEndPoint);
        int locLength = (int) Math.ceil(dir.length());
        int numParts = (int) (locLength * cp.generationParam.getNpartsDensity());
        if (numParts == 0) {
            numParts++;
        }
        cp.genNumberSegments = numParts;
        int[] b_list = new int[numBranch + 1];
        for (int i = 0; i < numBranch + 1; i++) {
            cp.branchDistribution.add(0);
        }
        cp.branchDistribution.set(0, -1);
        b_list[0] = -1;
        // Generate branch points.
        for (int i = 1; i < b_list.length; ++i) {
            float rand_num = cp.drawNumber.fdraw();
            b_list[i] = (int) (rand_num * (numParts - 2));
            //logger.info("b_list[i]: " + b_list[i]);
        }
        Arrays.sort(b_list);
        int differ = 0;
        for (int i = 1; i < b_list.length; i++) {
            if (b_list[i] == b_list[i - 1]) {
                differ++;
            }
        }
        cp.branchDistribution.clear();
        for (int i = 0; i < b_list.length - differ; i++) {
            cp.branchDistribution.add(i, 0);
        }
        //logger.info("branch disr size: " + cp.branch_distr.size());
        cp.branchDistribution.set(0, b_list[0]);
        // for one compartment maximal one branch
        int j = 0;
        for (int i = 1; i < b_list.length; i++) {
            if (b_list[i] != cp.branchDistribution.get(j)) {
                ++j;
                if (cp.branchDistribution.size() == j) {
                    cp.branchDistribution.add(j, b_list[i]);
                } else {
                    cp.branchDistribution.set(j, b_list[i]);
                }
            }
        }
        if (cp.branchDistribution.size() > cp.genNumberSegments - 1) {
            cp.genNumberSegments = cp.branchDistribution.size() + 1;
        }
    }

    private void generateBranchLimitDistribution(ConstrParameter cParam, Point3f start, float lowBranchingLimit) {

        //logger.info("new branch distribution!!");
        //logger.info("low branching limit: " + lowBranchingLimit);

        int noblique = cParam.numberOblique;
        //Generate branch distribution.
        float randomNum = cParam.drawNumber.fdraw();
        int nbranch = (int) (cParam.generationParam.getNbranchParam() * (randomNum + 0.5));
        nbranch += cParam.numberOblique;
        Vector3f dir = new Vector3f();
        dir.sub(start, cParam.sectionEndPoint);
        int locLength = (int) Math.ceil(dir.length());
        int nparts = (int) (locLength * cParam.generationParam.getNpartsDensity());
        if (nparts == 0) {
            nparts++;
        }
        cParam.genNumberSegments = nparts;
        List<Integer> b_list = new ArrayList<Integer>();
        for (int i = 0; i < nbranch + 1; i++) {
            cParam.branchDistribution.add(0);
        }
        cParam.branchDistribution.set(0, -1);
        b_list.add(-1);
        float randNum;
        int nlimit = (int) (lowBranchingLimit * cParam.generationParam.getNpartsDensity());
        if (nlimit > nparts) {
            nlimit = nparts;
        }
        int lowest = nparts;
        int n;
        int numOblique = cParam.numberOblique + 1;
        if (nlimit < nparts) {
            for (int i = numOblique; i < nbranch + 1; ++i) {
                randNum = cParam.drawNumber.fdraw();
                n = (int) (randNum * (nparts - nlimit - 2) + nlimit);
                b_list.add(n);
                if (n < lowest) {
                    lowest = n;
                }
            }
        }

        int highest_o = -1;
        if (lowest > 0) {
            for (int i = 1; i < noblique + 1; ++i) {
                randNum = cParam.drawNumber.fdraw();
                n = (int) (randNum * (lowest - 1));
                b_list.add(n);
                if (n > highest_o) {
                    highest_o = n;
                }
            }
        }

        Set<Integer> b_set = new HashSet<Integer>();
        b_set.addAll(b_list);
        b_list.clear();

        for (Integer i : b_set) {
            b_list.add(i);
        }
        Collections.sort(b_list);
        b_set.clear();

        int index = b_list.indexOf(highest_o);
        if (highest_o != -1 && index != -1) {
            noblique = index;
        }

        cParam.numberOblique = noblique;
        cParam.branchDistribution.clear();
        for (int i = 0; i < b_list.size(); i++) {
            cParam.branchDistribution.add(i, b_list.get(i));
        }

        if (cParam.branchDistribution.size() > cParam.genNumberSegments - 1) {
            cParam.genNumberSegments = cParam.branchDistribution.size() + 1;
        }
    }

    private void testRegionLimits(ConstrParameter cp, boolean respectColumn) {
        // respect column limits
        float dist = cp.soma.getMeanRadius() * 5.0f;
        Point3f somaMid = new Point3f(cp.soma.getMid());
        if (respectColumn) {
            Point3f lowLeftCornerP = Region.getInstance().getLowLeftCorner();
            Vector3f lowLeftCornerVec = new Vector3f();
            lowLeftCornerVec.sub(lowLeftCornerP, somaMid);

            Point3f upRightCornerP = Region.getInstance().getUpRightCorner();
            Vector3f upRightCornerVec = new Vector3f();
            upRightCornerVec.sub(upRightCornerP, somaMid);

            if (lowLeftCornerVec.length() < dist || upRightCornerVec.length() < dist) {

                if (cp.sectionEndPoint.x < lowLeftCornerP.x) {
                    cp.sectionEndPoint.x += 2.0f * (somaMid.x - cp.sectionEndPoint.x); ///< the component of \a v is mirrored at the soma
                } else if (cp.sectionEndPoint.x > upRightCornerP.x) {
                    cp.sectionEndPoint.x += 2.0f * (cp.sectionEndPoint.x - somaMid.x);
                }

                if (cp.sectionEndPoint.y < lowLeftCornerP.y) {
                    cp.sectionEndPoint.y += 2.0f * (somaMid.y - cp.sectionEndPoint.y); ///< the component of \a v is mirrored at the soma
                } else if (cp.sectionEndPoint.y > upRightCornerP.y) {
                    cp.sectionEndPoint.y += 2.0f * (cp.sectionEndPoint.y - somaMid.y);
                }

                if (cp.sectionEndPoint.z < lowLeftCornerP.z) {
                    cp.sectionEndPoint.z += 2.0f * (somaMid.z - cp.sectionEndPoint.z); ///< the component of \a v is mirrored at the soma
                } else if (cp.sectionEndPoint.z > upRightCornerP.z) {
                    cp.sectionEndPoint.z += 2.0f * (cp.sectionEndPoint.z - somaMid.z);
                }
            }

            if (cp.sectionEndPoint.x < lowLeftCornerP.x) {
                cp.sectionEndPoint.x = lowLeftCornerP.x;
            } else if (cp.sectionEndPoint.x > upRightCornerP.x) {
                cp.sectionEndPoint.x = upRightCornerP.x;
            }

            if (cp.sectionEndPoint.y < lowLeftCornerP.y) {
                cp.sectionEndPoint.y = lowLeftCornerP.y;
            } else if (cp.sectionEndPoint.y > upRightCornerP.y) {
                cp.sectionEndPoint.y = upRightCornerP.y;
            }

            if (cp.sectionEndPoint.z < lowLeftCornerP.z) {
                cp.sectionEndPoint.z = lowLeftCornerP.z;
            } else if (cp.sectionEndPoint.z > upRightCornerP.z) {
                cp.sectionEndPoint.z = upRightCornerP.z;
            }
        }
    }

    /**
     * Construct a section with
     * @param cp given ConstrParameter
     * @param respectColumn respect to column limits
     * @param scale linear scaling of the length
     * @param automaticShortening
     */
    @SuppressWarnings("unchecked")
    public DendriteSection(ConstrParameter cp, boolean respectColumn, float scale, boolean automaticShortening) {
        logger.setLevel(Level.OFF);
        setName("dendrite" + getId());
        //logger.setLevel(Level.OFF);
        secType = cp.sectionTye;
        childrenLink = null;
        parentalLink = null;
        Point3f end = new Point3f(cp.sectionEndPoint);
        Point3f start = new Point3f(cp.sectionStartPoint);
        float startRadius = cp.startRadius;
        // This section is firtst of a branch?
        if (cp.branchDistribution == null) {
            //logger.info("first section of a branch");
            cp.branchDistribution = new ArrayList<Integer>();
            // First section of the whole dendrite?
            if (cp.parentSection == null) {
                Vector3f vec = new Vector3f(cp.generationParam.getLenParam().getX(), cp.generationParam.getLenParam().getY(), cp.generationParam.getLenParam().getZ());
                float vecLen = vec.length();
                float rand1 = vecLen * (cp.drawNumber.fdraw() + 0.5f);
                Vector3f randomRot = cp.drawNumber.getRandomRotVector();
                cp.sectionEndPoint.scaleAdd(rand1, randomRot, start);
            } else {
                List<Segment> parentSegments = cp.parentSection.getSegments();
                Point3f parentStart = parentSegments.get(0).getStart();
                Point3f parentEnd = parentSegments.get(parentSegments.size() - 1).getEnd();
                Vector3f direction = new Vector3f();
                direction.sub(parentEnd, parentStart);
                float minAngle = cp.generationParam.getBranchAngle().getMin();
                float maxAngle = cp.generationParam.getBranchAngle().getMax();
                float randAngle = minAngle + (maxAngle - minAngle) * cp.drawNumber.fdraw();
                //logger.info("random angle: " + randAngle);
                Vector3f randomRot = cp.drawNumber.getRandomRotVector(randAngle, direction);
                Vector3f vec = new Vector3f(cp.generationParam.getLenParam().getX(), cp.generationParam.getLenParam().getY(), cp.generationParam.getLenParam().getZ());
                float vecLen = vec.length();
                cp.sectionEndPoint.scaleAdd(vecLen, randomRot, start);
            }
            Vector3f direction = new Vector3f();
            direction.sub(cp.sectionEndPoint, start);
            cp.sectionEndPoint.scaleAdd(scale, direction, start);


            if (cp.sectionEndPoint.z > cp.maxZ) {
                Vector3f diff = new Vector3f();
                diff.sub(cp.sectionEndPoint, start);
                diff.z = cp.maxZ - start.z;
                if (diff.x == 0) {
                    diff.x += 0.1f * cp.drawNumber.pm_onedraw();
                }
                if (diff.y == 0) {
                    diff.y += 0.1f * cp.drawNumber.pm_onedraw();
                }
                float len = diff.length();
                len += 1;
                float cor = (float) Math.sqrt((len + diff.z) * (len - diff.z) / (diff.x * diff.x + diff.y * diff.y));
                diff.x *= cor;
                diff.y *= cor;
                cp.sectionEndPoint.add(diff, start);
            }

            testRegionLimits(cp, respectColumn);

            if (cp.parentSection == null) {
                cp.sectionStartPoint = Cellipsoid.correctStart(cp.soma, cp.sectionStartPoint, cp.sectionEndPoint);
            }

            //Generate branch distribution.
            float randomNum = cp.drawNumber.fdraw();
            int numBranch = (int) (cp.generationParam.getNbranchParam() * (randomNum + 0.5f));
            Vector3f dir = new Vector3f();
            dir.sub(start, cp.sectionEndPoint);
            int locLength = (int) Math.ceil(dir.length());
            int numParts = (int) (locLength * cp.generationParam.getNpartsDensity());
            if (numParts == 0) {
                numParts++;
            }
            cp.genNumberSegments = numParts;
            int[] b_list = new int[numBranch + 1];
            for (int i = 0; i < numBranch + 1; i++) {
                cp.branchDistribution.add(0);
            }
            cp.branchDistribution.set(0, -1);
            b_list[0] = -1;
            // Generate branch points.
            for (int i = 1; i < b_list.length; ++i) {
                float rand_num = cp.drawNumber.fdraw();
                b_list[i] = (int) (rand_num * (numParts - 2));
                //logger.info("b_list[i]: " + b_list[i]);
            }
            Arrays.sort(b_list);
            int differ = 0;
            for (int i = 1; i < b_list.length; i++) {
                if (b_list[i] == b_list[i - 1]) {
                    differ++;
                }
            }
            cp.branchDistribution.clear();
            for (int i = 0; i < b_list.length - differ; i++) {
                cp.branchDistribution.add(i, 0);
            }
            //logger.info("branch disr size: " + cp.branch_distr.size());
            cp.branchDistribution.set(0, b_list[0]);
            // for one compartment maximal one branch
            int j = 0;
            for (int i = 1; i < b_list.length; i++) {
                if (b_list[i] != cp.branchDistribution.get(j)) {
                    ++j;
                    if (cp.branchDistribution.size() == j) {
                        cp.branchDistribution.add(j, b_list[i]);
                    } else {
                        cp.branchDistribution.set(j, b_list[i]);
                    }
                }
            }
            if (cp.branchDistribution.size() > cp.genNumberSegments - 1) {
                cp.genNumberSegments = cp.branchDistribution.size() + 1;
            }
        }

        int branchVal = cp.branchDistribution.get(cp.branchLevel);
        int numParts = cp.genNumberSegments - branchVal;
        numParts = numParts - 1;
        float endRadius = cp.endRadius;
        end = new Point3f(cp.sectionEndPoint);
        Vector3f inc = new Vector3f();
        inc.sub(end, start);
        inc.scale(1.0f / numParts);

        float numParts2 = numParts;
        if (cp.branchDistribution.size() - 1 != cp.branchLevel) {
            numParts -= cp.genNumberSegments - cp.branchDistribution.get(cp.branchLevel + 1) - 1;
            end.sub(start);
            end.scale(((float) numParts) / numParts2);
            end.add(start);
        }
        if (numParts < 1) {
            //nparts = 1;
            for (int i = 0; i < cp.branchDistribution.size(); i++) {
                logger.info("branch distribution[" + i + "]=" + cp.branchDistribution.get(i));
            }
            logger.info("nparts < 1: " + numParts);
            logger.info("<1 segments in a section! " + numParts2);
            logger.info("branch_level=" + cp.branchLevel + ' ' + "nex_branch by segment: " + cp.branchDistribution.get(cp.branchLevel + 1)
                    + ' ' + "this branch by segment: " + cp.branchDistribution.get(cp.branchLevel) + ' ' + "gen_nsegs: " + cp.genNumberSegments);
        }

        if (numParts > 1000) {
            logger.error("number of parts ist > 1000: " + numParts);
            //System.exit(0);
            numParts = 1000;
        }

        float newRadius = Segment.interpolateRadii(startRadius, cp.endRadius, numParts, 0, 0);
        if (newRadius < cp.minRadius) {
            newRadius = cp.minRadius;
        }
        //if(newRadius > cp.m)
        float oldRadius = startRadius;
        //Point3f cend = new Point3f(end);
        //Point3f cstart = new Point3f(start);
        Point3f cend = end;
        Point3f cstart = start;
        cp.drawNumber.setRotNormal(inc);
        // Generate segments.
        //logger.info("number of new parts: " + nparts);
        for (int i = 0; i < numParts; ++i) {
            //logger.info("generate segments");
            float fdrawLoc = cp.drawNumber.fdraw();
            float rand1 = fdrawLoc + 0.5f;
            float rand2 = /*7.5*/ 0.9f * inc.length() * cp.drawNumber.fpm_onedraw();
            if (Float.isNaN(inc.x)) {
                logger.info("NAN!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN2!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN3!");
            }
            Vector3f rand_rot = cp.drawNumber.getRandomRotOrthogonal(inc);
            cend.x = cstart.x + rand1 * inc.x + rand2 * rand_rot.x;
            cend.y = cstart.y + rand1 * inc.y + rand2 * rand_rot.y;
            cend.z = cstart.z + rand1 * inc.z + rand2 * rand_rot.z;
            if (Float.isNaN(cend.x)) {
                logger.info("NAN4!" + rand_rot + inc);
            }

            Segment segment = new Segment();
            segment.setSegment(cstart, cend, oldRadius, newRadius, cp.soma.getMid());
            segmentList.add(segment);

            cend = new Point3f(cend);
            cstart = new Point3f(cend);
            oldRadius = newRadius;
            newRadius = Segment.interpolateRadii(startRadius, endRadius, numParts, i + 1, 0);
            if (newRadius < cp.minRadius) {
                newRadius = cp.minRadius;
            }
        }

        // Generate branch sections.    
        if (cp.branchDistribution.size() - 1 != cp.branchLevel) {
            int curBL = cp.branchLevel;
            int noblique = cp.numberOblique;
            cp.parentSection = this;
            cp.branchLevel++;
            cp.sectionStartPoint = (segmentList.get(segmentList.size() - 1).getEnd());
            cp.startRadius = segmentList.get(segmentList.size() - 1).getEndRadius() * cp.c;
            if (cp.startRadius < cp.minRadius) {
                cp.startRadius = cp.minRadius;
            }
            DendriteSection branch0 = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, automaticShortening);
            cp.branchDistribution = null;
            if (cp.branchLevel > cp.numberOblique) {
                cp.generationParam = cp.generationParam.getSiblings();
            } else {
                if (cp.obliqueParam == null) {
                    cp.generationParam = null;
                } else {
                    cp.generationParam = cp.obliqueParam.gen_0;
                }
            }
            cp.numberOblique = 0;
            cp.branchLevel = 0;
            cp.startRadius = segmentList.get(segmentList.size() - 1).getEndRadius() * (float) Math.pow((1.0 - Math.pow(cp.c, cp.a)), 1.0 / cp.a);
            if (cp.startRadius < cp.minRadius) {
                cp.startRadius = cp.minRadius;
            }
            cp.endRadius = cp.startRadius * cp.endRadius / startRadius;
            DendriteSection branch1 = null;
            if (cp.generationParam != null) {
                if (!automaticShortening) {
                    if (curBL > noblique - 1) {
                        branch1 = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, false);
                    } else {
                        cp.sectionTye = SectionType.OBLIQUE;
                        branch1 = new DendriteSection(new ConstrParameter(cp), false, 1.0f - 0.95f * curBL / noblique, false);
                    }
                } else {
                    branch1 = new DendriteSection(new ConstrParameter(cp), respectColumn, (numParts2 - numParts) / cp.genNumberSegments * scale, automaticShortening);
                }
            } else {
                branch1 = null;
                logger.debug("NULL branched in DendriteSection::DendriteSection!");
            }
            childrenLink = new SectionLink(this, branch0, branch1);
        }
    }

    /**
     * Construct a section (and an apical dendrite which begins with this section)
     * with given
     * @param cParam constructor parameter,
     * @param lowBranchingLimit to signal a bias to not to branch non-oblique branches of.
     */
    public DendriteSection(ConstrParameter cParam, float lowBranchingLimit) {
        logger.setLevel(Level.OFF);
        setName("dendrite" + this.getId());
        secType = cParam.sectionTye;
        float a = cParam.a;
        float c = cParam.c;
        childrenLink = null;
        parentalLink = null;
        Point3f start = new Point3f(cParam.sectionStartPoint);
        int noblique = cParam.numberOblique;
        // generate subdendrite data in cp.
        if (cParam.branchDistribution == null) {
            //logger.info("first section of a branch");
            cParam.branchDistribution = new ArrayList<Integer>();
            float scalingFactor = cParam.drawNumber.fdraw() * 0.5f + 0.75f;
            cParam.sectionEndPoint.scaleAdd(scalingFactor, new Point3f(cParam.generationParam.getLenParam().getX(),
                    cParam.generationParam.getLenParam().getY(),
                    cParam.generationParam.getLenParam().getZ()), start);
            if (cParam.sectionEndPoint.z > cParam.maxZ) {
                Vector3f diff = new Vector3f();
                diff.sub(cParam.sectionEndPoint, start);
                diff.z = cParam.maxZ - start.z;
                if (diff.x == 0) {
                    diff.x += 0.1f * cParam.drawNumber.pm_onedraw();
                }
                if (diff.y == 0) {
                    diff.y += 0.1 * cParam.drawNumber.pm_onedraw();
                }
                float len = diff.length();
                len += 1;
                float cor = (float) Math.sqrt((len + diff.z) * (len - diff.z) / (diff.x * diff.x + diff.y * diff.y));
                diff.x *= cor;
                diff.y *= cor;
                cParam.sectionEndPoint.add(diff, start);
            }
            cParam.sectionStartPoint = Cellipsoid.correctStart(cParam.soma, cParam.sectionStartPoint, cParam.sectionEndPoint);

            //Generate branch distribution.
            float randomNum = cParam.drawNumber.fdraw();
            int nbranch = (int) (cParam.generationParam.getNbranchParam() * (randomNum + 0.5));
            nbranch += cParam.numberOblique;
            Vector3f dir = new Vector3f();
            dir.sub(start, cParam.sectionEndPoint);
            int locLength = (int) Math.ceil(dir.length());
            int nparts = (int) (locLength * cParam.generationParam.getNpartsDensity());
            if (nparts == 0) {
                nparts++;
            }
            cParam.genNumberSegments = nparts;
            List<Integer> b_list = new ArrayList<Integer>();
            for (int i = 0; i < nbranch + 1; i++) {
                cParam.branchDistribution.add(0);
            }
            cParam.branchDistribution.set(0, -1);
            b_list.add(-1);
            float randNum;
            int nlimit = (int) (lowBranchingLimit * cParam.generationParam.getNpartsDensity());
            if (nlimit > nparts) {
                nlimit = nparts;
            }
            int lowest = nparts;
            int n;
            int numOblique = cParam.numberOblique + 1;
            if (nlimit < nparts) {
                for (int i = numOblique; i < nbranch + 1; ++i) {
                    randNum = cParam.drawNumber.fdraw();
                    n = (int) (randNum * (nparts - nlimit - 2) + nlimit);
                    b_list.add(n);
                    if (n < lowest) {
                        lowest = n;
                    }
                }
            }

            int highest_o = -1;
            if (lowest > 0) {
                for (int i = 1; i < noblique + 1; ++i) {
                    randNum = cParam.drawNumber.fdraw();
                    n = (int) (randNum * (lowest - 1));
                    b_list.add(n);
                    if (n > highest_o) {
                        highest_o = n;
                    }
                }
            }

            Set<Integer> b_set = new HashSet<Integer>();
            b_set.addAll(b_list);
            b_list.clear();

            for (Integer i : b_set) {
                b_list.add(i);
            }
            Collections.sort(b_list);
            b_set.clear();

            int index = b_list.indexOf(highest_o);
            if (highest_o != -1 && index != -1) {
                noblique = index;
            }

            cParam.numberOblique = noblique;
            cParam.branchDistribution.clear();
            for (int i = 0; i < b_list.size(); i++) {
                cParam.branchDistribution.add(i, b_list.get(i));
            }

            if (cParam.branchDistribution.size() > cParam.genNumberSegments - 1) {
                cParam.genNumberSegments = cParam.branchDistribution.size() + 1;
            }
        }

        int branchVal = cParam.branchDistribution.get(cParam.branchLevel);
        int nparts = cParam.genNumberSegments - branchVal;
        nparts = nparts - 1;

        Point3f end = new Point3f(cParam.sectionEndPoint);
        float startRadius = cParam.startRadius;
        float endRadius = cParam.endRadius;

        Vector3f inc = new Vector3f();
        inc.sub(end, start);
        inc.scale(1.0f / nparts);

        float nparts2 = nparts;
        if (cParam.branchDistribution.size() - 1 != cParam.branchLevel) {
            nparts -= cParam.genNumberSegments - cParam.branchDistribution.get(cParam.branchLevel + 1) - 1;
            end.sub(start);
            end.scale(((float) nparts) / nparts2);
            end.add(start);
        }

        Point3f cend = end;
        Point3f cstart = start;
        float newRadius = Segment.interpolateRadii(startRadius, cParam.endRadius, nparts, 0, 0);
        if (newRadius < cParam.minRadius) {
            newRadius = cParam.minRadius;
        }
        float old_radius = startRadius;
        //logger.info("generate segments");
        // Generate segments.
        for (int i = 0; i < nparts; ++i) {
            float fdrawLoc = cParam.drawNumber.fdraw();
            float rand1 = fdrawLoc + 0.5f;
            float rand2 = /*7.5*/ 0.9f * inc.length() * cParam.drawNumber.fpm_onedraw();
            if (Float.isNaN(inc.x)) {
                logger.info("NAN!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN2!");
            }
            if (Float.isNaN(rand2)) {
                logger.info("NAN3!");
            }
            Vector3f rand_rot;
            if (i == 0) {
                rand_rot = cParam.drawNumber.getRandomRotOrthogonal(inc);
                cend.x = cstart.x + rand1 * inc.x + rand2 * rand_rot.x;
                cend.y = cstart.y + rand1 * inc.y + rand2 * rand_rot.y;
                cend.z = cstart.z + rand1 * inc.z + rand2 * rand_rot.z;
            } else {
                rand_rot = cParam.drawNumber.getRandomRotOrthogonal(segmentList.get(i - 1).getDirection());
                cend.x = cstart.x + rand1 * inc.x + rand2 * rand_rot.x;
                cend.y = cstart.y + rand1 * inc.y + rand2 * rand_rot.y;
                cend.z = cstart.z + rand1 * inc.z + rand2 * rand_rot.z;
            }

            if (Float.isNaN(cend.x)) {
                logger.info("NAN4!" + rand_rot + inc);
            }

            Segment segment = new Segment();
            segment.setSegment(cstart, cend, old_radius, newRadius, cParam.soma.getMid());
            segmentList.add(segment);

            cstart = new Point3f(cend);
            cend = new Point3f(cend);
            old_radius = newRadius;
            newRadius = Segment.interpolateRadii(startRadius, endRadius, nparts, i + 1, 0);
            if (newRadius < cParam.minRadius) {
                newRadius = cParam.minRadius;
            }
        }

        //logger.info("generate branch sections");
        // Generate branch sections.
        if (cParam.branchDistribution.size() - 1 != cParam.branchLevel) {
            //logger.info("generate branch sections");
            int curBL = cParam.branchLevel;
            cParam.parentSection = this;
            cParam.branchLevel++;
            //logger.info("segmentList.size is: " + segmentList.size());
            cParam.sectionStartPoint = segmentList.get(segmentList.size() - 1).getEnd();
            cParam.startRadius = segmentList.get(segmentList.size() - 1).getEndRadius() * c;

            if (cParam.startRadius < cParam.minRadius) {
                cParam.startRadius = cParam.minRadius;
            }

            cParam.sectionTye = DendriteSection.SectionType.APICAL;
            DendriteSection branch0 = new DendriteSection(new ConstrParameter(cParam), false, 1.0f, true);
            cParam.branchDistribution = null;
            cParam.branchLevel = 0;

            float scale_factor = 1.0f;
            if (curBL > noblique - 1) {
                cParam.generationParam = cParam.generationParam.getSiblings();
                scale_factor = (nparts2 - nparts) / cParam.genNumberSegments;
                cParam.sectionTye = DendriteSection.SectionType.APICAL;
            } else {
                if (cParam.obliqueParam == null) {
                    cParam.generationParam = null;
                } else {
                    cParam.generationParam = cParam.obliqueParam.gen_0;
                }
                scale_factor = 1.0f - curBL * 0.95f / noblique;
                cParam.sectionTye = DendriteSection.SectionType.OBLIQUE;
            }
            cParam.numberOblique = 0;
            cParam.startRadius = segmentList.get(segmentList.size() - 1).getEndRadius() * (float) Math.pow((1.0f - Math.pow(c, a)), 1 / a);
            if (cParam.startRadius < cParam.minRadius) {
                cParam.startRadius = cParam.minRadius;
            }
            cParam.endRadius = cParam.startRadius * cParam.endRadius / startRadius;
            DendriteSection branch1 = null;
            if (cParam.generationParam != null) {
                if (cParam.sectionTye == DendriteSection.SectionType.APICAL) {
                    branch1 = new DendriteSection(new ConstrParameter(cParam), false, scale_factor, true);
                } else {
                    branch1 = new DendriteSection(new ConstrParameter(cParam), false, scale_factor, false);
                }
            } else {
                branch1 = null;
            }
            childrenLink = new SectionLink(this, branch0, branch1);
        }
    }
}
