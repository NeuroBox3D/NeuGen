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
