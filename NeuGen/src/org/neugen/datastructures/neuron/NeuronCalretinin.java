package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 * 24.06.2010
 * @author Sergei Wolf
 */
public final class NeuronCalretinin extends NeuronBase implements Serializable, Neuron {

    public static class Param extends NeuronParam {

        protected static Param instance;

        public Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param crParam = new Param(ParameterConstants.SUFFIX_PATH_CR);
                crParam.setBasalParam(ParameterConstants.LAST_KEY_DENDRITE);
                setInstance(crParam);
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
    private static final long serialVersionUID = -7031942369184524614L;

    /** Constructor.  */
    public NeuronCalretinin() {
        super();
        type = DataStructureConstants.CR_CALRETININ;
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
     * Function for setting a calretinin neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        Param param = getParam();
        String mes = "set for " + getType() + " neuron";
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);
        logger.info(mes);


        Region.Param.CA1Param ca1RegionParam = Region.Param.getInstance().getCa1Param();

        float strOriensHeight = ca1RegionParam.getStratumOriens();
        float strPyramidaleHeight = ca1RegionParam.getStratumPyramidale();
        float strRadiatumHeight = ca1RegionParam.getStratumRadiatum();
        float strLacMolecHeight = ca1RegionParam.getStratumLacunosum();
        float regionToLacMolHight = strOriensHeight + strRadiatumHeight + strPyramidaleHeight;

        /*
        Point3f soma_x = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(soma_x);
        Point3f axonStart = new Point3f(soma_x);
        int up_down = drawNumber.fdraw() > 0.1 ? 1 : -1;
              
        AxonParam axonParam = param.getAxonParam();
        axonEnd.x += axonParam.getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += axonParam.getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = soma_x.z + up_down * axonParam.getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;
        axon.set(axonStart, axonParam, axonEnd);     
         */
        //soma_x[d - 1] += soma_radius;
        //logger.info("set dendirte (number of dendrites):" + param.getNumberOfDendrites());


        Point3f somaMid = new Point3f(soma.getMid());

        float hightToStrDistalRadiatum = strOriensHeight + strPyramidaleHeight + strRadiatumHeight/2.0f;
        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(param.getDeviation().getX(), param.getDeviation().getY(), param.getDeviation().getZ());
        deviation.scale(somaRadius);

        float scale;
        boolean down;
        Dendrite dendrite;
        for (int i = 0; i < param.getNumberOfDendrites(); ++i) {
            //logger.info("draw: " + drawNumber.draw());
            dendrite = new Dendrite();
            dendrite.setDrawNumber(basalRandomNumber);
            if (i < param.getNumberOfDendrites()/2) {
                down = false;
                if(somaMid.z >= hightToStrDistalRadiatum) {
                    scale = 0.5f;
                } else {
                   scale = 1.0f;
                }

                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            } else {
                down = true;
                if (somaMid.z >= hightToStrDistalRadiatum) {
                    scale = 1.0f;
                } else {
                    scale = 0.5f;
                }
                //dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, true);
                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            }
            //dendrite.setDendrite(calretininPar.getDendriteParam(), soma, deviation, true);
            dendrites.add(dendrite);
        }
    }
}
