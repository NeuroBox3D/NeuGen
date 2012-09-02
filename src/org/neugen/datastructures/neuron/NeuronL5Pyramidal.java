package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.KeyIdentificable;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 *
 * File: L5PyramidalNeuron.java
 *
 * @author Jens Eberhard
 * @author Alexander Wanner
 *
 * Created on 14.12.2009, 09:36:31
 *
 * Subclass for a L5 pyramidal neuron.
 */
public class NeuronL5Pyramidal extends NeuronPyramidal implements Serializable {

    public static class L5PyramidalParam extends PyramidalParam {

        private static L5PyramidalParam instance;

        /** Constructs contained parameters. */
        public L5PyramidalParam(String lastKey) {
            super(PyramidalParam.getInstance(), lastKey);
        }

        public L5PyramidalParam(KeyIdentificable parent, String lastKey) {
            super(parent, lastKey);
        }

        public static void setInstance(L5PyramidalParam instance) {
            L5PyramidalParam.instance = instance;
        }

        /** Returns instance. */
        public static L5PyramidalParam getInstance() {
            if (instance == null) {
                L5PyramidalParam param = new L5PyramidalParam(ParameterConstants.SUFFIX_PATH_L5PYRAMIDAL_PARAM);
                param.setApicalParam(ParameterConstants.LAST_KEY_APICAL);
                param.setBasalParam(ParameterConstants.LAST_KEY_BASAL);
                L5PyramidalParam.setInstance(param);
            }
            return instance;
        }
    }
    private static final long serialVersionUID = -8689338926398031243L;

    /** Constructor. */
    public NeuronL5Pyramidal() {
        super();
    }

    @Override
    public L5PyramidalParam getParam() {
        return L5PyramidalParam.getInstance();
    }

    /**
     * Function for setting a pyramidal neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        String mes = "set for " + getType() + " neuron";
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = soma.getMeanRadius();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = -1;
      
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;

        soma.cylindricRepresentant();
        axon.set(axonStart, axonEnd, getParam().getAxonParam());
        somaMid.z += somaRadius;
        //logger.info("set dendirte");
        int npyramidaldendrite = getParam().getNumberOfApicalDendrites();
        Vrand dendriteRandomNumber = new Vrand(getParam().getDendriteParam().getSeedValue());
        Vrand apicalRandomNumber = new Vrand(getParam().getApicalParam().getSeedValue());
        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            if (i < npyramidaldendrite) {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(apicalRandomNumber);
                dendrite.setPyramidalDendrite(getParam().getApicalParam(), soma, deviation, getParam().getApicalParam().getNumOblique());
                dendrites.add(dendrite);
            } else {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(dendriteRandomNumber);
                dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, false);
                dendrites.add(dendrite);
            }
        }
    }
}
