package org.neugen.datastructures.neuron;

import java.io.Serializable;
import org.neugen.gui.Trigger;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.utils.Vrand;

/**
 * created on: 23.06.2010
 * @author Sergei Wolf
 *
 *
 * Parvalbumin (PV) cells are chandelier or basket cells of the hippocampus.
 * 10% of all PV cells are bistratified
 *
 */
public final class NeuronParvalbumin extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        private static Param instance;

        /** Constructs contained parameters. */
        private Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new Param(ParameterConstants.SUFFIX_PATH_PARVALBUMIN);
                param.setBasalParam(ParameterConstants.LAST_KEY_DENDRITE);
                setInstance(param);
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

        @Override
        public String toString() {
            return super.toString();
        }
    }
    private static final long serialVersionUID = -6131922269185554614L;

    /** Constructor.  */
    public NeuronParvalbumin() {
        super();
        type = DataStructureConstants.PV_PARVALBUMIN;
        if (basalRandomNumber == null) {
            //logger.info("calretinin set basal random number");
            basalRandomNumber = new Vrand(getParam().getDendriteParam().getSeedValue());
        }
        if (drawNumber == null) {
            drawNumber = new Vrand(getParam().getSeedValue());
        }
    }

    @Override
    public Param getParam() {
        return Param.getInstance();
    }

    /**
     * Function for setting a Parvalbumin interneuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        Param param = getParam();

        String mes = "set for " + getType() + " neuron";
        logger.info(mes);
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

       
        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonStart = new Point3f(somaMid);
        Point3f axonEnd = new Point3f(somaMid);
        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(param.getDeviation().getX(), param.getDeviation().getY(), param.getDeviation().getZ());
        deviation.scale(somaRadius);

        int up_down =  -1;
        AxonParam axonParam = param.getAxonParam();
        axonEnd.x += axonParam.getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += axonParam.getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * axonParam.getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;

        axon.set(axonStart, axonEnd, axonParam);
        //soma_x[d - 1] += soma_radius;
       
        float scale;
        boolean down;
        Dendrite dendrite;
        for (int i = 0; i < param.getNumberOfDendrites(); ++i) {
            //logger.info("draw: " + drawNumber.draw());
            dendrite = new Dendrite();
            dendrite.setDrawNumber(basalRandomNumber);
            if (i < param.getNumberOfDendrites() / 2) {
                down = false;
                scale = 1.0f;
                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            } else {
                down = true;
                scale = 0.5f;
                //dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, true);
                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            }
            //dendrite.setDendrite(calretininPar.getDendriteParam(), soma, deviation, true);
            dendrites.add(dendrite);
        }

    }
}
