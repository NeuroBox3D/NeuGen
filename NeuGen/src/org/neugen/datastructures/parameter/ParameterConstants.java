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
package org.neugen.datastructures.parameter;

/**
 * @author Sergei Wolf
 */
public final class ParameterConstants {

    public static final Character SEPARATOR = '/';
    public static final String LAST_KEY_SIBLINGS = "siblings";
    public static final String LAST_KEY_SUBCLASSES = "subclasses";
    public static final String LAST_KEY_APICAL = "apical";
    public static final String LAST_KEY_BASAL = "basal";
    public static final String LAST_KEY_DENDRITE = "dendrite";
    public static final String LAST_KEY_NEURON = "neuron";
    public static final String LAST_KEY_NET = "net";
    public static final String SUFFIX_PATH_APICAL = LAST_KEY_DENDRITE + SEPARATOR + LAST_KEY_APICAL;
    public static final String SUFFIX_PATH_BASAL = LAST_KEY_DENDRITE + SEPARATOR + LAST_KEY_BASAL;
    public static final String PATH_SIBLINGS = LAST_KEY_SIBLINGS + SEPARATOR + LAST_KEY_SIBLINGS;
    // Neocortex paths
    public static final String LAST_KEY_PYRAMIDAL = "pyramidal";
    public static final String LAST_KEY_L5PYRAMIDAL = "L5pyramidal";
    public static final String LAST_KEY_L5APYRAMIDAL = "L5Apyramidal";
    public static final String LAST_KEY_L5BPYRAMIDAL = "L5Bpyramidal";
    public static final String LAST_KEY_L4STELLATE = "L4stellate";
    public static final String LAST_KEY_STARPYRAMIDAL = "starpyramidal";
    public static final String LAST_KEY_L23PYRAMIDAL = "L23pyramidal";
    public static final String SUFFIX_PATH_PYRAMIDAL_PARAM = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_PYRAMIDAL;
    public static final String SUFFIX_PATH_L5PYRAMIDAL_PARAM = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_L5PYRAMIDAL;
    public static final String SUFFIX_PATH_L5APYRAMIDAL_PARAM = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_L5APYRAMIDAL;
    public static final String SUFFIX_PATH_L5BPYRAMIDAL_PARAM = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_L5BPYRAMIDAL;
    public static final String SUFFIX_PATH_L4STELLATE = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_L4STELLATE;
    public static final String SUFFIX_PATH_STARPYRAMIDAL = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_STARPYRAMIDAL;
    public static final String SUFFIX_PATH_L23PYRAMIDAL = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_L23PYRAMIDAL;
    // Hippocampus paths
    public static final String LAST_KEY_CA1PYRAMIDAL = "CA1pyramidal";
    public static final String LAST_KEY_PV = "parvalbumin";
    public static final String LAST_KEY_CB = "calbindin";
    public static final String LAST_KEY_CR = "calretinin";
    public static final String LAST_KEY_CCK = "cholecystokinin";
    public static final String LAST_KEY_SOM = "somatostatin";
    public static final String SUFFIX_PATH_CA1PYRAMIDAL = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_CA1PYRAMIDAL;
    public static final String SUFFIX_PATH_PARVALBUMIN = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_PV;
    public static final String SUFFIX_PATH_CB = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_CB;
    public static final String SUFFIX_PATH_CR = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_CR;
    public static final String SUFFIX_PATH_CCK = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_CCK;
    public static final String SUFFIX_PATH_SOM = LAST_KEY_SUBCLASSES + SEPARATOR + LAST_KEY_SOM;
}
