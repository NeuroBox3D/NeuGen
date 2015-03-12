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
/*
 * File: MinMaxParameter.java
 * Created on 17.07.2009, 12:30:14
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Alexander Wanner
 */
public class MinMaxParameter<T> extends KeyIdentificable {

    /** use to log messages */
    private final static Logger logger = Logger.getLogger(MinMaxParameter.class.getName());
    /** Value of this parameter. */
    protected final List<T> value;
    /** Validation. True exactly if value successfully loaded. */
    protected boolean valid;
    /** Pointer to last used parser. */
    protected ConfigParser parser;

    /**
     * Constructor to load the parameter.
     *
     * @param parSource is the ConfigParser to get keyed value from.
     * @container is the pointer to the object the parameter contained in.
     * 
     */
    public MinMaxParameter(ConfigParser parSource, KeyIdentificable container, String lastKey) {
        super(container, lastKey);
        value = new ArrayList<T>(2);
        parser = parSource;
        valid = false;
        initMinMax();
    }

    /** returns string representation of this object */
    @Override
    public String toString() {
        String ret;
        if (!isValid()) {
            ret = this.getFullKey().toString() + ":\n" + "invalid!\n";
        } else {
            ret = "[" + value.get(0) + "," + value.get(1) + "]";
        }
        return ret;
    }

    /** returns parameters value. */
    public List<T> get() {
        return value;
    }

    /**
     * returns parameters min value.
     *
     * @return min value
     */
    public T getMin() {
        return value.get(0);
    }

    /**
     * returns parameters max value.
     *
     * @return max value
     */
    public T getMax() {
        return value.get(1);
    }

    /** Tries to read parameter value. */
    @SuppressWarnings("unchecked")
    private boolean initMinMax() {
        //logger.info("init begin");
        if (parser == null) {
            return valid;
        }
        value.clear();
        for (int i = 0; i < 2; i++) {
            value.add(null);
        }
        //logger.info("MinMaxParameter: init()");
        StringBuffer path = getFullKey();
        int start, end;
        start = path.length();
        //String path = fullKey().toString();
        //path += " min";
        path.append("/min");
        //logger.info("MinMaxParameter min path: " + path);
        if (!parser.get(path)) {
            if (logger.isDebugEnabled()) {
                logger.debug(path + " not found in " + parser.getPath() + "\n");
                if (!valid) {
                    return false;
                }
            }
        } else {
            value.set(0, (T) parser.getValue());
            //logger.info("first val: " + value.get(0));
        }
        //path = prefixPath;
        //path += " max";
        end = path.length();
        path.delete(start, end);
        path.append("/max");
        //logger.info("MinMaxParameter max path: " + path);
        if (!parser.get(path)) {
            if (logger.isDebugEnabled()) {
                logger.debug(path + " not found in " + parser.getPath() + "\n");
            }
        } else {
            value.set(1, (T) parser.getValue());
            //logger.info("sec val: " + value.get(1));
        }
        return true;
    }

    /**
     * Tries to reinitialize with given parser, container and
     * last part of the full key. Calls init(). You can give NULL instead lastKey or
     * parSource if you don't want to change this attributes.
     */
    public boolean init(ConfigParser parSource, KeyIdentificable container, String lastKey) {
        super.init(container, lastKey);
        if (parSource != null) {
            parser = parSource;
        }
        return initMinMax();
    }

    /** 
     * Returns true exactly if parameter could be loaded.
     *
     * @return true exactly if parameter could be loaded. 
     */
    public boolean isValid() {
        return valid;
    }
}
