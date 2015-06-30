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
 * File: SubdendriteParam.java
 * Created on 21.08.2009, 11:16:20
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.gui.GlobalParameterDialog;
import org.neugen.gui.NeuGenView;
import org.neugen.parsers.ConfigParserContainer;

/**
 * Subcommontree parameter.
 * @author Alexander Wanner
 */
public class SubCommonTreeParam extends KeyIdentificable {

    /** Length parameter for the main string of the subdendrite. */
    protected final SVecParameter<Float> len_param;
    /** angle this subdendrite is brunching from its parent */
    protected final MinMaxAngle<Float> branch_angle;
    /** Nparts per micrometer. */
    protected Parameter<Float> nparts_density;
    /** parameter to scale number of brunches.er. */
    protected final Parameter<Integer> nbranch_param;
    protected final static String LAST_PATH_SUBDENDRITE = ParameterConstants.PATH_SIBLINGS;
    protected SubCommonTreeParam siblings;

    /** 
     * Constructs contained parameters with given container and key =
     * @param container.fullKey() +
     * @param last_key.
     */
    public SubCommonTreeParam(KeyIdentificable container, String last_key) {
        super(container, last_key);
        len_param = new SVecParameter<Float>(ConfigParserContainer.getParamParser(), this, "len_param", null, null);
        branch_angle = new MinMaxAngle<Float>(ConfigParserContainer.getParamParser(), this, "branch_angle");
        nparts_density = new Parameter<Float>(ConfigParserContainer.getParamParser(), this, "nparts_density");
        nbranch_param = new Parameter<Integer>(ConfigParserContainer.getParamParser(), this, "nbranch_param");
       
    }

    public MinMaxAngle<Float> getBranchAngle() {
        return branch_angle;
    }

    public SVecParameter<Float> getLenParam() {
        return len_param;
    }

    public float getNbranchParam() {
        return nbranch_param.getValue();
    }

      public float getNpartsDensity() {
        System.err.println("getNpartsDensity (from within SubCommonTree's getter!): " + nparts_density.getValue());
        return nparts_density.getValue();
    }

    /**
     * Get the value of sibling
     *
     * @return the value of sibling
     */
    public SubCommonTreeParam getSiblings() {
        return siblings;
    }

    /**
     * Set the value of sibling
     *
     * @param sibling new value of sibling
     */
    public void setSiblings(SubCommonTreeParam siblings) {
        this.siblings = siblings;
    }

    /** Returns true exactly if all container parameter are valid. */
    public boolean isValid() {
        return len_param.isValid()
                && branch_angle.isValid()
                && nparts_density.isValid()
                && nbranch_param.isValid()
                && siblings.isValid();
    }

    /** 
     * Returns string representation of the sub tree parameter.
     * 
     * @return string representation of the sub tree parameter.
     */
    @Override
    public String toString() {
        String ret = "len_param = " + len_param.toString()
                + " branch_angle = " + branch_angle.toString()
                + " nparts_density = " + nparts_density.toString()
                + " nbranch_param = " + nbranch_param.toString();
        return ret;
    }

    public static class GNSubCommonTreeParam extends SubCommonTreeParam {

        private final SubCommonTreeParam gen0Siblings = this;
        private final SubCommonTreeParam gen1Siblings;
        private final SubCommonTreeParam gen2Siblings;
        private final SubCommonTreeParam gen3Siblings;
        private final SubCommonTreeParam gen4Siblings;
        private final SubCommonTreeParam gen5Siblings;
        private final SubCommonTreeParam gen6Siblings;
        private final SubCommonTreeParam gen7Siblings;
        private final SubCommonTreeParam gen8Siblings;
        private final SubCommonTreeParam gen9Siblings;

        public GNSubCommonTreeParam(KeyIdentificable container) {
            super(container, "gen_0");
            gen1Siblings = new SubCommonTreeParam(gen0Siblings, LAST_PATH_SUBDENDRITE);
            gen2Siblings = new SubCommonTreeParam(gen1Siblings, LAST_PATH_SUBDENDRITE);
            gen3Siblings = new SubCommonTreeParam(gen2Siblings, LAST_PATH_SUBDENDRITE);
            gen4Siblings = new SubCommonTreeParam(gen3Siblings, LAST_PATH_SUBDENDRITE);
            gen5Siblings = new SubCommonTreeParam(gen4Siblings, LAST_PATH_SUBDENDRITE);
            gen6Siblings = new SubCommonTreeParam(gen5Siblings, LAST_PATH_SUBDENDRITE);
            gen7Siblings = new SubCommonTreeParam(gen6Siblings, LAST_PATH_SUBDENDRITE);
            gen8Siblings = new SubCommonTreeParam(gen7Siblings, LAST_PATH_SUBDENDRITE);
            gen9Siblings = new SubCommonTreeParam(gen8Siblings, LAST_PATH_SUBDENDRITE);
            setSiblings();
        }

        private void setSiblings() {
            gen0Siblings.setSiblings(gen1Siblings);
            gen1Siblings.setSiblings(gen2Siblings);
            gen2Siblings.setSiblings(gen3Siblings);
            gen3Siblings.setSiblings(gen4Siblings);
            gen4Siblings.setSiblings(gen5Siblings);
            gen5Siblings.setSiblings(gen6Siblings);
            gen6Siblings.setSiblings(gen7Siblings);
            gen7Siblings.setSiblings(gen8Siblings);
            gen8Siblings.setSiblings(gen9Siblings);
            gen9Siblings.setSiblings(null);
        }
    }
}
