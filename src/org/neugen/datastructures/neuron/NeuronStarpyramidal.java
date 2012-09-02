/*
 * File: StarpyramidalNeuron.java
 * Created on 13.10.2009, 09:47:29
 *
 */
package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 * @author Jens Eberhard
 * @author Simone Eberhard
 * @author Alexander Wanner
 */
public final class NeuronStarpyramidal extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        /** basal dendrite parameter */
        private final DendriteParam.BasalParam basal;
        private static Param instance;

        /** Constructs contained parameters. */
        private Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
            basal = dendriteParam;
        }

        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        /** 
         * Returns instance.
         *
         * @return Instance of StarpyramidalParam.
         */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new NeuronStarpyramidal.Param(ParameterConstants.SUFFIX_PATH_STARPYRAMIDAL);
                param.setApicalParam(ParameterConstants.SUFFIX_PATH_APICAL);
                param.setBasalParam(ParameterConstants.SUFFIX_PATH_BASAL);
                Param.setInstance(param);
                param.getApicalParam().setTopFluctuation(null);
            }
            return instance;
        }

        /** Returns true exactly if all container parameter are valid. */
        @Override
        public boolean isValid() {
            return super.isValid() && apicalParam.isValid();
        }

        /** 
         * Get the string representation of the dendrite.
         *
         * @return the string representation of the starpyramidal parameter.
         */
        @Override
        public String toString() {
            String ret = "\n" + this.key + " parameter: validation = " + isValid() + "\nseed = " + seed.getValue()
                    + "\n nden = " + numDen.getValue() + "\nnapiden = " + napiden.getValue()
                    + "\n deviation(each coord. in times of soma radius) = " + deviation.toString()
                    + "\n" + somaParam.toString() + axonParam.toString() + basal.toString() + apicalParam.toString()
                    + synapseParam.toString() + this.key + " end\n\n";
            return ret;
        }
    }
    private static final long serialVersionUID = -6469337816398031243L;

    private static Vrand basalRandomNumber;
    private static Vrand apicalRandomNumber;

    /** Constructor. */
    public NeuronStarpyramidal() {
        super();
        type = DataStructureConstants.STAR_PYRAMIDAL;
        if (basalRandomNumber == null) {
            basalRandomNumber = new Vrand(getParam().getDendriteParam().getSeedValue());
        }

        if (apicalRandomNumber == null) {
            apicalRandomNumber = new Vrand(getParam().getApicalParam().getSeedValue());
        }

        if (drawNumber == null) {
            drawNumber = new Vrand(getParam().getSeedValue());
        }
    }


    @Override
    public NeuronParam getParam() {
        return Param.getInstance();
    }

    /**
     * Function for setting a L4stellate neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        String mes = "set for " + getType() + " neuron";
        //logger.info(mes);
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = soma.getMeanRadius();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = drawNumber.pm_onedraw();
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;

        soma.cylindricRepresentant();
        axon.set(axonStart, axonEnd, getParam().getAxonParam());
        somaMid.z += somaRadius;
        //logger.info("set dendirte");
        int npyramidaldendrite = getParam().getNumberOfApicalDendrites();
        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            if (i < npyramidaldendrite) {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(apicalRandomNumber);
                dendrite.setPyramidalDendrite(getParam().getApicalParam(), soma, deviation, getParam().getApicalParam().getNumOblique());
                dendrites.add(dendrite);
            } else {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(basalRandomNumber);
                dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, false);
                dendrites.add(dendrite);
            }
        }
    }
}
