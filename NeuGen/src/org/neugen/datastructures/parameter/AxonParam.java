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
 * File: AxonParam.java
 * Created on 21.08.2009, 14:36:34
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import org.neugen.parsers.ConfigParserContainer;

/**
 * Axon Parameter
 *
 * @author Alexander Wanner
 * 
 */
public final class AxonParam extends CommonTreeParam {

    /** Axon hillock parameter class. */
    public final class Hillock extends KeyIdentificable {

        /** Axon hillock length. */
        private final Parameter<Float> length;
        /** Soma proximal radius */
        private final Parameter<Float> proximalRadius;

        public Hillock(ConfigParser parser, KeyIdentificable container) {
            super(container, "hillock");
            length = new Parameter<Float>(parser, this, "legth");
            proximalRadius = new Parameter<Float>(parser, this, "proximal_radius");
        }

        public boolean isValid() {
            return length.isValid() && proximalRadius.isValid();
        }

        public float getLength() {
            return length.getValue();
        }

        public float getProximalRadius() {
            return proximalRadius.getValue();
        }
    }

    /**
     * Initial segment parameter class.
     * Initial segment follows the axonal hillock.
     */
    public final class InitialSegment extends KeyIdentificable {

        private final Parameter<Float> length;

        public InitialSegment(ConfigParser parser, KeyIdentificable container) {
            super(container, "initial_segment");
            length = new Parameter<Float>(parser, this, "legth");
        }

        public float getLength() {
            return length.getValue() < 0 ? 0 : length.getValue();
        }

        public boolean isValid() {
            return length.isValid();
        }
    }

    /** Myelin parameter class. */
    public final class Myelin extends KeyIdentificable {

        private final Parameter<Float> myelinatedLegth;
        private final Parameter<Float> ranvierLegth;

        public Myelin(ConfigParser parser, KeyIdentificable container) {
            super(container, "myelin");
            myelinatedLegth = new Parameter<Float>(parser, this, "myelinated_legth");
            ranvierLegth = new Parameter<Float>(parser, this, "ranvier_legth");
        }

        public float getMyelinatedLegth() {
            return myelinatedLegth.getValue() < 0 ? 0 : myelinatedLegth.getValue();
        }

        public float getRanvierLegth() {
            return ranvierLegth.getValue() < 0 ? 0 : ranvierLegth.getValue();
        }

        public boolean isValid() {
            return myelinatedLegth.isValid() && ranvierLegth.isValid();
        }
    }
    /** Axonal hillock.  */
    private final Hillock hillock;
    /** Initial segment. */
    private final InitialSegment initialSegment;
    /** Description of myelinisation. */
    private final Myelin myelin;

    /**
     * Constructor.
     * @param container is other KeyIdentificable param.
     */
    public AxonParam(KeyIdentificable container) {
        super(container, "axon");
        hillock = new Hillock(ConfigParserContainer.getParamParser(), this);
        initialSegment = new InitialSegment(ConfigParserContainer.getParamParser(), this);
        myelin = new Myelin(ConfigParserContainer.getParamParser(), this);
    }

    public Hillock getHillock() {
        return hillock;
    }

    public InitialSegment getInitialSegment() {
        return initialSegment;
    }

    public Myelin getMyelin() {
        return myelin;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && hillock.isValid()
                && initialSegment.isValid()
                && myelin.isValid();
    }

    /** 
     * Returns representation string for the value in form [min,max]
     *
     * @return the representation string for the value in form [min,max].
     */
    @Override
    public String toString() {
        String ret = key + " parameter: validation = " + isValid() + "\n "
                + super.toString() + " hillock: len= " + hillock.length.toString()
                + " prox_rad= " + hillock.proximalRadius.toString() + "\n"
                + " initial segment: len=" + initialSegment.length.toString() + "\n"
                + " myelin: myelinated_length=" + myelin.myelinatedLegth.getValue()
                + " ranvier_legth=" + myelin.ranvierLegth.getValue() + "\n" + key + " end\n";
        return ret;
    }
}
