package org.neugen.datastructures.neuron;

import org.apache.log4j.Logger;

/**
 * @author sergeiwolf
 */
public class NeuronCA3Pyramidal extends NeuronPyramidal {

    private static final long serialVersionUID = 45755634634L;

    public static class Param extends PyramidalParam {

        /** use to log messages */
        private static final Logger logger = Logger.getLogger(Param.class.getName());
        private static Param instance;

        public Param(String lastKey) {
            super(PyramidalParam.getInstance(), lastKey);
        }

        public static Param getInstance() {
            return instance;
        }

        public static void setInstance(Param instance) {
            Param.instance = instance;
        }
    }
}
