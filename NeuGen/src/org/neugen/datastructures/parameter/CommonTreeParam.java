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
