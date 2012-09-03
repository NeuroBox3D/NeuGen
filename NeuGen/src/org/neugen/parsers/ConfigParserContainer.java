package org.neugen.parsers;

import java.io.Serializable;

/**
 * File: ConfigParserContainer.java
 * @author Sergei Wolf
 * Created on 16.12.2009, 11:01:30
 */
public final class ConfigParserContainer implements Serializable {

    static final long serialVersionUID = -8689447816398030143L;
    /** The parser of Parma.neu */
    private static ConfigParser paramParser;
    /** The parser of Interna.neu */
    private static ConfigParser internaParser;

    /**
     * Get the value of internaParser
     *
     * @return the value of internaParser
     */
    public static ConfigParser getInternaParser() {
        return internaParser;
    }

    /**
     * Set the value of internaParser
     *
     * @param drawNumber new value of internaParser
     */
    public static void setInternaParser(ConfigParser internaParser) {
        ConfigParserContainer.internaParser = internaParser;
    }

    /**
     * Get the value of paramParser
     *
     * @return the value of paramParser
     */
    public static ConfigParser getParamParser() {
        return paramParser;
    }

    /**
     * Set the value of paramParser
     *
     * @param drawNumber new value of paramParser
     */
    public static void setParamParser(ConfigParser paramParser) {
        ConfigParserContainer.paramParser = paramParser;
    }
}
