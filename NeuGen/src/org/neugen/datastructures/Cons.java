/* 
 * Copyright (c) 2005–2012 Goethe Center for Scientific Computing - Simulation and Modelling (G-CSC Frankfurt)
 * Copyright (c) 2007–2012 Goethe Center for Scientific Computing - Computational Neuroscience (G-CSC Frankfurt)
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
