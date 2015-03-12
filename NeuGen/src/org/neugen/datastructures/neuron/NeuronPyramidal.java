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
package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.Axon;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.KeyIdentificable;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 *
 * File: PyramidalNeuron.java
 * @author Jens Eberhard
 * @author Alexander Wanner
 * Created on 11.12.2009, 09:33:45
 */
public class NeuronPyramidal extends NeuronBase implements Serializable {

    /**
     * Class for cell parameter of pyramidal neuron.
     * @author Alexander Wanner
     */
    public static class PyramidalParam extends NeuronParam {

        /** basal dendrite parameter */
        public DendriteParam.BasalParam basal;
        private static PyramidalParam instance;

        /** Constructs contained parameters.  */
        public PyramidalParam(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
            basal = getDendriteParam();
        }

        public PyramidalParam(KeyIdentificable parent, String lastKey) {
            super(parent, lastKey);
        }

        public static void setInstance(PyramidalParam instance) {
            PyramidalParam.instance = instance;
        }

        /** Returns instance. */
        public static PyramidalParam getInstance() {
            if (instance == null) {
                PyramidalParam pyrParam = new NeuronPyramidal.PyramidalParam(ParameterConstants.SUFFIX_PATH_PYRAMIDAL_PARAM);
                pyrParam.setApicalParam(ParameterConstants.SUFFIX_PATH_APICAL);
                pyrParam.setBasalParam(ParameterConstants.SUFFIX_PATH_BASAL);
                PyramidalParam.setInstance(pyrParam);
            }
            return instance;
        }

        /** Returns true exactly if all container parameter are valid. */
        @Override
        public boolean isValid() {
            return super.isValid() && apicalParam.isValid();
        }

        /** Returns string representation of the dendrite. */
        @Override
        public String toString() {
            String ret = "\n" + this.key + " parameter: validation = " + isValid() + "\nseed = " + seed.getValue()
                    + "\nnden = " + numDen.getValue() + "\nnapiden = " + napiden.getValue()
                    + "\ndeviation(each coord. in times of soma radius) = " + deviation.toString()
                    + "\n" + somaParam.toString() + axonParam.toString() + basal.toString() + apicalParam.toString()
                    + synapseParam.toString() + this.key + " end\n\n";
            return ret;
        }
    }

    private static final long serialVersionUID = -8689337816398142243L;
    protected static transient Vrand apicalRandomNumber;

    /** Constructor.*/
    public NeuronPyramidal() {
        super();
    }


    public static void deleteData() {
        NeuronBase.deleteData();
        //basalRandomNumber = null;
        apicalRandomNumber = null;
        //Axon.setDrawNumber(null);
        //drawNumber = null;
    }

    @Override
    public PyramidalParam getParam() {
        return PyramidalParam.getInstance();
    }

    /**
     * Function for setting a pyramidal neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        String mes = "set for " + getType() + " neuron";
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonStart = new Point3f(somaMid);
        Point3f axonEnd = new Point3f(somaMid);

        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = -1;
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;
        //logger.info("set axon");
        axon.set(axonStart, axonEnd, getParam().getAxonParam());
        somaMid.z += somaRadius;
        //logger.info("set dendirte");
        int npyramidaldendrite = getParam().getNumberOfApicalDendrites();
        for (int i = 0; i < getParam().getNumberOfDendrites(); ++i) {
            if (i < npyramidaldendrite) {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(apicalRandomNumber);
                dendrite.setPyramidalDendrite(getParam().getApicalParam(), soma, deviation, getParam().getApicalParam().getNumOblique());
                dendrites.add(dendrite);
            } else {
                Dendrite dendrite = new Dendrite();
                dendrite.setDrawNumber(basalRandomNumber);
                dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, false);
                dendrites.add(dendrite);
            }
        }
    }
}
