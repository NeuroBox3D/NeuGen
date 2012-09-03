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
