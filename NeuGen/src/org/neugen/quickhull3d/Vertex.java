/**
 * Copyright John E. Lloyd, 2004. All rights reserved. Permission to use,
 * copy, modify and redistribute is granted, provided that this copyright
 * notice is retained and the author is given credit whenever appropriate.
 *
 * This  software is distributed "as is", without any warranty, including 
 * any implied warranty of merchantability or fitness for a particular
 * use. The author assumes no responsibility for, and shall not be liable
 * for, any special, indirect, or consequential damages, or any damages
 * whatsoever, arising out of or in connection with the use of this
 * software.
 */
package org.neugen.quickhull3d;

/**
 * Represents vertices of the hull, as well as the points from which it is
 * formed.
 * 
 * @author John E. Lloyd, Fall 2004
 */
class Vertex {

    /**
     * Spatial point associated with this vertex.
     */
    Point3dQH pnt;
    /**
     * Back index into an array.
     */
    int index;
    /**
     * List forward link.
     */
    Vertex prev;
    /**
     * List backward link.
     */
    Vertex next;
    /**
     * Current face that this vertex is outside of.
     */
    Face face;

    /**
     * Constructs a vertex and sets its coordinates to 0.
     */
    public Vertex() {
        pnt = new Point3dQH();
    }

    /**
     * Constructs a vertex with the specified coordinates and index.
     */
    public Vertex(double x, double y, double z, int idx) {
        pnt = new Point3dQH(x, y, z);
        index = idx;
    }
}
