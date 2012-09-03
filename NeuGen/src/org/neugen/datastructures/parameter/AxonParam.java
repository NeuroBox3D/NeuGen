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
