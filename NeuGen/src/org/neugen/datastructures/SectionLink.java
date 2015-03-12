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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;

/**
 * @author Jens Eberhard
 * @author Alexander Wanner
 */

class ProtoLink implements Serializable {

    static final long serialVersionUID = -1291930069185634614L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(ProtoLink.class.getName());
    /** Distance composed of segment lengths. */
    protected float polygonal_soma_distance;
    /** Branching angles of both branches to the axis of last segment of parent section. */
    protected float[] branching_angles;

    public ProtoLink() {
        branching_angles = new float[2];
        polygonal_soma_distance = 0.0f;
    }

    /** Returns polygonal composed distance to soma of this link. */
    public float getPolySomaDist() {
        return polygonal_soma_distance;
    }

    public float[] getAngles() {
        return branching_angles;
    }
}

public final class SectionLink extends ProtoLink implements Serializable {

    private final static long serialVersionUID = -2689337816398030143L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(SectionLink.class.getName());
    /** Parental section. */
    private Section parental;
    /** first branch section. */
    private Section branch0;
    /** second branch section. */
    private Section branch1;
    /** Children sections (for more than two branches..) */
    private final Set<Section> childrenSet;

    public SectionLink() {
        super();
        childrenSet = new HashSet<Section>();
    }

    public SectionLink(Section parent, Section branch0, Section branch1) {
        polygonal_soma_distance = 0.0f;
        childrenSet = new HashSet<Section>();
        set(parent, branch0, branch1);
    }

    public final void set(Section parent, Section branch0, Section branch1) {
        this.parental = parent;
        this.branch0 = branch0;
        parent.setChildrenLink(this);
        //float s_product = 0.0f;
        if (branch0 != null) {
            branch0.setParentalLink(this);
            //Calculate angles.
            List<Segment> parentSegments = parent.getSegments();
            Segment parentEndSeg = parentSegments.get(parentSegments.size() - 1);
            Point3f start = parentEndSeg.getStart();
            Point3f end = parentEndSeg.getEnd();
            Vector3f v1 = new Vector3f();
            v1.sub(start, end);
            if(branch0.getSegments().size() > 0) {
                Segment branch0StartSeg = (branch0.getSegments()).get(0);
                Point3f start2 = branch0StartSeg.getStart();
                Point3f end2 = branch0StartSeg.getEnd();
                Vector3f v2 = new Vector3f();
                v2.sub(start2, end2);
                branching_angles[0] = v2.angle(v1);
            } else {
                Vector3f v2 = new Vector3f();
                branching_angles[0] = v2.angle(v1);
            }
            childrenSet.add(branch0);
        }

        //s_product = 0.0f;
        this.branch1 = branch1;
        if (branch1 != null) {
            branch1.setParentalLink(this);
            //Calculate angles.
            List<Segment> parentSegments = parent.getSegments();
            Segment parentEndSeg = parentSegments.get(parentSegments.size() - 1);
            Point3f start = parentEndSeg.getStart();
            Point3f end = parentEndSeg.getEnd();
            Vector3f v1 = new Vector3f();
            v1.sub(start, end);
            if(branch1.getSegments().size() > 0) {
                Segment branch1StartSeg = (branch1.getSegments()).get(0);
                Point3f start2 = branch1StartSeg.getStart();
                Point3f end2 = branch1StartSeg.getEnd();
                Vector3f v2 = new Vector3f();
                v2.sub(start2, end2);
                branching_angles[1] = v2.angle(v1) / (float) Math.PI * 180.0f;
            } else {
                Vector3f v2 = new Vector3f();
                branching_angles[1] = v2.angle(v1) / (float) Math.PI * 180.0f;
            }
            childrenSet.add(branch1);
        }
    }

    /**
     * Get the value of parental
     *
     * @return the value of parental
     */
    public Section getParental() {
        return parental;
    }

    /**
     * Set the value of parental
     *
     * @param parental new value of parental
     */
    public void setParental(Section parental) {
        this.parental = parental;
    }

    /**
     * Get the value of branch1
     *
     * @return the value of branch1
     */
    public Section getBranch1() {
        return branch1;
    }

    /**
     * Set the value of branch1
     *
     * @param branch1 new value of branch1
     */
    public void setBranch1(Section branch1) {
        this.branch1 = branch1;
    }

    /**
     * Get the value of branch0
     *
     * @return the value of branch0
     */
    public Section getBranch0() {
        return branch0;
    }

    /**
     * Set the value of branch0
     *
     * @param branch0 new value of branch0
     */
    public void setBranch0(Section branch0) {
        this.branch0 = branch0;
    }

    /**
     * Get the value of childrenSet
     *
     * @return the value of childrenSet
     */
    public Set<Section> getChildren() {
        return childrenSet;
    }

    /**
     * Updates polygonal composed distance to soma with help of links
     * closer to soma.
     */
    public void updatePolySomaDist() {
        polygonal_soma_distance = parental.getLength();
        if (parental.getParentalLink() != null) {
            polygonal_soma_distance += parental.getParentalLink().getPolySomaDist();
        }
        if (branch0 != null && branch0.getChildrenLink() != null) {
            branch0.getChildrenLink().updatePolySomaDist();
        }
        if (branch1 != null && branch1.getChildrenLink() != null) {
            branch1.getChildrenLink().updatePolySomaDist();
        }
    }
}
