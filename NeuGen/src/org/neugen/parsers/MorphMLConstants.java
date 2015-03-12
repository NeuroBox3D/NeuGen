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
package org.neugen.parsers;

/**
 * @author Sergei Wolf
 */
public final class MorphMLConstants {

    public static final String ROOT_ELEMENT = "morphml";
    public static final String NAMESPACE_URI = "http://morphml.org/morphml/schema";
    public static final String PREFIX = "mml";
    public static final String DEFAULT_SCHEMA_FILENAME = "MorphML.xsd";
    public static final String CELLS_ELEMENT = "cells";
    public static final String CELL_ELEMENT = "cell";
    public static final String CELL_NAME_ATTR = "name";
    public static final String SEGMENTS_ELEMENT = "segments";
    public static final String SEGMENT_ELEMENT = "segment";
    public static final String SEGMENT_ID_ATTR = "id";
    public static final String SEGMENT_NAME_ATTR = "name";
    public static final String SEGMENT_CABLE_ID_ATTR = "cable";
    public static final String SEGMENT_PARENT_ATTR = "parent";
    public static final String SEGMENT_PROXIMAL_ELEMENT = "proximal";
    public static final String SEGMENT_DISTAL_ELEMENT = "distal";
    public static final String POINT_X_ATTR = "x";
    public static final String POINT_Y_ATTR = "y";
    public static final String POINT_Z_ATTR = "z";
    public static final String POINT_DIAM_ATTR = "diameter";
    public static final String CABLES_ELEMENT = "cables";
    public static final String CABLE_ELEMENT = "cable";
    public static final String CABLE_GROUP_ELEMENT = "cablegroup";
    public static final String CABLE_GROUP_NAME = "name";
    public static final String CABLE_GROUP_ENTRY_ELEMENT = "cable";
    public static final String CABLE_ID_ATTR = "id";
    public static final String CABLE_NAME_ATTR = "name";
    public static final String SOMA_CABLE_GROUP = "soma_group";
    public static final String AXON_CABLE_GROUP = "axon_group";
    public static final String DENDRITE_CABLE_GROUP = "dendrite_group";
    public static final String PROPS_ELEMENT = "properties";
    public static final String COMMENT_PROP = "comment";
    public static final String FRACT_ALONG_PARENT_ATTR = "fract_along_parent";
    public static final String FRACT_ALONG_PARENT_ATTR_pre_v1_7_1 = "fractAlongParent";
    public static final String NUMBER_INTERNAL_DIVS_PROP = "numberInternalDivisions";
    public static final String FINITE_VOL_PROP = "finiteVolume";
}
