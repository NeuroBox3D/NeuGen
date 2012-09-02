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
 *
 * @author Sergei Wolf
 *
 * CCK-immunoreactive cells are generally basket cells
 *
 */
public final class NeuronCholecystokinin extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        protected static Param instance;

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
                Param param = new Param(ParameterConstants.SUFFIX_PATH_CCK);
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
    }
    private static final long serialVersionUID = -4241931269184524614L;

    /** Constructor.  */
    public NeuronCholecystokinin() {
        super();
        type = DataStructureConstants.CCK_CHOLECYSTOKININ;
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
     * Function for setting a Cholecystokinin neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        Param param = getParam();
        String mes = "set for " + getType() + " interneuron";
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);
        logger.info(mes);

        
        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(param.getDeviation().getX(), param.getDeviation().getY(), param.getDeviation().getZ());
        deviation.scale(somaRadius);

        Region.Param.CA1Param ca1RegionParam = Region.Param.getInstance().getCa1Param();

        float strOriensHeight = ca1RegionParam.getStratumOriens();
        float strPyramidaleHeight = ca1RegionParam.getStratumPyramidale();
        float strRadiatumHeight = ca1RegionParam.getStratumRadiatum();
        float strLacMolecHeight = ca1RegionParam.getStratumLacunosum();
        float regionToLacMolHight = strOriensHeight + strRadiatumHeight + strPyramidaleHeight;

        float hightToStrDistalRadiatum = strOriensHeight + strPyramidaleHeight + strRadiatumHeight/2.0f;

        //int up_down = drawNumber.fdraw() > 0.1 ? 1 : -1;
        int up_down = -1;
        AxonParam axonParam = getParam().getAxonParam();
        axonEnd.x += axonParam.getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += axonParam.getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z += up_down * axonParam.getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;
        logger.info("set axon of Cholecystokinin");
        
        //soma_x[d - 1] += soma_radius;

        if(somaMid.z < hightToStrDistalRadiatum) {
            axon.set(axonStart, axonEnd, axonParam);
        }

        
        float scale;
        boolean down;
        Dendrite dendrite;
        for (int i = 0; i < param.getNumberOfDendrites(); ++i) {
            //logger.info("draw: " + drawNumber.draw());
            dendrite = new Dendrite();
            dendrite.setDrawNumber(basalRandomNumber);
            if (i < param.getNumberOfDendrites() / 2) {
                down = false;
                if(somaMid.z >= hightToStrDistalRadiatum) {
                    scale = 0.25f;
                } else {
                   scale = 1.0f;
                }
                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            } else {
                down = true;
                if (somaMid.z >= hightToStrDistalRadiatum) {
                    scale = 1.0f;
                } else {
                    scale = 0.25f;
                }
                //dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, true);
                dendrite.setBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down);
            }
            //dendrite.setDendrite(calretininPar.getDendriteParam(), soma, deviation, true);
            dendrites.add(dendrite);
        }
    }
}
