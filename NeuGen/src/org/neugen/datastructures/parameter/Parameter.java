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
 * File: Parameter.java
 * Created on 17.07.2009, 12:28:02
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import org.apache.log4j.Logger;

/**
 * Simple parameter classes.
 * @author Alexander Wanner
 */
public final class Parameter<Object> extends KeyIdentificable {

    /** use to log messages */
    private static final Logger parLogger = Logger.getLogger(Parameter.class.getName());
    /** Value of this parameter. */
    private Object value;
    /** Validation. True exactly if value successfully loaded. */
    private boolean valid;
    /** Pointer to last used parser. */
    private ConfigParser parser;

    /**
     * Constructor to load the parameter.
     * @param parSource is the ConfigParser to get keyed value from.
     * @container is the pointer to the object the parameter contained in.
     */
    public Parameter(ConfigParser parSource, KeyIdentificable container, String lastKey) {
        super(container, lastKey);
        parser = parSource;
        valid = false;
        if (parLogger.isDebugEnabled()) {
            parLogger.info("---------");
            parLogger.info("parent fullKey: " + container.getFullKey());
            parLogger.info("lastKey: " + lastKey);
            parLogger.info("this parameter fullKey: " + getFullKey());
        }
        init();
    }

    /** Initializes value by the saved parser. */
    @SuppressWarnings("unchecked")
    public boolean init() {
        if (parser == null) {
            return valid;
        }
        if (parLogger.isDebugEnabled()) {
            parLogger.info(getFullKey());
        }
        if (!(parser.get(getFullKey()))) {
            if (parLogger.isDebugEnabled()) {
                parLogger.info(getFullKey() + " not found in " + parser.getPath() + "\n");
            }
        } else {
            value = (Object) parser.getValue();
            valid = true;
        }
        return valid;
    }

    /** 
     * Returns parameters value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /** 
     * Returns true exactly if parameter could be loaded.
     *
     * @return the value of valid
     */
    public boolean isValid() {
        return valid;
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
        return init();
    }

    /**
     * Get the string-representation of the value.
     *
     * @return string-representation of the value.
     */
    @Override
    public String toString() {
        String oss = "";
        if (!isValid()) {
            oss = oss + this.getFullKey() + ":\n" + "invalid!\n";
        } else {
            oss = oss + value.toString();
        }
        return oss;
    }
}
