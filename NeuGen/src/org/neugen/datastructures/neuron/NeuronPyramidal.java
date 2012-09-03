package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.KeyIdentificable;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 *
 * File: PyramidalNeuron.java
 * @author Jens Eberhard
 * @author Alexander Wanner
 * Created on 11.12.2009, 09:33:45
 */
public class NeuronPyramidal extends NeuronBase implements Serializable {

    /**
     * Class for cell parameter of pyramidal neuron.
     * @author Alexander Wanner
     */
    public static class PyramidalParam extends NeuronParam {

        /** basal dendrite parameter */
        public DendriteParam.BasalParam basal;
        private static PyramidalParam instance;

        /** Constructs contained parameters.  */
        public PyramidalParam(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
            basal = getDendriteParam();
        }

        public PyramidalParam(KeyIdentificable parent, String lastKey) {
            super(parent, lastKey);
        }

        public static void setInstance(PyramidalParam instance) {
            PyramidalParam.instance = instance;
        }

        /** Returns instance. */
        public static PyramidalParam getInstance() {
            if (instance == null) {
                PyramidalParam pyrParam = new NeuronPyramidal.PyramidalParam(ParameterConstants.SUFFIX_PATH_PYRAMIDAL_PARAM);
                pyrParam.setApicalParam(ParameterConstants.SUFFIX_PATH_APICAL);
                pyrParam.setBasalParam(ParameterConstants.SUFFIX_PATH_BASAL);
                PyramidalParam.setInstance(pyrParam);
            }
            return instance;
        }

        /** Returns true exactly if all container parameter are valid. */
        @Override
        public boolean isValid() {
            return super.isValid() && apicalParam.isValid();
        }

        /** Returns string representation of the dendrite. */
        @Override
        public String toString() {
            String ret = "\n" + this.key + " parameter: validation = " + isValid() + "\nseed = " + seed.getValue()
                    + "\nnden = " + numDen.getValue() + "\nnapiden = " + napiden.getValue()
                    + "\ndeviation(each coord. in times of soma radius) = " + deviation.toString()
                    + "\n" + somaParam.toString() + axonParam.toString() + basal.toString() + apicalParam.toString()
                    + synapseParam.toString() + this.key + " end\n\n";
            return ret;
        }
    }

    private static final long serialVersionUID = -8689337816398142243L;
    protected static transient Vrand apicalRandomNumber;

    /** Constructor.*/
    public NeuronPyramidal() {
        super();
    }


    public static void deleteData() {
        NeuronBase.deleteData();
        //basalRandomNumber = null;
        apicalRandomNumber = null;
        //Axon.setDrawNumber(null);
        //drawNumber = null;
    }

    @Override
    public PyramidalParam getParam() {
        return PyramidalParam.getInstance();
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
        Point3f axonStart = new Point3f(somaMid);
        Point3f axonEnd = new Point3f(somaMid);

        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = -1;
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;
        //logger.info("set axon");
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
