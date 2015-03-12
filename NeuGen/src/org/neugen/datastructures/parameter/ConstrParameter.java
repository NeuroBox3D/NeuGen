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
