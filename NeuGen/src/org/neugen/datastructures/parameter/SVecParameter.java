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
 * File: SVecParameter.java
 * Created on 17.07.2009, 12:33:35
 *
 */
package org.neugen.datastructures.parameter;

import org.neugen.parsers.ConfigParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class for spatial vector parameter.
 *
 * @author Alexander Wanner
 */
public class SVecParameter<T> extends KeyIdentificable {

    /** use to log messages */
    protected static final Logger svParLog = Logger.getLogger(SVecParameter.class.getName());
    /**  Value of this parameter. */
    protected List<T> value;
    /** Validation. True exactly if value successfully loaded. */
    protected boolean valid;
    /** Pointer to last used parser. One parser per value field (x,y,z). */
    protected ConfigParser parser, parser1, parser2;

    /**
     * Constructor to load the parameter.
     * @param parSource is the ConfigParser to get keyed value from.
     * @container is the pointer to the object the parameter contained in.
     */
    public SVecParameter(ConfigParser parSource, KeyIdentificable container,
            String lastKey, ConfigParser parSource1, ConfigParser parSource2) {
        super(container, lastKey);
        parser = parSource;
        if (parSource1 == null) {
            parser1 = parser;
        }
        if (parSource2 == null) {
            parser2 = parser1;
        }
        value = new ArrayList<T>(3);
        init();
    }

    /** returns string representation of this object. */
    @Override
    public String toString() {
        String oss;
        if (!isValid()) {
            oss = this.getFullKey() + ":\n" + "invalid!\n";
        } else {
            oss = "(" + value.get(0) + "," + value.get(1) + "," + value.get(2) + ")";
        }
        return oss;
    }

    /** Returns true exactly if parameter could be loaded. */
    public boolean isValid() {
        return valid;
    }

    public T getX() {
        if(isValid()) {
            return value.get(0);
        } else {
            return null;
        }
    }

    public T getY() {
        if (isValid()) {
            return value.get(1);
        } else {
            return null;
        }
    }

    public T getZ() {
        if (isValid()) {
            return value.get(2);
        } else {
            return null;
        }
    }

    /** Tries to read parameter value. */
    @SuppressWarnings("unchecked")
    public boolean init() {
        if (parser == null) {
            return valid;
        }
        value.clear();
        for (int i = 0; i < 3; i++) {
            value.add(null);
        }
        StringBuffer path = getFullKey();
        int start, end;
        start = path.length();
        path.append("/x");
        if (!parser.get(path)) {
            if (svParLog.isDebugEnabled()) {
                svParLog.debug(path + " not found in " + parser.getPath() + "\n");
                if (!valid) {
                    return false;
                }
            }
        } else {
            value.set(0, (T) parser.getValue());
        }
        end = path.length();
        path.delete(start, end);
        path.append("/y");
        //System.out.println("path to y; " + path);
        if (!parser1.get(path)) {
            if (svParLog.isDebugEnabled()) {
                svParLog.debug(path + " not found in " + parser1.getPath() + "\n");
                if (!valid) {
                    return false;
                }
            }
        } else {
            value.set(1, (T) parser1.getValue());
        }
        end = path.length();
        path.delete(start, end);
        path.append("/z");
        if (!parser2.get(path)) {
            if (svParLog.isDebugEnabled()) {
                svParLog.debug(path + " not found in " + parser2.getPath() + "\n");
                if (!valid) {
                    return false;
                }
            }
        } else {
            value.set(2, (T) parser2.getValue());
        }
        valid = true;
        return valid;
    }
}
