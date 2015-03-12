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
 * File: Vrand.java
 * Created on 06.10.2009, 13:56:13
 *
 */
package org.neugen.utils;

import java.io.Serializable;
import javax.vecmath.Vector3f;

/**
 * Class for random space vectors on the \a NEUGEN_DIM -dimensional unit sphere.
 * @author Jens Eberhard
 */
public class Vrand extends Frand implements Serializable {

    static final long serialVersionUID = -8689448816398030143L;
    public static final int d = 3;
    /** The deviation for the random rotation vector in a plane orthogonal to a given direction.*/
    public Vector3f rotationDeviation;
    /** The normal vector for the plane. */
    public Vector3f normalVector;

    /**
     * Constructor.
     * Initialize with a seed and sets the deviation vector to (1,1,...,1).
     * @param s the seed.
     */
    public Vrand(long s) {
        super(s);
        rotationDeviation = new Vector3f(1.0f, 1.0f, 1.0f);
        normalVector = new Vector3f();
    }

    /**
     * Function for a valarray of floating point numbers.
     * It returns its euclidean length.
     */
    public static float length(float[] v) {
        float sumVal = 0.0f;
        for (int i = 0; i < v.length; i++) {
            //v[i] = v[i] * v[i];
            sumVal = sumVal + v[i] * v[i];
        }
        return (float) Math.sqrt(sumVal);
        //return Math.sqrt((v * v).sum());
    }

