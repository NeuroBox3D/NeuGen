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
/*
 * Dendrite.java
 *
 * created on 19.03.2004
 *
 */
package org.neugen.datastructures;

import org.neugen.datastructures.parameter.ConstrParameter;
import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.apache.log4j.Logger;
import org.neugen.utils.Vrand;
import org.neugen.datastructures.parameter.CommonTreeParam;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.DendriteParam.ApicalParam;
import org.neugen.datastructures.parameter.DendriteParam.BasalParam;

/**
 * It contains the basic class for the construction of a single dendrite.
 * The dendrite can have several branches where the forks are installed. The branches can be
 * basal, pyramidal, or oblique branches.
 * 
 * @author Jens Eberhard
 */
public class Dendrite extends NeuronalTreeStructure<DendriteSection> implements Serializable {

    private static final long serialVersionUID = -4611546473283433478L;
    /** use to log messages */
    private final static Logger logger = Logger.getLogger(Dendrite.class.getName());
    /** random number generator */
    private transient Vrand drawNumber;
    /** Pointer to dendrite parameter */
    public transient CommonTreeParam denPar;

    public Dendrite() {
        super();
        firstSection = new DendriteSection();
    }

    public void setDrawNumber(Vrand drawNumber) {
        this.drawNumber = drawNumber;
    }


