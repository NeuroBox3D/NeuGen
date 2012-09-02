/*
 * Class for synaptic connections between two neurons of a net.
 */
package org.neugen.datastructures;

import org.neugen.datastructures.neuron.Neuron;
import java.io.Serializable;

/**
 * @author Jens Eberhard
 * @author Sergei Wolf
 */
public final class Cons implements Serializable {

    private static final long serialVersionUID = -3611946473283033478L;
    /** The source of the synapse. */
    /** The first neuron. */
    private final Neuron neuron1;
    /** The axonal section reference. */
    private final Section neuron1AxSection;
    /** The presynaptic segment in a section of fist neuron. */
    private final Segment neuron1AxSegment;
    /** The target of the synapse. */
    /** The second neuron. */
    private final Neuron neuron2;
    /** The dendrite section of second neuron. */
    private final Section neuron2DenSection;
    /** The segment within the section of second neuron. */
    private final Segment neuron2DenSectionSegment;
    /** Polygonal distance to postsynaptic soma. */
    private float dendriticSomaDistance;
    /** Polygonal distance to presynaptic soma. */
    private float axonalSomaDistance;

    public static class Builder {

        private Neuron neuron1;
        private Section neuron1AxSection;
        private final Segment neuron1AxSegment;
        /** The target of the synapse. */
        private Neuron neuron2;
        private Section neuron2DenSection;
        private final Segment neuron2DenSectionSegment;
        /** Polygonal distance to postsynaptic soma. */
        private float dendriticSomaDistance = -1;
        /** Polygonal distance to presynaptic soma. */
        private float axonalSomaDistance = -1;

        public Builder(Segment neuron1AxSegment, Segment neuron2DenSectionSegment) {
            this.neuron1AxSegment = neuron1AxSegment;
            this.neuron2DenSectionSegment = neuron2DenSectionSegment;
        }

        public Builder neuron1(Neuron neuron1) {
            this.neuron1 = neuron1;
            return this;
        }

        public Builder neuron2(Neuron neuron2) {
            this.neuron2 = neuron2;
            return this;
        }

        public Builder neuron1AxSection(Section neuron1AxSection) {
            this.neuron1AxSection = neuron1AxSection;
            return this;
        }

        public Builder neuron2DenSection(Section neuron2DenSection) {
            this.neuron2DenSection = neuron2DenSection;
            return this;
        }

        public Cons build() {
            return new Cons(this);
        }
    }

    private Cons(Builder builder) {
        this.neuron1 = builder.neuron1;
        this.neuron2 = builder.neuron2;
        this.neuron1AxSection = builder.neuron1AxSection;
        this.neuron2DenSection = builder.neuron2DenSection;
        this.neuron1AxSegment = builder.neuron1AxSegment;
        this.neuron2DenSectionSegment = builder.neuron2DenSectionSegment;
        this.axonalSomaDistance = builder.axonalSomaDistance;
        this.dendriticSomaDistance = builder.dendriticSomaDistance;
    }

    public Neuron getNeuron1() {
        return neuron1;
    }

    public Section getNeuron1AxSection() {
        return neuron1AxSection;
    }

    public Segment getNeuron1AxSegment() {
        return neuron1AxSegment;
    }

    public Neuron getNeuron2() {
        return neuron2;
    }

    public Section getNeuron2DenSection() {
        return neuron2DenSection;
    }

    public Segment getNeuron2DenSectionSegment() {
        return neuron2DenSectionSegment;
    }

    public float getAxonalSomaDistance() {
        return axonalSomaDistance;
    }

    public void setAxonalSomaDistance(float axonalSomaDistance) {
        this.axonalSomaDistance = axonalSomaDistance;
    }

    public float getDendriticSomaDistance() {
        return dendriticSomaDistance;
    }

    public void setDendriticSomaDistance(float dendriticSomaDistance) {
        this.dendriticSomaDistance = dendriticSomaDistance;
    }
}
