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
 * File: ObliqueParam.java
 * Created on 09.09.2009, 13:48:04
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import org.neugen.parsers.ConfigParserContainer;
import org.neugen.datastructures.parameter.SubCommonTreeParam.GNSubCommonTreeParam;

/**
 * @author Alexander Wanner
 */
public final class ObliqueParam extends KeyIdentificable {

    public final class BranchParam extends KeyIdentificable {

        /** min and max branching angles (degrees). */
        protected final MinMaxAngle angle;

        /** Constructs from given parser. */
        public BranchParam(ConfigParser parser, KeyIdentificable container) {
            super(container, "branch");
            angle = new MinMaxAngle(parser, this, "angle");
        }

        /** Initializes this branch parameter with given parser. */
        public boolean init(ConfigParser parser, KeyIdentificable container) {
            super.init(container, "branch");
            angle.init(parser, this, "angle");
            return angle.isValid();
        }

        /** Returns true exactly if parameter valid. */
        public boolean isValid() {
            return angle.isValid();
        }
    }
    /** Branching parameter. */
    public BranchParam branch;
    /** 0th generation of dendrite strings. */
    public GNSubCommonTreeParam gen_0;

    /** Constructs contained parameters with given container. */
    public ObliqueParam(KeyIdentificable container) {
        super(container, "oblique");
        branch = new BranchParam(ConfigParserContainer.getInternaParser(), this);
        gen_0 = new GNSubCommonTreeParam(this);
    }

    /** Returns true exactly if all container parameter are valid. */
    public boolean isValid() {
        return branch.isValid() && gen_0.isValid();
    }

    /** Returns string representation of the dendrite. */
    @Override
    public String toString() {
        String ret = key + ": validation = " + isValid() + "\nbranch.angle = " + branch.angle.toString()
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
                + "\n " + key + " end\n";
        return ret;
    }
}