    /**
     * Set basal dendrites of interneuron 
     */
    public void setHorizontalBasalDendrite(BasalParam param, Cellipsoid soma, Vector3f deviation, float scale, boolean down, boolean horizontalDir) {
        logger.info("set horizontal basal dendrit!!!");
        drawNumber.setRotDeviation(deviation);
        denPar = param;
        ConstrParameter cp = new ConstrParameter();
        cp.drawNumber = drawNumber;
        cp.sectionTye = DendriteSection.SectionType.BASAL;
        cp.branchDistribution = null;
        cp.branchLevel = 0;
        //cp.deviation = new Vector3f(deviation);

        cp.startRadius = param.getRad().getMax().floatValue();
        cp.endRadius = param.getRad().getMin().floatValue();
        cp.minRadius = param.getRad().getMin().floatValue();

        cp.genNumberSegments = 1;
        cp.generationParam = param.getFirstGen();
        cp.parentSection = null;
        cp.soma = soma;

        // unter dem soma!
        int up_down = -1;
        cp.sectionStartPoint = new Point3f(soma.getMid());
        cp.sectionStartPoint.z += up_down * soma.getMaxRadius();
        cp.sectionEndPoint = new Point3f(soma.getMid());
        /*
        cp.end.x += param.getFirstGen().getLenParam().getX();
        cp.end.y += param.getFirstGen().getLenParam().getY();
        cp.end.z += down * param.getFirstGen().getLenParam().getZ();
         *
         */
        cp.obliqueParam = null;
        cp.numberOblique = 0;
        cp.maxZ = Region.getInstance().getUpRightCorner().z;

        //logger.info("max z Wert: " + cp.maxZ);
        cp.a = param.getA();
        cp.c = param.getC();
        cp.id = 0;
        cp.horizontalDir = true;

        boolean respectColumn = false;
        boolean automaticShortening = false;

        float lowBranchingLimit = param.getLowBranchingLimit().getValue();

        if(down) {
            lowBranchingLimit = 0.0f;
        }

        firstSection = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, automaticShortening, down, lowBranchingLimit);
        //logger.info("first sec lenght: " + firstSection.getLength());
        if (firstSection.getChildrenLink() != null) {
            //logger.info("update poly soma dist!");
            firstSection.getChildrenLink().updatePolySomaDist();
        }
    }


    /**
     * Set an pyramidal basal dendrite (CA1)
     */
    public void setBasalDendrite(BasalParam param, Cellipsoid soma, Vector3f deviation, float scale, boolean down) {
        drawNumber.setRotDeviation(deviation);
        denPar = param;
        ConstrParameter cp = new ConstrParameter();
        cp.drawNumber = drawNumber;
        cp.sectionTye = DendriteSection.SectionType.BASAL;
        cp.branchDistribution = null;
        cp.branchLevel = 0;
        //cp.deviation = new Vector3f(deviation);
       
        cp.startRadius = param.getRad().getMax().floatValue();
        cp.endRadius = param.getRad().getMin().floatValue();
        cp.minRadius = param.getRad().getMin().floatValue();

        cp.genNumberSegments = 1;
        cp.generationParam = param.getFirstGen();
        cp.parentSection = null;
        cp.soma = soma;

        // unter dem soma!
        int up_down = -1;
        cp.sectionStartPoint = new Point3f(soma.getMid());
        cp.sectionStartPoint.z += up_down * soma.getMaxRadius();
        cp.sectionEndPoint = new Point3f(soma.getMid());
        /*
        cp.end.x += param.getFirstGen().getLenParam().getX();
        cp.end.y += param.getFirstGen().getLenParam().getY();
        cp.end.z += down * param.getFirstGen().getLenParam().getZ();
         * 
         */
        cp.obliqueParam = null;
        cp.numberOblique = 0;
        cp.maxZ = Region.getInstance().getUpRightCorner().z;

        //logger.info("max z Wert: " + cp.maxZ);
        cp.a = param.getA();
        cp.c = param.getC();
        cp.id = 0;

        boolean respectColumn = false;
        boolean automaticShortening = false;

        float lowBranchingLimit = param.getLowBranchingLimit().getValue();

        if(down) {
            lowBranchingLimit = 0.0f;
        }
               
        firstSection = new DendriteSection(new ConstrParameter(cp), respectColumn, scale, automaticShortening, down, lowBranchingLimit);
        //logger.info("first sec lenght: " + firstSection.getLength());
        if (firstSection.getChildrenLink() != null) {
            //logger.info("update poly soma dist!");
            firstSection.getChildrenLink().updatePolySomaDist();
        }
    }
    

    /**
     * Set an usual dendrite. It creates the list of compartments.
     * @param param dendrite parameter.
     * @param soma the soma of the neuron.
     * @param deviation the deviation vector for the compartments of dendrite.
     * @param respectColumns says to respect column limits.?
     */
    public void setDendrite(DendriteParam param, Cellipsoid soma, Vector3f deviation, boolean respectColumns) {
        drawNumber.setRotDeviation(deviation);
        denPar = param;
        ConstrParameter cp = new ConstrParameter();
        cp.drawNumber = drawNumber;
        cp.sectionTye = DendriteSection.SectionType.BASAL;
        cp.branchDistribution = null;
        cp.branchLevel = 0;
        //cp.deviation = new Vector3f(deviation);
        cp.sectionEndPoint = new Point3f();

        cp.startRadius = param.getRad().getMax().floatValue();
        cp.endRadius = param.getRad().getMin().floatValue();
        cp.minRadius = param.getRad().getMin().floatValue();

        //logger.info("max radius of dendrite: " + cp.startRadius);
        //logger.info("min radius of dendrite: " + cp.endRadius);

        cp.genNumberSegments = 1;
        cp.generationParam = param.getFirstGen();
        cp.parentSection = null;
        cp.soma = soma;

        cp.sectionStartPoint = new Point3f(soma.getMid());
        cp.sectionStartPoint.z += soma.getMeanRadius();

        cp.obliqueParam = null;
        cp.numberOblique = 0;
        cp.maxZ = Region.getInstance().getUpRightCorner().z;

        cp.a = param.getA();
        cp.c = param.getC();
        cp.id = 0;

    
        //logger.info("max angle: " + param.getFirstGen().getBranchAngle().getMax());
        //logger.info("min angle: " + param.getFirstGen().getBranchAngle().getMin());
        //logger.info("setDendrite randomNum(orig): " + drawNumber.draw());
        //logger.info("setDendrite randomNum(cp): " + cp.drawNumber.draw());
        //logger.info("random number counter: " + draw_number.getRandNumCounter());
        firstSection = new DendriteSection(new ConstrParameter(cp), respectColumns, 1.0f, false);
        if (firstSection.getChildrenLink() != null) {
            firstSection.getChildrenLink().updatePolySomaDist();
        }
    }


    /**
     * Set an apical pyramidal dendrite. It creates the list of compartments.
     * @param soma the soma of the neuron.
     * @param deviation the deviation vector for the compartments of the pyramidal end of dendrite.
     * @param numObliqueBranch apical_continuations the number of oblique branches for
     */

    public void setPyramidalDendrite(ApicalParam param, Cellipsoid soma, Vector3f deviation, int numObliqueBranch) {
        drawNumber.setRotDeviation(deviation);
        denPar = param;
        ConstrParameter cp = new ConstrParameter();
        cp.drawNumber = drawNumber;
        cp.sectionTye = DendriteSection.SectionType.APICAL;
        cp.branchDistribution = null;
        cp.branchLevel = 0;
        //cp.deviation = deviation;
        cp.sectionEndPoint = new Point3f();

        cp.startRadius = param.getRad().getMax();
        cp.endRadius = param.getRad().getMin();
        cp.minRadius = param.getRad().getMin();

        cp.genNumberSegments = 1;
        cp.generationParam = param.getFirstGen();
        cp.parentSection = null;
        cp.soma = soma;

        cp.sectionStartPoint = new Point3f(soma.getMid());
        cp.sectionStartPoint.z += soma.getMeanRadius();

        cp.obliqueParam = param.getOblique();
        cp.numberOblique = numObliqueBranch;

        cp.a = param.getA();
        cp.c = param.getC();
        cp.id = 0;

        if (!param.getFirstGen().getLenParam().isValid()) {
            cp.maxZ = Region.getInstance().getUpRightCorner().z;
        } else {
            cp.maxZ = param.getFirstGen().getLenParam().getZ() * (1 + param.getTopFluctuationValue() * (2 * drawNumber.fdraw() - 1)) + soma.getMid().z;
            if (cp.maxZ > Region.getInstance().getUpRightCorner().z) {
                cp.maxZ = Region.getInstance().getUpRightCorner().z;
            }
        }

        if (!param.getLowBranchingLimit().isValid()) {
            firstSection = new DendriteSection(new ConstrParameter(cp), 0.0f);
        } else {
            logger.info("create dendrite section with low branching limit: " + param.getLowBranchingLimit().getValue());
            firstSection = new DendriteSection(new ConstrParameter(cp), param.getLowBranchingLimit().getValue());
        }
        if (firstSection.getChildrenLink() != null) {
            firstSection.getChildrenLink().updatePolySomaDist();
        }
    }
    
}
