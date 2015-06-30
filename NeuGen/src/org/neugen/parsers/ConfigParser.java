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
 * ConfigParser.java
 *
 * Created on 15.07.2009, 14:03:28
 *
 */
package org.neugen.parsers;

import java.io.Serializable;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;
import org.neugen.datastructures.DataStructureConstants;

import org.neugen.datastructures.Pair;
import org.neugen.datastructures.xml.XMLBool;
import org.neugen.datastructures.xml.XMLNode;
import org.neugen.datastructures.xml.XMLObject;

/**
 * Parser class to import a config file.
 *
 * @author Alexander Wanner
 * @author Sergei Wolf
 *
 */
public final class ConfigParser implements Serializable {

    static final long serialVersionUID = -5789337816398030143L;
    /** Use to log messages, */
    private static Logger logger = Logger.getLogger(ConfigParser.class.getName());
    /** The path of conig file. */
    private String path;
    /** Map of key-value pairs loaded from the config file. */
    private final Map<String, Object> result;
    private Object value;

    /**
     *  Tries to load a config file and save key-value pairs
     *  into result.
     * 
     *  @param path new value of the config file.
     */
    public ConfigParser(String path, XMLObject root) {
        this.path = path;
        result = new HashMap<String, Object>();
        init(root);
    }

    public ConfigParser(XMLObject root) {
        result = new HashMap<String, Object>();
        init(root);
    }

    /**
     * Get the value of result
     *
     * @return the value of result
     */
    public Map<String, Object> getResult() {
        return result;
    }

    /**
     * Get the value
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }
    
     /**
     *  Concatenates a vector of strings (@param stringv)
     *  into a single string. Elements of @param stringv are
     *  separated by space in the result string.
     */
    public StringBuffer sum(LinkedList<String> stringv) {
        StringBuffer returns = new StringBuffer();
        for (int i = 0; i < stringv.size(); i++) {
            returns.append(stringv.get(i));
            //returns += stringv.get(i);
            if (i < stringv.size() - 1) {
                //returns += " ";
                returns.append("/");
            }
        }
        return returns;
    }

    /**
     * Write a value associated with key into ret.
     * @return true exactly if successful.
     */
    public boolean get(StringBuffer key) {
        //logger.info("get start");
        String keyVal = key.toString();
        if (result.containsKey(keyVal)) {
            value = result.get(keyVal);
            return true;
        }
        return false;
    }

    /** 
     * Get the path of the config file.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /** Reinitialize the parser from the old file. */
    public void init(XMLObject root) {
        result.clear();
        LinkedList<Pair<XMLObject, Integer>> nextChildren = new LinkedList<Pair<XMLObject, Integer>>();
        LinkedList<String> actualPath = new LinkedList<String>();
        if (root.getChildrenCount() != 0) {
            nextChildren.addLast(new Pair<XMLObject, Integer>(root, 0));
        }
        while (nextChildren.size() > 0) {
            Pair<XMLObject, Integer> currentPair = nextChildren.getLast();
            XMLObject parent = currentPair.first;
            int childNr = currentPair.second;
            /*
            logger.info();
            logger.info("parent key: " + parent.getKey());
            logger.info("number of children: " + parent.getChildrenCount());
            logger.info("childNr: " + childNr);
             */
            XMLNode child = null;
            if (childNr != parent.getChildrenCount()) {
                child = parent.getChild(childNr);
            }
            nextChildren.removeLast();
            if (child != null) {
                //currentPair.first = parent;
                //currentPair.second = childNr + 1;
                nextChildren.addLast(new Pair<XMLObject, Integer>(parent, childNr + 1));
                if (child instanceof XMLObject) {
                    currentPair.first = XMLObject.convert(child);
                    currentPair.second = 0;
                    nextChildren.addLast(currentPair);
                    actualPath.addLast(child.getKey());
                } else {
                    //logger.info("child key (no object): " + child.getKey());
                    //String value = child.getValue().toString();
                    Object valueLocal = child.getValue();
                    if (child instanceof XMLBool) {
                        if (valueLocal.equals("true")) {
                            valueLocal = "1";
                        } else {
                            valueLocal = "0";
                        }
                    }
                    if(valueLocal instanceof Double) {
                        //logger.info("double inserted!!: " + valueLocal);
                        valueLocal = Float.parseFloat(valueLocal.toString());
                    }
                    Pair<StringBuffer, Object> locPair = new Pair<StringBuffer, Object>(new StringBuffer(), valueLocal);
                    //locPair.first = (sum(actualPath) + " ") + child.getKey(); //pfad
                    locPair.first.append(sum(actualPath).append(DataStructureConstants.NG_PATH_SEPARATOR).append(child.getKey()));
                    /*
                    logger.info("Pfad: " + locPair.first);
                    logger.info("Wert: " + locPair.second);
                     */
                    result.put(locPair.first.toString(), locPair.second);
                }
            } else {
                //logger.info("child ist null");
                if (actualPath.size() != 0) {
                    actualPath.removeLast();
                }
            }
        }
    }
}
