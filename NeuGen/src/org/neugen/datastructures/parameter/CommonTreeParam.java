/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2012-2015 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
/*
 * File: CommonTreeParam.java
 * Created on 20.08.2009, 15:29:34
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParserContainer;
import org.neugen.datastructures.parameter.SubCommonTreeParam.GNSubCommonTreeParam;

class TreeStaticParam extends KeyIdentificable {

    protected final Parameter<Float> val;
    protected final Parameter<Float> vel;
    protected final Parameter<Integer> seed;

    /**
     * Initializes val, vel and seed with values from the InternaParser.
     *  @param container is the object "containing" (s. KeyIdentificable) this object.
     */
    public TreeStaticParam(KeyIdentificable container, String key) {
        super(container, key);
        val = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "val");
        vel = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "vel");
        seed = new Parameter<Integer>(ConfigParserContainer.getInternaParser(), this, "seed");
    }

    public int getSeedValue() {
        return seed.getValue().intValue();
    }

    public float getValValue() {
        return val.getValue().floatValue();
    }

    public float getVelValue() {
        return vel.getValue().floatValue();
    }
}

/**
 * Common tree parameter.
 * @author Alexander Wanner
 */
public class CommonTreeParam extends TreeStaticParam {

    /** min, max radius of the dendrite or axon. */
    protected final MinMaxParameter<Float> rad;
    /** Parameter for substring of the first "generation" */
    protected final GNSubCommonTreeParam gen_0;
    protected final Parameter<Float> a;
    protected final Parameter<Float> c;

    /**
     * Constructs contained parameters with given container.
     * @param container is the object containing this object.
     */
    public CommonTreeParam(KeyIdentificable container, String key) {
        super(container, key);
        rad = new MinMaxParameter<Float>(ConfigParserContainer.getParamParser(), this, "rad");
        a = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "a");
        c = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "c");
        gen_0 = new GNSubCommonTreeParam(this);
    }

    /**
     * Get the value of Rall's power.
     *
     * @return the value of Rall's power.
     */
    public float getA() {
        return a.getValue().floatValue();
    }

    /**
     * Get the threshold of the radius of Rall's power rule.
     *
     * @return the threshold of the radius of Rall's power rule.
     */
    public float getC() {
        return c.getValue().floatValue();
    }

    /**
     * Get the first generation of the neuronal structure tree.
     *
     * @return the first generation of the neuronal structure tree.
     */
    public GNSubCommonTreeParam getFirstGen() {
        return gen_0;
    }

    public MinMaxParameter<Float> getRad() {
        return rad;
    }

    /**
     * @return true exactly if all container parameter are valid.
     */
    public boolean isValid() {
        return val.isValid()
                && vel.isValid()
                && seed.isValid()
                && rad.isValid()
                && gen_0.isValid();
    }

    /**
     * Returns string representation of the neuronal tree.
     * 
     * @return ret The string representation of siblings.
     */
    @Override
    public String toString() {
        String ret = "val = " + val.toString()
                + " vel = " + vel.toString()
                + " seed = " + seed.toString()
                + " rad = " + rad.toString()
                + "\n generations of dendrite strings:"
                + "\n\t0: " + gen_0.toString()
                + "\n\t1: " + gen_0.getSiblings().toString()
                + "\n\t2: " + gen_0.getSiblings().getSiblings().toString()
                + "\n\t3: " + gen_0.getSiblings().getSiblings().getSiblings().toString()
                + "\n\t4: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n\t5: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n\t6: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n\t7: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n\t8: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n\t9: " + gen_0.getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().getSiblings().toString()
                + "\n ";
        return ret;
    }
}
