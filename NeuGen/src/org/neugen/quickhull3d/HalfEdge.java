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
 * Copyright John E. Lloyd, 2003. All rights reserved. Permission
 * to use, copy, and modify, without fee, is granted for non-commercial 
 * and research purposes, provided that this copyright notice appears 
 * in all copies.
 *
 * This  software is distributed "as is", without any warranty, including 
 * any implied warranty of merchantability or fitness for a particular
 * use. The authors assume no responsibility for, and shall not be liable
 * for, any special, indirect, or consequential damages, or any damages
 * whatsoever, arising out of or in connection with the use of this
 * software.
 */
package org.neugen.quickhull3d;

/**
 * Represents the half-edges that surround each face in a counter-clockwise
 * direction.
 * 
 * @author John E. Lloyd, Fall 2004
 */
class HalfEdge {

    /**
     * The vertex associated with the head of this half-edge.
     */
    Vertex vertex;
    /**
     * Triangular face associated with this half-edge.
     */
    Face face;
    /**
     * Next half-edge in the triangle.
     */
    HalfEdge next;
    /**
     * Previous half-edge in the triangle.
     */
    HalfEdge prev;
    /**
     * Half-edge associated with the opposite triangle adjacent to this
     * edge.
     */
    HalfEdge opposite;

    /**
     * Constructs a HalfEdge with head vertex <code>v</code> and left-hand
     * triangular face <code>f</code>.
     *
     * @param v
     *                head vertex
     * @param f
     *                left-hand triangular face
     */
    public HalfEdge(Vertex v, Face f) {
        vertex = v;
        face = f;
    }

    public HalfEdge() {
    }

    /**
     * Sets the value of the next edge adjacent (counter-clockwise) to this
     * one within the triangle.
     *
     * @param edge
     *                next adjacent edge
     */
    public void setNext(HalfEdge edge) {
        next = edge;
    }

    /**
     * Gets the value of the next edge adjacent (counter-clockwise) to this
     * one within the triangle.
     *
     * @return next adjacent edge
     */
    public HalfEdge getNext() {
        return next;
    }

    /**
     * Sets the value of the previous edge adjacent (clockwise) to this one
     * within the triangle.
     *
     * @param edge
     *                previous adjacent edge
     */
    public void setPrev(HalfEdge edge) {
        prev = edge;
    }

    /**
     * Gets the value of the previous edge adjacent (clockwise) to this one
     * within the triangle.
     *
     * @return previous adjacent edge
     */
    public HalfEdge getPrev() {
        return prev;
    }

    /**
     * Returns the triangular face located to the left of this half-edge.
     *
     * @return left-hand triangular face
     */
    public Face getFace() {
        return face;
    }

    /**
     * Returns the half-edge opposite to this half-edge.
     *
     * @return opposite half-edge
     */
    public HalfEdge getOpposite() {
        return opposite;
    }

    /**
     * Sets the half-edge opposite to this half-edge.
     *
     * @param edge
     *                opposite half-edge
     */
    public void setOpposite(HalfEdge edge) {
        opposite = edge;
        edge.opposite = this;
    }

    /**
     * Returns the head vertex associated with this half-edge.
     *
     * @return head vertex
     */
    public Vertex head() {
        return vertex;
    }

    /**
     * Returns the tail vertex associated with this half-edge.
     *
     * @return tail vertex
     */
    public Vertex tail() {
        return prev != null ? prev.vertex : null;
    }

    /**
     * Returns the opposite triangular face associated with this half-edge.
     *
     * @return opposite triangular face
     */
    public Face oppositeFace() {
        return opposite != null ? opposite.face : null;
    }

    /**
     * Produces a string identifying this half-edge by the point index
     * values of its tail and head vertices.
     *
     * @return identifying string
     */
    public String getVertexString() {
        if (tail() != null) {
            return "" + tail().index + "-" + head().index;
        } else {
            return "?-" + head().index;
        }
    }

    /**
     * Returns the length of this half-edge.
     *
     * @return half-edge length
     */
    public double length() {
        if (tail() != null) {
            return head().pnt.distance(tail().pnt);
        } else {
            return -1;
        }
    }

    /**
     * Returns the length squared of this half-edge.
     *
     * @return half-edge length squared
     */
    public double lengthSquared() {
        if (tail() != null) {
            return head().pnt.distanceSquared(tail().pnt);
        } else {
            return -1;
        }
    }
    // /**
    // * Computes nrml . (del0 X del1), where del0 and del1
    // * are the direction vectors along this halfEdge, and the
    // * halfEdge he1.
    // *
    // * A product > 0 indicates a left turn WRT the normal
    // */
    // public double turnProduct (HalfEdge he1, Vector3d nrml)
    // {
    // Point3dQH pnt0 = tail().pnt;
    // Point3dQH pnt1 = head().pnt;
    // Point3dQH pnt2 = he1.head().pnt;
    // double del0x = pnt1.x - pnt0.x;
    // double del0y = pnt1.y - pnt0.y;
    // double del0z = pnt1.z - pnt0.z;
    // double del1x = pnt2.x - pnt1.x;
    // double del1y = pnt2.y - pnt1.y;
    // double del1z = pnt2.z - pnt1.z;
    // return (nrml.x*(del0y*del1z - del0z*del1y) +
    // nrml.y*(del0z*del1x - del0x*del1z) +
    // nrml.z*(del0x*del1y - del0y*del1x));
    // }
    }
