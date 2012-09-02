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
