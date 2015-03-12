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
 * File: L4stellateNeuron.java
 * Created on 12.10.2009, 13:47:43
 *
 *
 * @date 10.05.2004
 *
 */
package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 * Subclass for a L4stellate neuron.
 * @author Jens Eberhard
 * @author Alexander Wanner
 */
public final class NeuronL4stellate extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        private static Param instance;

        /** Constructs contained parameters. */
        private Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
            if (instance != null) {
                throw new IllegalStateException("Already instantiated");
            }
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new NeuronL4stellate.Param(ParameterConstants.SUFFIX_PATH_L4STELLATE);
                param.setBasalParam(ParameterConstants.LAST_KEY_DENDRITE);
                Param.setInstance(param);
            }
            return instance;
        }

        /**
         * Set the value of instance
         *
         * @param instance new value of instance
         */
        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        /** Returns string representation of the dendrite. */
        @Override
        public String toString() {
            return super.toString();
        }
    }
    private static final long serialVersionUID = -7031931269184524614L;

    /** Constructor.  */
    public NeuronL4stellate() {
        super();
        type = DataStructureConstants.L4_STELLATE;
        if (basalRandomNumber == null) {
            basalRandomNumber = new Vrand(getParam().getDendriteParam().getSeedValue());
        }
        if (drawNumber == null) {
            drawNumber = new Vrand(getParam().getSeedValue());
        }
    }

    @Override
    public Param getParam() {
        return Param.getInstance();
    }

    /** 
     * Function for setting a L4stellate neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        String mes = "set for " + getType() + " neuron";
        //logger.info(mes);
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);

        int up_down = drawNumber.fdraw() > 0.1 ? 1 : -1;
        AxonParam axonParam = getParam().getAxonParam();
        axonEnd.x += axonParam.getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += axonParam.getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * axonParam.getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;

        soma.cylindricRepresentant();
        //logger.info("set axon of L4stellate");
        axon.set(axonStart, axonEnd, axonParam);
        //somaMid.z += somaRadius;
        logger.info("set dendirte (number of dendrites):" + getParam().getNumberOfDendrites());

        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            //logger.info("draw: " + drawNumber.draw());
            Dendrite dendrite = new Dendrite();
            dendrite.setDrawNumber(basalRandomNumber);
            dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, true);
            dendrites.add(dendrite);
        }
    }
}
