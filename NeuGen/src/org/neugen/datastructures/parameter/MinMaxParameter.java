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
