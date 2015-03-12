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
 * File: KeyIdentificable.java
 *
 * Created on 15.07.2009, 12:23:11
 *
 */
package org.neugen.datastructures.parameter;

import org.apache.log4j.Logger;

/**
 * KeyIdentificable is a help class to construct key hierarchies.
 * It's useful to handle hierarchy of .NEU configuration files,
 * which define such by intentation. We say here "key" to describe
 * a text string formed by space divided nonspaced alphanumeric words.
 * Each KeyIdentificable has a part of a key. For Example:
 * A KeyIdentificable A is the container of B, both constructed with
 * A(null,"A") and B(&A,"B"). B.fullKey() gives back "A B".
 *
 * @author Alexander Wanner
 * 
 */
public class KeyIdentificable {

    /** use to log messages  */
    private static final Logger logger = Logger.getLogger(KeyIdentificable.class.getName());
    /** Pointer to KeyIdentificable container "containing" this object. */
    protected KeyIdentificable container;
    /** Special subkey, last part of the full key of this object. */
    protected String key = null;
    protected boolean detailed_output = false;
    private StringBuffer fullKey;

    /**
     * Constructs a KeyIdentificable
     */
    public KeyIdentificable(String lastkey) {
        key = lastkey;
        fullKey = new StringBuffer();
        fullKey.append(key);
    }

    /**
     * Constructs a KeyIdentificable contained in an other with
     * specified @param last key and @param container.
     */
    public KeyIdentificable(KeyIdentificable k, String lastkey) {
        container = k;
        key = lastkey;
        fullKey = new StringBuffer();
        //logger.info("lastKey: " + lastkey);
        //logger.info("fulllKey before: " + fullKey);
        if (container != null) {
            //fullKey.delete(0, fullKey.length());
            fullKey.append(container.getFullKey());
            fullKey.append("/").append(key);
        } else {
            //fullKey.delete(0, fullKey.length());
            fullKey.append(key);
        }
        //fullKey.append(key);
        //logger.info("fulllKey after: " + fullKey);
    }

    /**
     * Redefines a KeyIdentificable as contained in an other with
     * specified @param last key and @param container.
     * Returns true exactly if both container and lastkey
     * are not NULL.
     */
    public boolean init(KeyIdentificable container, String lastkey) {
        //System.out.println("KeyIdentificable init");
        if (container == null) {
            return false;
        }
        this.container = container;
        if (lastkey != null) {
            key = lastkey;
        }
        fullKey.delete(0, fullKey.length());
        fullKey.append(container.getFullKey());
        fullKey.append("/").append(key);
        return true;
    }

    /**
     * Redefines a KeyIdentificable as contained in an other with
     * specified @param last key and @param container.
     * If @param lastkey == NULL, then the old last key
     * saved before remains.
     */
    public void reinit(KeyIdentificable container, String lastkey) {
        this.container = container;
        if (lastkey != null) {
            key = lastkey;
            fullKey.delete(0, fullKey.length());
            if (container != null) {
                fullKey.append(container.getFullKey());
                fullKey.append("/").append(key);
            } else {
                fullKey.append(key);
            }
        }
    }

    /**
     * Returns full key for this KeyIdentificable.
     * 
     * @return the value of fullKey
     */
    public StringBuffer getFullKey() {
        return new StringBuffer(fullKey);
    }

    /** 
     * Return last part of the full key.
     *
     * @return the value of key
     */
    public String getLastKeyPart() {
        return key;
    }

    /**
     * Reeturn container.
     *
     * @return the value of container
     */
    public KeyIdentificable getContainer() {
        return container;
    }
}
