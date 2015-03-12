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
package org.neugen.datastructures;

import java.io.Serializable;
import javax.vecmath.Point3f;


/**
 * @author Sergei Wolf
 */
public class CellBase implements Cell, Serializable {

    private static final long serialVersionUID = -4623546484283033478L;
    /** The name of the cell. */
    protected String name;
    /** The type of cell. */
    protected String type;
    /** The soma of neuron. */
    protected final Cellipsoid soma;

    public CellBase() {
        soma = new Cellipsoid();
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param instanceName new value of name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    /** Return type name of the neuron. */
    public String getType() {
        return type;
    }

    /**
     * Get the value of soma
     *
     * @return the value of soma
     */
    @Override
    public Cellipsoid getSoma() {
        return soma;
    }

    /**
     * Define a neuron. It defines its axon with its radius and number of compartments,
     * and calls the function setNeuron.
     * @param somaMid new mid point of soma.
     * @param somaRadius new radius of soma.
     */
    @Override
    public void setSoma(Point3f somaMid, float somaRadius) {
        soma.setCellipsoid(somaMid, somaRadius);
    }
}
