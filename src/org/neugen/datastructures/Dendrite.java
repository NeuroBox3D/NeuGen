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
