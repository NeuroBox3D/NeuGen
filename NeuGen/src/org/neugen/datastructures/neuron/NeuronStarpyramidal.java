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
 * File: StarpyramidalNeuron.java
 * Created on 13.10.2009, 09:47:29
 *
 */
package org.neugen.datastructures.neuron;

import java.io.Serializable;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.parameter.DendriteParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.NeuGenConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 * @author Jens Eberhard
 * @author Simone Eberhard
 * @author Alexander Wanner
 */
public final class NeuronStarpyramidal extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        /** basal dendrite parameter */
        private final DendriteParam.BasalParam basal;
        private static Param instance;

        /** Constructs contained parameters. */
        private Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
            basal = dendriteParam;
        }

        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        /** 
         * Returns instance.
         *
         * @return Instance of StarpyramidalParam.
         */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new NeuronStarpyramidal.Param(ParameterConstants.SUFFIX_PATH_STARPYRAMIDAL);
                param.setApicalParam(ParameterConstants.SUFFIX_PATH_APICAL);
                param.setBasalParam(ParameterConstants.SUFFIX_PATH_BASAL);
                Param.setInstance(param);
                param.getApicalParam().setTopFluctuation(null);
            }
            return instance;
        }

        /** Returns true exactly if all container parameter are valid. */
        @Override
        public boolean isValid() {
            return super.isValid() && apicalParam.isValid();
        }

        /** 
         * Get the string representation of the dendrite.
         *
         * @return the string representation of the starpyramidal parameter.
         */
        @Override
        public String toString() {
            String ret = "\n" + this.key + " parameter: validation = " + isValid() + "\nseed = " + seed.getValue()
                    + "\n nden = " + numDen.getValue() + "\nnapiden = " + napiden.getValue()
                    + "\n deviation(each coord. in times of soma radius) = " + deviation.toString()
                    + "\n" + somaParam.toString() + axonParam.toString() + basal.toString() + apicalParam.toString()
                    + synapseParam.toString() + this.key + " end\n\n";
            return ret;
        }
    }
    private static final long serialVersionUID = -6469337816398031243L;

    private static Vrand basalRandomNumber;
    private static Vrand apicalRandomNumber;

    /** Constructor. */
    public NeuronStarpyramidal() {
        super();
        type = DataStructureConstants.STAR_PYRAMIDAL;
        if (basalRandomNumber == null) {
            basalRandomNumber = new Vrand(getParam().getDendriteParam().getSeedValue());
        }

        if (apicalRandomNumber == null) {
            apicalRandomNumber = new Vrand(getParam().getApicalParam().getSeedValue());
        }

        if (drawNumber == null) {
            drawNumber = new Vrand(getParam().getSeedValue());
        }
    }


    @Override
    public NeuronParam getParam() {
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
	if (NeuGenConstants.WITH_GUI) {
 	       Trigger trigger = Trigger.getInstance();
    	  	 trigger.outPrintln();
       		 trigger.outPrintln(mes);
	}

        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonEnd = new Point3f(somaMid);
        Point3f axonStart = new Point3f(somaMid);

        float somaRadius = soma.getMeanRadius();
        Vector3f deviation = new Vector3f(getParam().getDeviation().getX(), getParam().getDeviation().getY(), getParam().getDeviation().getZ());
        deviation.scale(somaRadius);
        int up_down = drawNumber.pm_onedraw();
        axonEnd.x += getParam().getAxonParam().getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += getParam().getAxonParam().getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z = somaMid.z + up_down * getParam().getAxonParam().getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);
        axonStart.z += up_down * somaRadius;

        soma.cylindricRepresentant();
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
