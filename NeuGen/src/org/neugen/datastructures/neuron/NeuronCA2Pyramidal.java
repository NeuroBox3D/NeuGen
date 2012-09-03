package org.neugen.datastructures.neuron;

import org.apache.log4j.Logger;

/**
 *
 * @author sergeiwolf
 *
 * TODO: 
 */
public final class NeuronCA2Pyramidal extends NeuronPyramidal {

    private static final long serialVersionUID = 44756634634L;

    public final static class Param extends PyramidalParam {

        /** use to log messages */
        private static final Logger logger = Logger.getLogger(Param.class.getName());
        private static Param instance;

        public static Param getInstance() {
            return instance;
        }

        public static void setInstance(Param instance) {
            Param.instance = instance;
        }

        public Param(String lastKey) {
            super(PyramidalParam.getInstance(), lastKey);
        }
    }
}
