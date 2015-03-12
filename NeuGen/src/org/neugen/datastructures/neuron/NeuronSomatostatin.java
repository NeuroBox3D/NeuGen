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
import org.neugen.datastructures.DataStructureConstants;
import org.neugen.datastructures.Dendrite;
import org.neugen.datastructures.Region;
import org.neugen.datastructures.parameter.AxonParam;
import org.neugen.datastructures.parameter.NeuronParam;
import org.neugen.datastructures.parameter.ParameterConstants;
import org.neugen.gui.Trigger;
import org.neugen.utils.Vrand;

/**
 *
 * @author Sergei Wolf
 */
public final class NeuronSomatostatin extends NeuronBase implements Serializable, Neuron {

    public static final class Param extends NeuronParam {

        private static Param instance;

        private Param(String lastKey) {
            super(NeuronParam.getInstance(), lastKey);
        }

        /**
         * Get the value of instance
         *
         * @return the value of instance
         */
        public static Param getInstance() {
            if (instance == null) {
                Param param = new Param(ParameterConstants.SUFFIX_PATH_SOM);
                param.setBasalParam(ParameterConstants.LAST_KEY_DENDRITE);
                setInstance(param);
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
    }
    private static final long serialVersionUID = -3331922269185554614L;

    /** Constructor.  */
    public NeuronSomatostatin() {
        super();
        type = DataStructureConstants.SOM_SOMATOSTATIN;
        if (basalRandomNumber == null) {
            //logger.info("calretinin set basal random number");
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
     * Function for setting a Somatostatin neuron.
     * It sets the axon and creates the dendrites.
     */
    @Override
    public void setNeuron() {
        Param param = getParam();
        String mes = "set for " + getType() + " neuron";
        logger.info(mes);
        Trigger trigger = Trigger.getInstance();
        trigger.outPrintln();
        trigger.outPrintln(mes);

        
        Point3f somaMid = new Point3f(soma.getMid());
        Point3f axonStart = new Point3f(somaMid);
        Point3f axonEnd = new Point3f(somaMid);
        float somaRadius = ((Float) soma.getMeanRadius()).floatValue();
        Vector3f deviation = new Vector3f(param.getDeviation().getX(), param.getDeviation().getY(), param.getDeviation().getZ());
        deviation.scale(somaRadius);

        Region.Param.CA1Param ca1RegionParam = Region.Param.getInstance().getCa1Param();

        float strOriensHeight = ca1RegionParam.getStratumOriens();
        float strPyramidaleHeight = ca1RegionParam.getStratumPyramidale();
        float strRadiatumHeight = ca1RegionParam.getStratumRadiatum();
        float strLacMolecHeight = ca1RegionParam.getStratumLacunosum();
        float regionToLacMolHight = strOriensHeight + strRadiatumHeight + strPyramidaleHeight;



        logger.info("hight to lac mol: " + regionToLacMolHight);
        //int up_down = drawNumber.fdraw() > 0.1 ? 1 : -1;
        int up_down = 1;
        AxonParam axonParam = param.getAxonParam();


        /*
        float axonLengthX = axonParam.getFirstGen().getLenParam().getX();
        float axonLengthY = axonParam.getFirstGen().getLenParam().getY();
        float axonLengthZ = axonParam.getFirstGen().getLenParam().getZ();

        Vector3f lenVec = new Vector3f(axonLengthX, axonLengthY, axonLengthZ);
        float axonLen = lenVec.length();
        logger.info("länge des Axons: " + axonLen);
         *
         */

        axonEnd.x += axonParam.getFirstGen().getLenParam().getX() * drawNumber.fpm_onedraw();
        axonEnd.y += axonParam.getFirstGen().getLenParam().getY() * drawNumber.fpm_onedraw();
        axonEnd.z += up_down * axonParam.getFirstGen().getLenParam().getZ() * (drawNumber.fdraw() + 0.5f);

        /*
        logger.info("axon end x: " + axonEnd.x);
        logger.info("axon end y: " + axonEnd.y);
        logger.info("axon end z: " + axonEnd.z);
         * 
         */

        if(axonEnd.z < regionToLacMolHight) {
            axonEnd.z = regionToLacMolHight + (2 * strLacMolecHeight);
        }

        if(axonEnd.z > regionToLacMolHight + strLacMolecHeight) {
            axonEnd.z = regionToLacMolHight + (2 * strLacMolecHeight);
        }

        axonStart.z += up_down * somaRadius;

        axon.setBranchStart(1.1f);

        // die Axone sollen in der oberen Schicht bleiben! ende und anfang berechnen!

        axon.set(axonStart, axonEnd, axonParam);
        //soma_x[d - 1] += soma_radius;


        float scale;
        boolean down;
        Dendrite dendrite;
        for (int i = 0; i < param.getNumberOfDendrites(); ++i) {
            //logger.info("draw: " + drawNumber.draw());
            dendrite = new Dendrite();
            dendrite.setDrawNumber(basalRandomNumber);
            if (i < param.getNumberOfDendrites() / 2) {
                down = false;
                scale = 1.0f;
                dendrite.setHorizontalBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down, true);

            } else {
                down = true;
                scale = 1.0f;
                //dendrite.setDendrite(getParam().getDendriteParam(), soma, deviation, true);
                dendrite.setHorizontalBasalDendrite(param.getDendriteParam(), soma, deviation, scale, down, true);
            }
            //dendrite.setDendrite(calretininPar.getDendriteParam(), soma, deviation, true);
            dendrites.add(dendrite);
        }
    }
}
