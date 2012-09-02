/*
 * File: DendriteParam.java
 * Created on 09.09.2009, 11:00:15
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParserContainer;

/**
 * Basic container for dendritic parameters.
 * @author Alexander Wanner
 * @author Sergei Wolf
 */
public class DendriteParam extends CommonTreeParam {

    public static final String LOW_BRANCHING_LIMIT = "low_branching_limit";
    public static final String TOP_FLUCTUATION = "top_fluctuation";

    public final static class BasalParam extends DendriteParam {

        /** low limit to branch non-obligue branches */
        protected final Parameter<Float> lowBranchingLimit;

        /** Constructs contained parameters with given container. */
        public BasalParam(KeyIdentificable container, String lastKey) {
            super(container, lastKey);
            lowBranchingLimit = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, LOW_BRANCHING_LIMIT);
        }


        public Parameter<Float> getLowBranchingLimit() {
            return lowBranchingLimit;
        }


        /**
         * @return true exactly if all container parameter are valid.
         */
        @Override
        public boolean isValid() {
            return super.isValid();
        }

        /** Returns string representation of the dendrite. */
        @Override
        public String toString() {
            String ret = this.key + " parameter: validation = " + this.isValid() + "\n"
                    + super.toString() + "\n" + this.key + " end\n";
            return ret;
        }
    }

    public final static class ApicalParam extends DendriteParam {

        /** low limit to branch non-obligue branches */
        protected final Parameter<Float> lowBranchingLimit;
        /** Number of oblique dendrites. */
        protected final Parameter<Integer> noblique;
        /**
         * random fluctuation of the topmost limit for the dendrite (not over column top)
         * 1-top_fluctuation<=(soma[z]-top_limit[z]<=max())/gen0 len_param z<=1+top_fluctuation.
         */
        protected Parameter<Float> topFluctuation;
        /** Oblique branches parameter. */
        protected final ObliqueParam oblique;

        /**
         * Constructs contained parameters with given container.
         * @param container is the object containing this object.
         * It has to be a NeuronParam!
         */
        public ApicalParam(KeyIdentificable container, String lastKey) {
            super(container, lastKey);
            lowBranchingLimit = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, LOW_BRANCHING_LIMIT);
            noblique = new Parameter<Integer>(ConfigParserContainer.getParamParser(), this, "noblique");
            //logger.info(this.fullKey().toString());
            topFluctuation = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, TOP_FLUCTUATION);
            oblique = new ObliqueParam(this);
        }

        public int getNumOblique() {
            return noblique.getValue().intValue();
        }

        public ObliqueParam getOblique() {
            return oblique;
        }

        public float getTopFluctuationValue() {
            if (topFluctuation == null) {
                return 0f;
            } else {
                return topFluctuation.getValue();
            }
        }

        public void setTopFluctuation(Parameter<Float> topFluctuation) {
            this.topFluctuation = topFluctuation;
        }

        public Parameter<Float> getLowBranchingLimit() {
            return lowBranchingLimit;
        }

        /** Returns true exactly if all container parameter are valid. */
        @Override
        public boolean isValid() {
            return super.isValid()
                    && lowBranchingLimit.isValid()
                    && noblique.isValid()
                    && oblique.isValid()
                    && gen_0.isValid()
                    && topFluctuation.isValid();
        }

        /** Returns string representation of the dendrite. */
        @Override
        public String toString() {
            String ret = key + " parameter: validation = " + isValid() + "\n"
                    + super.toString() + "  noblique = " + noblique.toString() + "\n"
                    + "low_branching_limit = " + lowBranchingLimit.toString() + "\n"
                    + "top_fluctuation = " + topFluctuation.toString() + "\n"
                    + oblique.toString() + key + " end\n";
            return ret;
        }
    }

    /**
     * Parameter for the simple destribution of nonfunctional synapses.
     */
    public final class NFSSDParam extends KeyIdentificable {

        /** synapses per micrometer. */
        protected final Parameter<Float> density;
        /** min max distance for generated synapses. */
        protected final MinMaxParameter<Float> soma_distance;

        /** Constructs contained parameters with given container. */
        public NFSSDParam(KeyIdentificable container) {
            super(container, "simple_distr");
            density = new Parameter<Float>(ConfigParserContainer.getInternaParser(), this, "density");
            soma_distance = new MinMaxParameter<Float>(ConfigParserContainer.getInternaParser(), this, "soma_distance");
        }

        public float getDensity() {
            return density.getValue();
        }

        public MinMaxParameter<Float> getSomaDistance() {
            return soma_distance;
        }

        /** 
         * Returns true exactly if all container parameter are valid.
         *
         * @return true exactly if all container parameter are valid.
         */
        public boolean isValid() {
            return density.isValid() && soma_distance.isValid();
        }

        /** 
         * Returns string representation of the simple distribution of nonfunctional synapses.
         *
         * @return string representation of the simple distribution of nonfunctional synapses.
         */
        @Override
        public String toString() {
            String ret = "simple distribution of nonfunctional synapses parameter: validation = "
                    + isValid() + "\n density (1 per micrometer) = "
                    + density.toString() + "  soma_distance = "
                    + soma_distance.toString();
            return ret;
        }
    }

    /**
     * Parameter for nonfunctional synapses
     */
    public final class NFSParam extends KeyIdentificable {

        /** Parameter for a simple distribution of nonfunctional synapses. */
        protected final NFSSDParam simple_distr;

        /** Constructs contained parameters with given container. */
        public NFSParam(KeyIdentificable container) {
            super(container, "non_functional_synapses");
            simple_distr = new NFSSDParam(this);
        }

        public NFSSDParam getSimpleDistr() {
            return simple_distr;
        }

        /**
         * Initializes contained parameters with given container.
         *
         * @param container is the object containing this object.
         *
         * @return  true exactly if all inits were successfull.
         */
        @Override
        public boolean init(KeyIdentificable container, String last_key) {
            super.init(container, last_key);
            return simple_distr.init(this, null);
        }

        /**
         * Returns true exactly if all container parameter are valid.
         *
         * @return true exactly if all container parameter are valid.
         */
        public boolean isValid() {
            return simple_distr.isValid();
        }

        /**
         * Returns string representation of nonfunctional synapses.
         *
         * @return string representation of nonfunctional synapses.
         */
        @Override
        public String toString() {
            String ret = key + ": validation = " + isValid() + "\n" + simple_distr.toString() + "\n";
            return ret;
        }
    }
    /** nonfunctional synapse parameter. */
    protected final NFSParam non_functional_synapses;

    /**
     * Constructs contained parameters with given container.
     * @param container is the object containing this object.
     */
    public DendriteParam(KeyIdentificable container, String lastKey) {
        super(container, lastKey);
        non_functional_synapses = new NFSParam(this);
    }

    public NFSParam getNonFunctionalSynapses() {
        return non_functional_synapses;
    }

    /**
     *
     * @returns true exactly if all container parameter are valid.
     */
    @Override
    public boolean isValid() {
        return non_functional_synapses.isValid();
    }

    /**
     * Get the string representation of the neuronal structure.
     * 
     * @returns string representation of the dendrite.
     */
    @Override
    public String toString() {
        String ret = "val = " + val.toString()
                + " vel = " + vel.toString()
                + " seed = " + seed.toString()
                + " rad = " + rad.toString() + "\n"
                + non_functional_synapses.toString()
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
