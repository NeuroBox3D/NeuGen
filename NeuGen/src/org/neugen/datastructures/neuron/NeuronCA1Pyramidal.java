package org.neugen.datastructures.neuron;

import java.io.Serializable;
import org.neugen.gui.Trigger;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.utils.Vrand;

/**
 *
 * @author Sergei Wolf
 */
public final class NeuronCA1Pyramidal extends NeuronPyramidal implements Serializable, Neuron {

    private static final long serialVersionUID = 44645745634L;

    public static final class Param extends PyramidalParam {

        private static Param instance;

        private Param(String lastKey) {
            super(PyramidalParam.getInstance(), lastKey);
            if (instance != null) {
                throw new IllegalStateException("Already instantiated");
            }
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param ca1PyrParam = new Param(ParameterConstants.SUFFIX_PATH_CA1PYRAMIDAL);
                ca1PyrParam.setApicalParam(ParameterConstants.LAST_KEY_APICAL);
                ca1PyrParam.setBasalParam(ParameterConstants.LAST_KEY_BASAL);
                setInstance(ca1PyrParam);
            }
            return instance;
        }

        /**
         * Set the value of instance
         *
         * @param instance new value of instance
         */
        public static void setInstance(Param instance) {
            Param.instance = instance;
        }
    }

    //Constructor
    public NeuronCA1Pyramidal() {
        super();
        type = DataStructureConstants.CA1_PYRAMIDAL;
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

    /*
    public static void deleteData() {
        basalRandomNumber = null;
        apicalRandomNumber = null;
        drawNumber = null;
    }
     * 
     */

    @Override
    public Param getParam() {
        return Param.getInstance();
    }

    /**
     * Function for setting a pyramidal neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        String mes = "set for " + getType() + " neuron";
        logger.info("seed: " + drawNumber.randx);
        //logger.info(mes);
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        somaMid.z += somaRadius;
        logger.info("path: " + getParam().getFullKey().toString());

        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = -1;

        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);

        axonStart.z += up_down * somaRadius;
        //logger.info("set axon");
        axon.set(axonStart, axonEnd, getParam().getAxonParam());
        //logger.info("set dendirte");
        int npyramidaldendrite = getParam().getNumberOfApicalDendrites();

        float scale = 1.0f;
        boolean down = true;

        Dendrite dendrite;
        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            if (i < npyramidaldendrite) {
                dendrite = new Dendrite();
                dendrite.setDrawNumber(apicalRandomNumber);
                dendrite.setPyramidalDendrite(getParam().getApicalParam(), soma, deviation, getParam().getApicalParam().getNumOblique());
                dendrites.add(dendrite);
            } else {
                dendrite = new Dendrite();
                dendrite.setDrawNumber(basalRandomNumber);
                //dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, false);
                dendrite.setBasalDendrite(getParam().getDendriteParam(), soma, deviation, scale, down);
                dendrites.add(dendrite);
            }
        }
    }
}
