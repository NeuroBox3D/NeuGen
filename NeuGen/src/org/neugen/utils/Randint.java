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
 * It contains several classes for random number generators
 * and random vector generators on the unit sphere.
 *
 * @date 06.05.2004
 *
 *
 * File: Randint.java
 * Created on 05.10.2009, 15:05:27
 */
package org.neugen.utils;

import java.util.Random;
import org.apache.log4j.Logger;

/**
 * Class for random numbers. The integer numbers are first equally distributed in the interval [0,max].
 * There are also integer numbers which are equally distributed to -1 or 1.
 *
 * @author Jens Eberhard
 */
public class Randint {

    //static final long serialVersionUID = -8689337816398534143L;
    /** use to log messages */
    protected final static Logger logger = Logger.getLogger(Randint.class.getName());
    public long randx;
    private Random rand = new Random();


    /** 
     * Constructor. Initialize with a seed.
     * @param s the seed.
     */
    public Randint(long s) {
        randx = s;
    }

    /**
     * Re-initialize the seed.
     * @param s the seed.
     */
    public void seed(long s) {
        randx = s;
    }

    public long abs(long x) {
        return x & 0x7fffffff;
    }

    public static float max() {
        return 2147483648.0f;
    }

    public long draw() {
        //return rand.nextLong();
        //return randx = randx * 1103515245 + 12345;
        //System.out.println("randx: " + randx);
        long tmp = (1103515245 * randx + 12345);
        long m = (long) Math.pow(2, 31);
        randx = (tmp % m);
        //System.out.println("size of randomList: " + randomList.size());
        //System.out.println("randx: " + randx);
        return randx;
    }

    ///< equally -1 or +1
    public int pm_onedraw() {
        return (abs(draw()) < max() / 2) ? -1 : 1;
    } 
}

/**
 * Class for random numbers.
 * The integer numbers are equally distributed in the interval [0,n].
 */
class Urand extends Frand {

    private static final long serialVersionUID = -1180930069184524614L;
    public long n;

    /**
     * Constructor. Initialize with a seed.
     * @param s the seed.
     * @param nn is n.
     */
    public Urand(long nn, long s) {
        super(s);
        n = nn;
    }
}

/**
 * Class for random numbers. The integer numbers are exponentially distributed random numbers.
 * There are also floating point numbers @see Erandfloat.
 */
class Erand extends Randint {

    //static final long serialVersionUID = -1290820069184524614L;
    public long mean;

    /**
     * Constructor. Initialize with a seed.
     * @param s the seed.
     * @param m the mean.
     */
    public Erand(long m, long s) {
        super(s);
        mean = m;
    }
}

/**
 * Class for random numbers. The floating point numbers are exponentially distributed random numbers.
 * There are also integer numbers @see Erand.
 */
class Erandfloat extends Randint {

    public float mean;

    /**
     * Constructor. Initialize with a seed.
     * @param s the seed.
     * @param m the mean.
     */
    public Erandfloat(float m, long s) {
        super(s);
        mean = m;
    }
}
