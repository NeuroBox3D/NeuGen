package org.neugen.datastructures.parameter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Point3f;
import org.neugen.datastructures.Cellipsoid;
import org.neugen.datastructures.Section;
import org.neugen.utils.Vrand;

public final class ConstrParameter implements Serializable {

    static final long serialVersionUID = -6131930069184524614L;
    /** Parent section. */
    public Section parentSection = new Section();
    /** end point of a string. */
    public Point3f sectionEndPoint;
    /** start point of a section. */
    public Point3f sectionStartPoint;
    /** dendrite draw_number */
    public Vrand drawNumber;
    /** Branch distribution. Gives an array of segments, behind which branches begin. */
    public List<Integer> branchDistribution = new ArrayList<Integer>();
    /** Parameter for the whole generation a string belongs to. */
    public SubCommonTreeParam generationParam;
    /** Soma of the dendrite the string is belonging to. */
    public Cellipsoid soma;
    /** Deviation vector. */
    //public Vector3f deviation;
    /** Branching level of a section. */
    public int branchLevel;
    /** End radius of a string. */
    public float endRadius;
    /** Start radius of a string. */
    public float startRadius;
    /** Only radia >= radius_treshold are generated. */
    public float minRadius;
    /** Number of segements. */
    public int genNumberSegments;
    /** Parameter for eventually existing oblique branches. */
    public ObliqueParam obliqueParam;
    /** Number of oblique branches. */
    public int numberOblique;
    /** Maximal possible z-coordinate of the section. */
    public float maxZ;
    /** Ralls power low interpretation: r1^a=(c*r1)^a+r3^a. */
    public float a;
    public float c;
    /** Type id. */
    public Section.SectionType sectionTye;
    public int id;

    public boolean horizontalDir;

    public ConstrParameter() {
        //gen_param  = new SubdendriteParam(par, null) ??
        branchLevel = 0;
        endRadius = -1;
        startRadius = -1;
        minRadius = -1;
        genNumberSegments = -1;
        numberOblique = 0;
        a = 0;
        c = 0;
        obliqueParam = new ObliqueParam(null);
        id = 0;
        drawNumber = null;
    }

    // copy constructor
    public ConstrParameter(ConstrParameter c) {
        parentSection = c.parentSection;
        sectionEndPoint = new Point3f(c.sectionEndPoint);
        sectionStartPoint = new Point3f(c.sectionStartPoint);
        branchDistribution = c.branchDistribution;
        generationParam = c.generationParam;
        soma = c.soma;
        //deviation = new Vector3f(c.deviation);
        branchLevel = c.branchLevel;
        endRadius = c.endRadius;
        startRadius = c.startRadius;
        minRadius = c.minRadius;
        genNumberSegments = c.genNumberSegments;
        obliqueParam = c.obliqueParam;
        numberOblique = c.numberOblique;
        maxZ = c.maxZ;
        a = c.a;
        this.c = c.c;
        id = c.id;
        id++;
        sectionTye = c.sectionTye;
        drawNumber = c.drawNumber;
        horizontalDir = c.horizontalDir;
    }
}
