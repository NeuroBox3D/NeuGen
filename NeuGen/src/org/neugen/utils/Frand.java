/*
 * File: Frand.java
 * Created on 06.10.2009, 13:57:12
 *
 */
package org.neugen.utils;

import java.io.Serializable;

/**
 * Class for floating point random numbers. The class contains floating point numbers
 * which are equally distributed in [0,1] or [-1,1].
 *
 * @author Jens Eberhard
 */
public class Frand extends Randint implements Serializable {

    static final long serialVersionUID = -8689337816399130143L;

    /**
     * Constructor. Initialize with a seed.
     * @param s the seed.
     */
    public Frand(long s) {
        super(s);
    }

    /**
     * equally distributed in [0,1]
     */
    public float fdraw() {
        return abs(draw()) / max();
    }

    /**
     * equally distributed in [-1,1]
     */
    public float fpm_onedraw() {
        return 2 * fdraw() - 1.0f;
    }
}
