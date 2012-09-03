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