    /**
     * Function for two arrays of floating point numbers.
     * It returns their devision array.
     */
    public static float[] div(float[] v1, float[] v2) {
        float[] result = new float[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v2[i] != 0) {
                result[i] = v1[i] / v2[i];
            }
        }
        return result;
    }

    public static float[] div(float[] v1, float v2) {
        float[] result = new float[v1.length];
        for (int i = 0; i < v1.length; i++) {
            if (v2 != 0) {
                result[i] = v1[i] / v2;
            }
        }
        return result;
    }

    /**
     * Function for a random space vector. It computes a vector which is equally
     * distributed on the d-dimensional unit sphere.
     */
    public Vector3f getRandomRotVector() {
        float phi = 2.0f * (float) Math.PI * fdraw();
        float theta = (float) Math.PI * fdraw();
        if (d == 2) {
            Vector3f v = new Vector3f();
            v.x = (float) Math.cos(phi);
            v.y = (float) Math.sin(phi);
            return v;
        }
        if (d == 3) {
            Vector3f v = new Vector3f();
            v.x = (float) Math.cos(phi) * (float) Math.sin(theta);
            v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
            v.z = (float) Math.cos(theta);
            return v;
        }
        return null;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the d-dimensional unit sphere with angle @a theta. The angle @a theta
     * measures from the abscissa.
     * @param theta the spherical angle.
     */
    public Vector3f getRandomRotVector(float theta) {
        float phi = 2.0f * (float) Math.PI * fdraw();
        if (d == 3) {
            Vector3f v = new Vector3f();
            v.x = (float) Math.cos(theta);
            v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
            v.z = (float) -Math.cos(phi) * (float) Math.sin(theta);
            return v;
        }
        return null;
    }

    public static Vector3f rotVectorY(float theta, Vector3f v) {
        v.x = v.x * (float) Math.cos(theta) + v.z * (float) Math.sin(theta);
        v.z = v.x * (float) -Math.sin(theta) + v.z * (float) Math.cos(theta);
        return v;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the d-dimensional unit sphere with angle @a theta. The angle @a theta
     * measures from the abscissa.
     * @param theta the spherical angle.
     */
    public Vector3f getRandomRotVector(float theta, float range) {
        float rand = (fdraw() + range);
        float phi = 2.0f * (float) Math.PI * rand;
        if (d == 3) {
            Vector3f v = new Vector3f();
            v.x = (float) Math.cos(theta);
            v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
            v.z = (float) -Math.cos(phi) * (float) Math.sin(theta);
            return v;
        }
        return null;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the d-dimensional unit sphere with angle @a theta. The angle @a theta
     * measures from the abscissa.
     * @param theta the spherical angle.
     */
    /*
    public float[] getRandomRotVector(float theta) {
    float phi = 2.0f * (float) Math.PI * fdraw();
    if (d == 2) {
    float[] v = new float[2];
    v[0] = (float) Math.cos(theta);
    v[1] = (float) Math.sin(theta);
    return v;
    }
    if (d == 3) {
    float[] v = new float[3];
    v[0] = (float) Math.cos(theta);
    v[1] = (float) Math.sin(phi) * (float) Math.sin(theta);
    v[2] = (float) -Math.cos(phi) * (float) Math.sin(theta);
    return v;
    }
    return null;
    }
     *
     */
    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the d-dimensional unit sphere with angle @a theta around the given direction
     * vector @a vd. It works only for three dimensions.
     * @param theta the spherical angle.
     * @param vd the vector of direction.
     */
    public Vector3f getRandomRotVector(float theta, Vector3f vd) {
        Vector3f direction = new Vector3f();
        direction.add(vd);
        direction.normalize();

        float vd_phi = (float) Math.atan2(direction.y, direction.x);

        // angles of the vector of direction
        float vd_theta = (float) Math.acos(direction.z);
        float sin_phi = (float) Math.sin(vd_phi);
        float cos_phi = (float) Math.cos(vd_phi);
        float sin_theta = (float) Math.sin(vd_theta);
        float cos_theta = (float) Math.cos(vd_theta);

        // generate a vector around the z-axis
        Vector3f v = new Vector3f();
        float phi = 2.0f * (float) Math.PI * fdraw();
        v.x = (float) Math.cos(phi) * (float) Math.sin(theta);
        v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
        v.z = (float) Math.cos(theta);

        // rotate the vector around the y-axis
        Vector3f v1 = new Vector3f();
        v1.x = cos_theta * v.x - sin_theta * v.z;
        v1.y = v.y;
        v1.z = sin_theta * v.x + cos_theta * v.z;

        // rotate the vector around the z-axis
        Vector3f v2 = new Vector3f();
        v2.x = cos_phi * v1.x + sin_phi * v1.y;
        v2.y = -sin_phi * v1.x + cos_phi * v1.y;
        v2.z = v1.z;
        v2.x *= -1.0f;
        if (v2.length() == 0.0) {
            return v2;
        }
        v2.normalize();
        return v2;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the d-dimensional unit sphere with angle @a theta around the given direction
     * vector @a vd. It works only for three dimensions.
     * @param theta the spherical angle.
     * @param vd the vector of direction.
     */
    public Vector3f getRandomRotVector(float theta, Vector3f vd, float range) {
        //theta = (float) Math.toRadians(theta);
        //range = (float) Math.toRadians(range);
        Vector3f direction = new Vector3f();
        direction.add(vd);
        direction.normalize();
        float vd_phi = (float) Math.atan2(direction.y, direction.x);

        // angles of the vector of direction
        float vd_theta = (float) Math.acos(direction.z);
        float sin_phi = (float) Math.sin(vd_phi);
        float cos_phi = (float) Math.cos(vd_phi);
        float sin_theta = (float) Math.sin(vd_theta);
        float cos_theta = (float) Math.cos(vd_theta);

        // generate a vector around the z-axis
        Vector3f v = new Vector3f();
        float rand = fdraw() + range;
        float phi = 2.0f * (float) Math.PI * rand;
        v.x = (float) Math.cos(phi) * (float) Math.sin(theta);
        v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
        v.z = (float) Math.cos(theta);

        // rotate the vector around the y-axis
        Vector3f v1 = new Vector3f();
        v1.x = cos_theta * v.x - sin_theta * v.z;
        v1.y = v.y;
        v1.z = sin_theta * v.x + cos_theta * v.z;

        // rotate the vector around the z-axis
        Vector3f v2 = new Vector3f();
        v2.x = cos_phi * v1.x + sin_phi * v1.y;
        v2.y = -sin_phi * v1.x + cos_phi * v1.y;
        v2.z = v1.z;
        v2.x *= -1.0f;
        if (v2.length() == 0.0) {
            return v2;
        }
        v2.normalize();
        return v2;
    }

    public Vector3f getRotVector(float theta, Vector3f vd, float phi) {
        Vector3f direction = new Vector3f();
        direction.add(vd);
        direction.normalize();
        float vd_phi = (float) Math.atan2(direction.y, direction.x);

        // angles of the vector of direction
        float vd_theta = (float) Math.acos(direction.z);
        float sin_phi = (float) Math.sin(vd_phi);
        float cos_phi = (float) Math.cos(vd_phi);
        float sin_theta = (float) Math.sin(vd_theta);
        float cos_theta = (float) Math.cos(vd_theta);

        // generate a vector around the z-axis
        Vector3f v = new Vector3f();

        //float phi = 2.0f * (float) Math.PI * rand;
        v.x = (float) Math.cos(phi) * (float) Math.sin(theta);
        v.y = (float) Math.sin(phi) * (float) Math.sin(theta);
        v.z = (float) Math.cos(theta);

        // rotate the vector around the y-axis
        Vector3f v1 = new Vector3f();
        v1.x = cos_theta * v.x - sin_theta * v.z;
        v1.y = v.y;
        v1.z = sin_theta * v.x + cos_theta * v.z;

        // rotate the vector around the z-axis
        Vector3f v2 = new Vector3f();
        v2.x = cos_phi * v1.x + sin_phi * v1.y;
        v2.y = -sin_phi * v1.x + cos_phi * v1.y;
        v2.z = v1.z;
        v2.x *= -1.0f;
        if (v2.length() == 0.0) {
            return v2;
        }
        v2.normalize();
        return v2;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the two-dimensional plane which is orthogonal to the given direction
     * vector \a n_vector. The returned vector has unit length and is shrinked to an ellipse due to
     * the given deviation vector \a rotation_dev. For the default case \a rotation_dev=1, the
     * random vector lies on the circle line. The function works only for three dimensions.
     */
    public float[] getRandomRotOrthogonal() {
        if (d == 3) {
            //n_vector /= length(n_vector);
            normalVector.normalize();
            float vd_phi = (float) Math.atan2(normalVector.y, normalVector.x);
            // angles of the vector of direction
            float vd_theta = (float) Math.acos(normalVector.z);

            float sin_phi = (float) Math.sin(vd_phi);
            float cos_phi = (float) Math.cos(vd_phi);
            float sin_theta = (float) Math.sin(vd_theta);
            float cos_theta = (float) Math.cos(vd_theta);

            //valarray<float> v1(3), v2(3);
            float[] v1 = new float[3];
            float[] v2 = new float[3];

            float phi = 2.0f * (float) Math.PI * fdraw();
            v1[0] = cos_theta * (float) Math.cos(phi) * rotationDeviation.x;
            v1[1] = (float) Math.sin(phi) * rotationDeviation.y;
            v1[2] = sin_theta * (float) Math.cos(phi) * rotationDeviation.x;

            // rotate the vector around the z-axis
            v2[0] = cos_phi * v1[0] + sin_phi * v1[1];
            v2[1] = -sin_phi * v1[0] + cos_phi * v1[1];
            v2[2] = v1[2];
            v2[0] *= -1.0f;
            if (length(v2) == 0.0) {
                return v2;
            } else {
                return div(v2, length(v2));
            }
        }
        return null;
    }

    /**
     * Function for a random space vector. It computes and returns a vector which is equally
     * distributed on the two-dimensional plane which is orthogonal to the given direction
     * vector \a vd. The returned vector has unit length and is shrinked to an ellipse due to
     * the given deviation vector \a rotation_dev. For the default case \a rotation_dev=(1,1,1), the
     * random vector lies on the circle line. The function works only for three dimensions.
     * \param vd the vector of direction.
     */
    public Vector3f getRandomRotOrthogonal(Vector3f vd) {
        if (d == 3) {
            //vd /= length(vd);
            Vector3f direction = new Vector3f(vd);
            direction.normalize();
            float vd_phi = (float) Math.atan2(direction.y, direction.x);

            // angles of the vector of direction
            float vd_theta = (float) Math.acos(direction.z);
            float sin_phi = (float) Math.sin(vd_phi);
            float cos_phi = (float) Math.cos(vd_phi);
            float sin_theta = (float) Math.sin(vd_theta);
            float cos_theta = (float) Math.cos(vd_theta);

            float[] v1 = new float[3];
            float[] v2 = new float[3];

            //valarray<float> v1(3), v2(3);
            float phi = 2.0f * (float) Math.PI * fdraw();
            v1[0] = cos_theta * (float) Math.cos(phi) * rotationDeviation.x;
            v1[1] = (float) Math.sin(phi) * rotationDeviation.y;
            v1[2] = sin_theta * (float) Math.cos(phi) * rotationDeviation.x;

            // rotate the vector around the z-axis
            v2[0] = cos_phi * v1[0] + sin_phi * v1[1];
            v2[1] = -sin_phi * v1[0] + cos_phi * v1[1];
            v2[2] = v1[2];
            v2[0] *= -1.0f;

            if (length(v2) == 0.0) {
                return new Vector3f(v2);
            } else {
                Vector3f dir = new Vector3f(v2);
                dir.normalize();
                return dir;
            }
        }
        return null;
    }

    /** Set the deviation for the random rotation vector orthogonal to a given direction. */
    public void setRotDeviation(Vector3f rd) {
        rotationDeviation = rd;
    }

    /** Set the normal vector for the plane for generating random vectors within the plane. */
    public void setRotNormal(Vector3f n) {
        normalVector = n;
    }
}
