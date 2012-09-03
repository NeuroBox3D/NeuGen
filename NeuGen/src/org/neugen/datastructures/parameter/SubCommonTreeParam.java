/*
 * File: SubdendriteParam.java
 * Created on 21.08.2009, 11:16:20
 *
 */
package org.neugen.datastructures.parameter;

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
    protected final Parameter<Float> nparts_density;
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
