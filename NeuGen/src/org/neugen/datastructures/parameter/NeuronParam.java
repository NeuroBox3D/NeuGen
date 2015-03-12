/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
package org.neugen.datastructures.parameter;

import org.apache.log4j.Logger;
import org.neugen.parsers.ConfigParserContainer;
import org.neugen.datastructures.parameter.DendriteParam.ApicalParam;


/**
 * Class to contain parameters for Neuron.
 * @author Alexander Wanner
 */
public class NeuronParam extends KeyIdentificable {

    public final class SomaParam extends KeyIdentificable {

        private final Parameter<Float> rad;
        private final Parameter<Float> val;
        private final Parameter<Float> vel;

        public SomaParam(KeyIdentificable container) {
            super(container, "soma");
            rad = new Parameter<Float>(ConfigParserContainer.getParamParser(), this, "rad");
            val = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "val");
            vel = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "vel");
        }

        public boolean isValid() {
            return rad.isValid() && val.isValid() && vel.isValid();
        }

        public float getRadValue() {
            return rad.getValue().floatValue();
        }

        public float getVal() {
            return val.getValue();
        }

        public float getVel() {
            return vel.getValue();
        }
    }

    public final class SynapseParam extends KeyIdentificable {

        public Parameter<Float> val;
        public Parameter<Float> vel;
        /** Radius (micrometer).*/
        public Parameter<Float> rad;

        /** Constructs contained parameters with given container.*/
        @SuppressWarnings("unchecked")
        public SynapseParam(KeyIdentificable container) {
            super(container, "synapse");
            val = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "val");
            vel = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "vel");
            rad = new Parameter<Float>(ConfigParserContainer.getParamParser(), this, "rad");
        }

        /**
         * Returns true exactly if all container parameter are valid.
         *
         * @return the value of valid
         */
        public boolean isValid() {
            return val.isValid() & vel.isValid() & rad.isValid();
        }

        /** Returns string representation of the dendrite.*/
        @Override
        public String toString() {
            String ret = key + " parameter: validation =" + isValid() + "\n val = "
                    + val.toString() + " vel = " + vel.toString() + " rad = "
                    + rad.toString() + "\n" + key + " end\n";
            return ret;
        }
    }


    /** use to log messages */
    private static final Logger logger = Logger.getLogger(NeuronParam.class.getName());
    private static NeuronParam instance;
    protected final Parameter<Integer> seed, numDen, napiden;
    protected final SomaParam somaParam;
    protected final SVecParameter<Float> deviation;
    protected final AxonParam axonParam;
    protected DendriteParam.BasalParam dendriteParam;
    protected SynapseParam synapseParam;
    /** apical dendrite parameter. */
    protected DendriteParam.ApicalParam apicalParam;
    //private final static String LAST_KEY = ParameterConstants.LAST_KEY_NEURON;

    public NeuronParam(KeyIdentificable parent, String lastKey) {
        super(parent, lastKey);
        seed = new Parameter<Integer>(ConfigParserContainer.getInternaParser(), this, "seed");
        numDen = new Parameter<Integer>(ConfigParserContainer.getParamParser(), this, "nden");
        napiden = new Parameter<Integer>(ConfigParserContainer.getParamParser(), this, "napiden");
        somaParam = new SomaParam(this);
        deviation = new SVecParameter<Float>(ConfigParserContainer.getParamParser(), this, "deviation", null, null);
        axonParam = new AxonParam(this);
        synapseParam = new SynapseParam(this);
    }

    public SVecParameter<Float> getDeviation() {
        return deviation;
    }

    public void setBasalParam(String lastKey) {
        dendriteParam = new DendriteParam.BasalParam(this, lastKey);
    }

    public void setApicalParam(String lastKey) {
        apicalParam = new DendriteParam.ApicalParam(this, lastKey);

    }

    public ApicalParam getApicalParam() {
        return apicalParam;
    }

    public void setApicalParam(ApicalParam apicalParam) {
        this.apicalParam = apicalParam;
    }

    public AxonParam getAxonParam() {
        return axonParam;
    }

    public SomaParam getSomaParam() {
        return somaParam;
    }

    public SynapseParam getSynapseParam() {
        return synapseParam;
    }

    public static void setInstance(NeuronParam instance) {
        NeuronParam.instance = instance;
    }

    public static NeuronParam getInstance() {
        if (instance == null) {
            NeuronParam nParam = new NeuronParam(null, ParameterConstants.LAST_KEY_NEURON);
            nParam.setBasalParam(ParameterConstants.LAST_KEY_DENDRITE);
            NeuronParam.setInstance(nParam);
        }
        return instance;
    }

    public DendriteParam.BasalParam getDendriteParam() {
        return dendriteParam;
    }

    public boolean isValid() {
        return seed.isValid() && numDen.isValid() && napiden.isValid()
                && somaParam.isValid() && deviation.isValid()
                && axonParam.isValid() && dendriteParam.isValid()
                && synapseParam.isValid();
    }

    public int getSeedValue() {
        return seed.getValue().intValue();
    }

    public int getNumberOfDendrites() {
        return numDen.getValue().intValue();
    }

    public int getNumberOfApicalDendrites() {
        return napiden.getValue().intValue();
    }
}
