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
