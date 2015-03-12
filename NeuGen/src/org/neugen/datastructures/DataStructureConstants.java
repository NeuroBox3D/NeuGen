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

/**
 * This class contains constans for datastructures.
 * @author Sergei Wolf
 *
 * Created on 16.07.2009, 12:22:21
 */
public final class DataStructureConstants implements Serializable {

    private static final long serialVersionUID = -5131930069184524614L;
    /** constants for neocortex */
    public static final String STAR_PYRAMIDAL = "L4 starpyramidal";
    public static final String L4_STELLATE = "L4 stellate";
    public static final String L23_PYRAMIDAL = "L23 pyramidal";
    public static final String L5_PYRAMIDAL = "L5 pyramidal";
    public static final String L5A_PYRAMIDAL = "L5A pyramidal";
    public static final String L5B_PYRAMIDAL = "L5B pyramidal";
    public static final String PYRAMIDAL = "pyramidal";
    /** constants for hippocampus */
    // pyramidal cells
    public static final String CA1_PYRAMIDAL = "CA1 pyramidal";
    public static final String CA2_PYRAMIDAL = "CA2 pyramidal";
    public static final String CA3_PYRAMIDAL = "CA3 pyramidal";

    //interneurons
    public static final String CR_CALRETININ = "Calretinin (CR)";
    public static final String CB_CALBINDIN = "Calbindin (CB)";
    public static final String CCK_CHOLECYSTOKININ = "Cholecystokinin (CCK)";
    public static final String PV_PARVALBUMIN = "Parvalbumin (PV)";
    public static final String SOM_SOMATOSTATIN = "Somatostatin (SOM)";

    /** xml datastructure constants */
    public static final String XED = "xed";
    public static final String STRING = "string";
    public static final String REAL = "real";
    public static final String INT = "int";
    public static final String BOOL = "bool";
    public static final String OBJECT = "object";
    public static final String KEY = "key";
    public static final String CLASSDESCRIPTOR = "classdescriptor";
    public static final String MAJOR = "major";
    public static final String MINOR = "minor";
    public static final String XML_BOOL_TYPE = "XMLBool";
    public static final String XML_INT_TYPE = "XMLInt";
    public static final String XML_REAL_TYPE = "XMLReal";
    public static final String XML_STRING_TYPE = "XMLString";
    public static final String XML_NODE_TYPE = "XMLNode";
    public static final String ALIAS_TYPE = "alias";
    public static final String SIBLINGS_TYPE = "siblings";

    public static final String ALL = "all";
    public static final String SOMA_GROUP = "soma_group";
    public static final String DENDRITIC_GROUP = "dendrite_group";
    public static final String AXONAL_GROUP = "axon_group";
    public static final Character NG_PATH_SEPARATOR = '/';
}
