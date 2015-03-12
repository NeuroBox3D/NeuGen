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
 * Point3D.java
 *
 * Created on 17. Februar 2007
 *
 */
package org.neugen.simpletriangulation;

/**
 * A very simple implementation of a point in 3D space.
 * 
 * @author Jens P Eberhard
 */
public class Point3D {

    int x, y, z;

    /** Creates a new instance of Point3D */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Checks if Point3D p1 equals this
     */
    public boolean equals(Point3D p1) {
        return (x == p1.x && y == p1.y && z == p1.z);
    }

    /**
     * Checks if a Point3D p1 is a neighbour of <i>this</i>. Neighbouring
     * points are at most 1 step away in each direction.
     */
    public boolean isNeighbour(Point3D p1) {

        if (this.equals(p1)) {
            return false;
        }

        int dx = (x > p1.x) ? (x - p1.x) : (p1.x - x);
        int dy = (y > p1.y) ? (y - p1.y) : (p1.y - y);
        int dz = (z > p1.z) ? (z - p1.z) : (p1.z - z);
        return (dx <= 1 && dy <= 1 && dz <= 1);
    }

    public final void printData() {
        System.out.print("Pt3D(" + x + " " + y + " " + z + ") ");
    }
}
