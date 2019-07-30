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
/*
 * File: MinMaxAngle.java
 * Created on 17.07.2009, 12:33:09
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import org.apache.log4j.Logger;
/**
 * @author Alexander Wanner
 */
public class MinMaxAngle<T> extends MinMaxParameter<T> {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(MinMaxAngle.class.getName());
    /** rad = grad2rad*grad */
    private final static float grad2rad = (float) (2.0f * Math.PI / 360.0f);
    //private static float grad2rad = 2;

    /** 
     * Constructor to load the parameter.
     * 
     * @param parSource is the ConfigParser to get keyed value from.
     * @container is the pointer to the object the parameter contained in.
     * Tries to read from parSource, where minmax angles have to be in grad.
     */
    @SuppressWarnings("unchecked")
    public MinMaxAngle(ConfigParser parSource, KeyIdentificable container, String lastKey) {
        super(parSource, container, lastKey);
        //logger.info("MinMaxAngle: full key: " + this.fullKey());
        if(value.size() > 0) {
            if (value.get(0) != null) {
                float tmpVal = (Float) value.get(0);
                //logger.info("MinMaxAngle before value1: " + tmpVal);
                float val = tmpVal * grad2rad;
                value.set(0, (T)  Float.valueOf(val));
                //value.set(0, val);
                //logger.info("MinMaxAngle value1: " + val);
            }
            if (value.get(1) != null) {
                float tmpVal = (Float) value.get(1);
                //logger.info("MinMaxAngle before value2: " + tmpVal);
                float val1 = tmpVal * grad2rad;
                //value.set(1, val1);
                value.set(1, (T) Float.valueOf(val1));
                //logger.info("MinMaxAngle value2: " + val1);
            }
        }
    }

    /** returns string representation of this object (grad). */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        String ret;
        float val = (Float) value.get(0) / grad2rad;
        float val1 = (Float) value.get(1) / grad2rad;
        ret = "[" + val + "," + val1 + "]";
        return ret;
    }

    /**
     * Tries to reinitialize with given parser, container and
     * last part of the full key. Calls init(). You can give NULL instead lastKey or
     * parSource if you don't want to change this attributes.
     */
    @Override
    public boolean init(ConfigParser parSource, KeyIdentificable container, String lastKey) {
        if (parSource != null) {
            this.parser = parSource;
        }
        super.init(container, lastKey);
        return initMinMaxAngle();
    }

    /** Tries to read parameter value. */
    @SuppressWarnings("unchecked")
    public boolean initMinMaxAngle() {
        //logger.info("init of minMaxAngle");
        if (parser == null) {
            return valid;
        }
        value.clear();
        for (int i = 0; i < 2; i++) {
            value.add(null);
        }
        //logger.info("MinMaxAngle init");
        StringBuffer path = getFullKey();
        int start, end;
        start = path.length();
        path.append("/min");
        // logger.info("minMaxAngle min path: " + path);
        if (!this.parser.get(path)) {
            if (logger.isDebugEnabled()) {
                logger.debug(path + " not found in " + this.parser.getPath());
                if (!valid) {
                    return false;
                }
            }
        } else {
            float val = (Float) parser.getValue() * grad2rad;
            //value.set(0, val);
            value.set(0, (T) Float.valueOf(val));
        }
        end = path.length();
        path.delete(start, end);
        path.append("/max");
        //  logger.info("minMaxAngle max path: " + path);
        if (!parser.get(path)) {
            if (logger.isDebugEnabled()) {
                logger.debug(path + " not found in " + this.parser.getPath());
                if (!this.valid) {
                    return false;
                }
            }
        } else {
            //logger.info("MinMaxAnlge: " + this.value.get(1).toString());
            float val = (Float) parser.getValue() * grad2rad;
            //value.set(1, val);
            value.set(1, (T) Float.valueOf(val));
        }
        return true;
    }
}
