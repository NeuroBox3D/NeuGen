/*
 * File: L5APyramidalNeuron.java
 * Created on 13.10.2009, 09:39:50
 *
 */
package org.neugen.datastructures.neuron;

import java.io.Serializable;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.utils.Vrand;

/**
 *
 * @author Jens Eberhard
 * @author Alexander Wanner
 *
 * Subclass for a L5A pyramidal neuron.
 *
 */
public final class NeuronL5APyramidal extends NeuronL5Pyramidal implements Serializable, Neuron {

    public static final class Param extends L5PyramidalParam {

        private static Param instance;

        /** Constructs contained parameters. */
        private Param(String lastKey) {
            super(L5PyramidalParam.getInstance(), lastKey);
            if (instance != null) {
                throw new IllegalStateException("Already instantiated");
            }
        }

        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        /** Returns instance. */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new Param(ParameterConstants.SUFFIX_PATH_L5APYRAMIDAL_PARAM);
                param.setApicalParam(ParameterConstants.LAST_KEY_APICAL);
                param.setBasalParam(ParameterConstants.LAST_KEY_BASAL);
                Param.setInstance(param);
            }
            return instance;
        }
    }
    private static final long serialVersionUID = -8689448816398031243L;

    /** Constructor. */
    public NeuronL5APyramidal() {
        super();
        type = DataStructureConstants.L5A_PYRAMIDAL;
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
    public Param getParam() {
        return Param.getInstance();
    }
}
