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
